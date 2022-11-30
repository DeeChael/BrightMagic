package net.deechael.fabric.brightmagic.mixin;

import net.deechael.fabric.brightmagic.element.ElementData;
import net.deechael.fabric.brightmagic.mana.ManaData;
import net.deechael.fabric.brightmagic.networking.packet.ElementS2CPacket;
import net.deechael.fabric.brightmagic.networking.packet.ManaS2CPacket;
import net.deechael.fabric.brightmagic.networking.packet.SkillS2CPacket;
import net.deechael.fabric.brightmagic.util.IDataHolder;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    private int syncedMana = -1;
    private int syncedMaxMana = -1;

    @Inject(method = "playerTick", at = @At("HEAD"))
    private void playerTick(CallbackInfo ci) {
        IDataHolder dataHolder = (IDataHolder) this;
        if (this.syncedMana != ManaData.getMana(dataHolder) || this.syncedMaxMana != ManaData.getMaxMana(dataHolder)) {
            ManaS2CPacket.writeS2CManaUpdatePacket((ServerPlayerEntity) (Object) this);
            this.syncedMana = ManaData.getMana(dataHolder);
            this.syncedMaxMana = ManaData.getMaxMana(dataHolder);
        }
        ElementS2CPacket.writeS2CElementUpdatePacket((ServerPlayerEntity) (Object) this);
        ElementS2CPacket.writeS2CElementOnBodyUpdatePacket((ServerPlayerEntity) (Object) this);
        SkillS2CPacket.writeS2CSkillUpdatePacket((ServerPlayerEntity) (Object) this);
    }

    @Inject(method = "copyFrom(Lnet/minecraft/server/network/ServerPlayerEntity;Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;setHealth(F)V"))
    public void copyFrom$invoke(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo info) {
        ManaData.setMana((IDataHolder) this, ManaData.getMana((IDataHolder) oldPlayer));
        ManaData.setMaxMana((IDataHolder) this, ManaData.getMaxMana((IDataHolder) oldPlayer));
        ElementData.setUnlocked((IDataHolder) this, ElementData.getUnlockedElements((IDataHolder) oldPlayer));
    }

    @Inject(method = "copyFrom(Lnet/minecraft/server/network/ServerPlayerEntity;Z)V", at = @At(value = "TAIL"))
    public void copyFrom$tail(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo info) {
        this.syncedMana = -1;
        this.syncedMaxMana = -1;
    }

    @Inject(method = "teleport", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;setWorld(Lnet/minecraft/server/world/ServerWorld;)V"))
    private void teleport(ServerWorld targetWorld, double x, double y, double z, float yaw, float pitch, CallbackInfo info) {
        this.syncedMana = -1;
        this.syncedMaxMana = -1;
    }

    @Inject(method = "moveToWorld", at = @At(value = "FIELD", target = "Lnet/minecraft/server/network/ServerPlayerEntity;syncedFoodLevel:I", ordinal = 0))
    private void moveToWorld(ServerWorld destination, CallbackInfoReturnable<Entity> info) {
        this.syncedMana = -1;
        this.syncedMaxMana = -1;
    }

}
