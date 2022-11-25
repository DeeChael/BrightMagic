package net.deechael.fabric.brightmagic.networking.packet;

import io.netty.buffer.Unpooled;
import net.deechael.fabric.brightmagic.Constants;
import net.deechael.fabric.brightmagic.mana.ManaData;
import net.deechael.fabric.brightmagic.util.IDataHolder;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class ManaS2CPacket {

    public final static Identifier MANA_UPDATE = new Identifier(Constants.MOD_ID, "mana_updated");

    public static void init() {
    }

    public static void writeS2CManaUpdatePacket(ServerPlayerEntity serverPlayerEntity) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        IDataHolder holder = (IDataHolder) serverPlayerEntity;
        buf.writeIntArray(new int[] { serverPlayerEntity.getId(), ManaData.getMana(holder), ManaData.getMaxMana(holder)});
        serverPlayerEntity.networkHandler.sendPacket(new CustomPayloadS2CPacket(ManaS2CPacket.MANA_UPDATE, buf));
    }

}
