package net.deechael.fabric.brightmagic.mixin;

import net.deechael.fabric.brightmagic.element.ElementType;
import net.deechael.fabric.brightmagic.util.ElementContainer;
import net.deechael.fabric.brightmagic.util.IDataHolder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
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

@Mixin(Entity.class)
public abstract class EntityMixin implements IDataHolder {

    @Shadow public abstract World getWorld();

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
    private void onStruckByLightning(ServerWorld world, LightningEntity lightning, CallbackInfo ci) {
        if (((Object) this) instanceof LivingEntity)
            ((ElementContainer) this).addElement(ElementType.ELECTRO);
    }

}
