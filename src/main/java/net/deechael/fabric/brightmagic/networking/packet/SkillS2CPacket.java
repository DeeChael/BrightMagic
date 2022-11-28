package net.deechael.fabric.brightmagic.networking.packet;

import net.deechael.fabric.brightmagic.registry.BrightMagicItems;
import net.deechael.fabric.brightmagic.util.NumberUtils;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class SkillS2CPacket {

    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(SkillC2SPacket.USE_SKILL, ((server, nothing, handler, buf, responseSender) -> {
            byte[] bytes = buf.readByteArray();
            int uuidLength = readInt(bytes, 0);
            UUID uuid = UUID.fromString(readString(bytes, 4, uuidLength));
            if (uuid != nothing.getUuid())
                return;
            ServerPlayerEntity player = server.getPlayerManager().getPlayer(uuid);
            if (player == null)
                return;
            if (player.getStackInHand(Hand.MAIN_HAND).getItem() != BrightMagicItems.FINAL_WAND)
                return;
            // TODO
        }));
    }

    private static int readInt(byte[] bytes, int startAt) {
        byte[] bs = new byte[4];
        System.arraycopy(bytes, startAt, bs, 0, 4);
        return NumberUtils.bytesToInt(bs);
    }

    private static String readString(byte[] bytes, int startAt, int length) {
        byte[] bs = new byte[length];
        System.arraycopy(bytes, startAt, bs, 0, length);
        return new String(bs, StandardCharsets.UTF_8);
    }

}
