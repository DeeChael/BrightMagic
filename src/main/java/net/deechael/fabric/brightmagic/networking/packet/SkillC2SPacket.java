package net.deechael.fabric.brightmagic.networking.packet;

import io.netty.buffer.Unpooled;
import net.deechael.fabric.brightmagic.Constants;
import net.deechael.fabric.brightmagic.skill.Skill;
import net.deechael.fabric.brightmagic.util.ListUtils;
import net.deechael.fabric.brightmagic.util.NumberUtils;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SkillC2SPacket {

    public final static Identifier USE_SKILL = new Identifier(Constants.MOD_ID, "use_skill");

    public static void init() {
    }

    public static void writeS2CManaUpdatePacket(ClientPlayerEntity clientPlayerEntity, Skill skill) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        List<Byte> byteList = new ArrayList<>();
        String uuid = clientPlayerEntity.getUuidAsString();
        for (byte b : NumberUtils.intToBytes(uuid.length()))
            byteList.add(b);
        for (byte b : uuid.getBytes(StandardCharsets.UTF_8))
            byteList.add(b);
        String id = skill.getIdentifier().toString();
        for (byte b : NumberUtils.intToBytes(id.length()))
            byteList.add(b);
        for (byte b : id.getBytes(StandardCharsets.UTF_8))
            byteList.add(b);
        buf.writeByteArray(ListUtils.classToPrimitive(byteList.toArray(new Byte[0])));
        clientPlayerEntity.networkHandler
                .sendPacket(new CustomPayloadC2SPacket(SkillC2SPacket.USE_SKILL, buf));
    }

}
