package net.deechael.fabric.brightmagic.mixin;

import net.deechael.fabric.brightmagic.element.Element;
import net.deechael.fabric.brightmagic.element.ElementType;
import net.deechael.fabric.brightmagic.element.reaction.ElementReaction;
import net.deechael.fabric.brightmagic.skill.Skill;
import net.deechael.fabric.brightmagic.util.ElementContainer;
import net.deechael.fabric.brightmagic.util.ListUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements ElementContainer {


    private final Map<Element, Long> elements = new HashMap<>();

    private final Map<ElementReaction, Long> lastReacted = new HashMap<>();

    private final List<Element> toBeDeleted = new ArrayList<>();

    private World getWorld$shadow() {
        return ((LivingEntity) (Object) this).getWorld();
    }

    private boolean isTouchingWaterOrRain$shadow() {
        return ((LivingEntity) (Object) this).isTouchingWaterOrRain();
    }
    private boolean isInLava$shadow() {
        return ((LivingEntity) (Object) this).isInLava();
    }

    private Vec3d getPos$shadow() {
        return ((LivingEntity) (Object) this).getPos();
    }

    @Override
    public List<Element> owningElement() {
        return new ArrayList<>(elements.keySet());
    }

    @Override
    public void addElement(Element element) {
        if (this.getWorld$shadow().isClient)
            return;
        if (this.elements.containsKey(element) || this.elements.isEmpty()) {
            this.elements.put(element, System.currentTimeMillis());
            return;
        }
        List<ElementReaction> elementReactions = Element.availableReactions(element);
        boolean reacted = false;
        long now = System.currentTimeMillis();
        for (Element e : this.elements.keySet()) {
            List<ElementReaction> bb = Element.availableReactions(e);
            List<ElementReaction> available = ListUtils.findSame(elementReactions, bb);
            if (available.isEmpty())
                continue;
            for (ElementReaction reaction : available) {
                if (this.lastReacted.containsKey(reaction))
                    if (now - this.lastReacted.get(reaction) < reaction.getCooldown() * 1000L)
                        continue;
                reaction.react(this.getWorld$shadow(), ((LivingEntity) ((Object) this)), e, element);
                this.lastReacted.put(reaction, now);
            }
            this.toBeDeleted.add(e);
            reacted = true;
            break;
        }
        if (reacted) {
            this.toBeDeleted.forEach(this.elements::remove);
            this.toBeDeleted.clear();
            return;
        }
        this.elements.put(element, now);
    }

    @Override
    public void addElement(Element element, ItemStack itemStack, Skill skill) {
        if (this.getWorld$shadow().isClient)
            return;
        if (this.elements.containsKey(element) || this.elements.isEmpty()) {
            this.elements.put(element, System.currentTimeMillis());
            return;
        }
        List<ElementReaction> elementReactions = Element.availableReactions(element);
        boolean reacted = false;
        long now = System.currentTimeMillis();
        for (Element e : this.elements.keySet()) {
            List<ElementReaction> available = ListUtils.findSame(elementReactions, Element.availableReactions(e));
            if (available.isEmpty())
                continue;
            for (ElementReaction reaction : available) {
                if (this.lastReacted.containsKey(reaction))
                    if (now - this.lastReacted.get(reaction) < reaction.getCooldown() * 1000L)
                        continue;
                reaction.react(this.getWorld$shadow(), ((LivingEntity) ((Object) this)), e, element, itemStack, skill);
                this.lastReacted.put(reaction, now);
            }
            this.toBeDeleted.add(e);
            reacted = true;
            break;
        }
        if (reacted) {
            this.toBeDeleted.forEach(this.elements::remove);
            this.toBeDeleted.clear();
            return;
        }
        this.elements.put(element, now);
    }

    @Override
    public void updateElement(List<Element> elements) {
        this.elements.clear();
        long current = System.currentTimeMillis();
        for (Element element : elements) {
            this.elements.put(element, current);
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
        if (this.getWorld$shadow().isClient)
            return;
        long current = System.currentTimeMillis();
        this.elements.forEach((element, time) -> {
            if (current - time > 15 * 1000L)
                toBeDeleted.add(element);
        });
        this.toBeDeleted.forEach(this.elements::remove);
        this.toBeDeleted.clear();
        if (this.isTouchingWaterOrRain$shadow())
            this.addElement(ElementType.HYDRO);
        if (this.isInLava$shadow())
            this.addElement(ElementType.PYRO);
        Vec3d vec3d = this.getPos$shadow();
        BlockState state = this.getWorld$shadow().getBlockState(new BlockPos(vec3d.x, vec3d.y, vec3d.z));
        if (state.getBlock() == Blocks.POWDER_SNOW)
            this.addElement(ElementType.CRYO);
        else if (state.getBlock() == Blocks.FIRE)
            this.addElement(ElementType.PYRO);
    }

    @Inject(method = "damage", at = @At("HEAD"))
    private void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (this.getWorld$shadow().isClient)
            return;
        if (source.isFire())
            this.addElement(ElementType.PYRO);
        else if (source.isExplosive())
            this.addElement(ElementType.PYRO);
        else if (source == DamageSource.HOT_FLOOR)
            this.addElement(ElementType.PYRO);
        else if (source == DamageSource.FREEZE)
            this.addElement(ElementType.CRYO);
        else if (source == DamageSource.LIGHTNING_BOLT)
            this.addElement(ElementType.ELECTRO);
    }

}
