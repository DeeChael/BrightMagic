package net.deechael.fabric.brightmagic.mixin;

import net.deechael.fabric.brightmagic.element.Element;
import net.deechael.fabric.brightmagic.element.reaction.ElementReaction;
import net.deechael.fabric.brightmagic.element.ElementType;
import net.deechael.fabric.brightmagic.skill.Skill;
import net.deechael.fabric.brightmagic.util.ElementContainer;
import net.deechael.fabric.brightmagic.util.IDataHolder;
import net.deechael.fabric.brightmagic.util.ListUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(Entity.class)
public abstract class EntityMixin implements IDataHolder, ElementContainer {

    @Shadow public abstract World getWorld();

    @Shadow public abstract boolean isOnFire();

    @Shadow public abstract boolean isTouchingWater();

    @Shadow public abstract int getFireTicks();

    @Shadow protected abstract int getBurningDuration();

    @Shadow public abstract boolean isInLava();

    private final Map<Element, Long> elements = new HashMap<>();

    private final List<Element> toBeDeleted = new ArrayList<>();

    @Override
    public List<Element> owningElement() {
        return new ArrayList<>(elements.keySet());
    }

    @Override
    public void addElement(Element element) {
        if (this.elements.containsKey(element) || this.elements.isEmpty()) {
            this.elements.put(element, System.currentTimeMillis());
            return;
        }
        List<ElementReaction> elementReactions = Element.availableReactions(element);
        for (Element e : this.elements.keySet()) {
            List<ElementReaction> available = ListUtils.findSame(elementReactions, Element.availableReactions(e));
            if (available.isEmpty())
                continue;
            for (ElementReaction reaction : available) {
                reaction.react(this.getWorld(), ((Entity) ((Object) this)));
            }
            this.elements.remove(e);
            return;
        }
        this.elements.put(element, System.currentTimeMillis());
    }

    @Override
    public void addElement(Element element, ItemStack itemStack, Skill skill) {
        if (this.elements.containsKey(element) || this.elements.isEmpty()) {
            this.elements.put(element, System.currentTimeMillis());
            return;
        }
        List<ElementReaction> elementReactions = Element.availableReactions(element);
        for (Element e : this.elements.keySet()) {
            List<ElementReaction> available = ListUtils.findSame(elementReactions, Element.availableReactions(e));
            if (available.isEmpty())
                continue;
            for (ElementReaction reaction : available) {
                reaction.react(this.getWorld(), ((Entity) ((Object) this)), itemStack, skill);
            }
            this.elements.remove(e);
            return;
        }
        this.elements.put(element, System.currentTimeMillis());
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
        if (this.isInLava())
            this.addElement(ElementType.PYRO);
        if (this.isTouchingWater())
            this.addElement(ElementType.HYDRO);
        long current = System.currentTimeMillis();
        this.elements.forEach((element, time) -> {
            if (current - time > 15 * 1000L)
                toBeDeleted.add(element);
        });
        this.toBeDeleted.forEach(this.elements::remove);
        this.toBeDeleted.clear();
    }

    private NbtCompound manaData;
    private NbtCompound elementData;
    private NbtCompound skillData;

    @Override
    public NbtCompound getManaData() {
        if (this.manaData == null) {
            NbtCompound manaData = new NbtCompound();
            manaData.putInt("mana", 100);
            manaData.putInt("maxMana", 100);
            this.manaData = manaData;
        } else {
            if (!manaData.contains("mana"))
                this.manaData.putInt("mana", 100);
            if (!manaData.contains("maxMana"))
                this.manaData.putInt("maxMana", 100);
        }
        return this.manaData;
    }

    @Override
    public NbtCompound getElementData() {
        if (this.elementData == null) {
            NbtCompound elementData = new NbtCompound();
            elementData.putInt("unlocked", 0);
            elementData.put("unlockedElements", new NbtList());
            this.elementData = elementData;
        } else {
            if (!elementData.contains("unlocked"))
                this.elementData.putInt("mana", 0);
            if (!elementData.contains("unlockedElements"))
                this.elementData.put("maxMana", new NbtList());
        }
        return this.elementData;
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    private void writeNbt(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> callbackInfoReturnable) {
        NbtCompound brightMagicData = new NbtCompound();
        brightMagicData.put("manaData", getManaData());
        brightMagicData.put("elementData", getElementData());
        nbt.put("brightmagic", brightMagicData);
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    private void readNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
        if (nbt.contains("brightmagic", 10)) {
            NbtCompound brightmagic = nbt.getCompound("brightmagic");
            if (brightmagic.contains("manaData"))
                this.manaData = brightmagic.getCompound("manaData");
            if (brightmagic.contains("elementData"))
                this.elementData = brightmagic.getCompound("elementData");
        } else {
            NbtCompound manaData = new NbtCompound();
            manaData.putInt("mana", 100);
            manaData.putInt("maxMana", 100);
            this.manaData = manaData;
            NbtCompound elementData = new NbtCompound();
            elementData.putInt("unlocked", 0);
            elementData.put("unlockedElements", new NbtList());
            this.elementData = elementData;
        }
    }

    @Inject(method = "onStruckByLightning", at = @At("HEAD"))
    private void onLightning(ServerWorld world, LightningEntity lightning, CallbackInfo ci) {
        this.addElement(ElementType.ELECTRO);
    }

    @Inject(method = "damage", at = @At("HEAD"))
    private void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source == DamageSource.IN_FIRE)
            this.addElement(ElementType.PYRO);
        else if (source.isExplosive())
            this.addElement(ElementType.PYRO);
        else if (source == DamageSource.HOT_FLOOR)
            this.addElement(ElementType.PYRO);
        else if (source == DamageSource.FREEZE)
            this.addElement(ElementType.CRYO);
    }

}
