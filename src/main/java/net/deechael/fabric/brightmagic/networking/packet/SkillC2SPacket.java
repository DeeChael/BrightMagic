package net.deechael.fabric.brightmagic.networking.packet;

import io.netty.buffer.Unpooled;
import net.deechael.fabric.brightmagic.Constants;
import net.deechael.fabric.brightmagic.element.Element;
import net.deechael.fabric.brightmagic.element.ElementData;
import net.deechael.fabric.brightmagic.mana.ManaData;
import net.deechael.fabric.brightmagic.skill.Skill;
import net.deechael.fabric.brightmagic.skill.SkillData;
import net.deechael.fabric.brightmagic.util.IDataHolder;
import net.deechael.fabric.brightmagic.util.ListUtils;
import net.deechael.fabric.brightmagic.util.NumberUtils;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkillC2SPacket {

    public final static Identifier USE_SKILL = new Identifier(Constants.MOD_ID, "use_skill");
    public final static Identifier SET_SLOT_SKILL = new Identifier(Constants.MOD_ID, "set_slot_skill");

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(SkillS2CPacket.SKILL_UPDATE, (client, handler, buffer, responseSender) -> {
            byte[] bytes = buffer.readByteArray();
            int entityId = readInt(bytes, 0);
            client.execute(() -> {
                if (client.player.world.getEntityById(entityId) != null) {
                    IDataHolder dataHolder = (IDataHolder) client.player.world.getEntityById(entityId);
                    int lastIndex = 4;
                    Map<Integer, Skill> slots = new HashMap<>();
                    for (int i = 4, times = 0; i < bytes.length && times < 4; times++) {
                        int length = readInt(bytes, i);
                        String id = readString(bytes, i + 4, length);
                        if (id.equals("null")) {
                            slots.put(times, null);
                        } else {
                            slots.put(times, Skill.get(new Identifier(id)));
                        }
                        lastIndex = i = i + 4 + length;
                    }
                    for (int i = 0; i < 4; i++) {
                        SkillData.setSlot(dataHolder, i + 1, slots.get(i));
                    }
                    int unlocked = readInt(bytes, lastIndex);
                    if (unlocked > 0) {
                        lastIndex += 4;
                        List<Skill> skills = new ArrayList<>();
                        for (int i = lastIndex; i < bytes.length;) {
                            int length = readInt(bytes, i);
                            skills.add(Skill.get(new Identifier(readString(bytes, i + 4, length))));
                            i += 4 + length;
                        }
                        SkillData.setUnlocked(dataHolder, skills);
                    } else {
                        SkillData.setUnlocked(dataHolder, new ArrayList<>());
                    }
                }
            });
        });
    }

    public static void writeC2SUseSkillPacket(ClientPlayerEntity clientPlayerEntity, int slot) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        List<Byte> byteList = new ArrayList<>();
        String uuid = clientPlayerEntity.getUuidAsString();
        for (byte b : NumberUtils.intToBytes(uuid.length()))
            byteList.add(b);
        for (byte b : uuid.getBytes(StandardCharsets.UTF_8))
            byteList.add(b);
        for (byte b : NumberUtils.intToBytes(slot))
            byteList.add(b);
        buf.writeByteArray(ListUtils.classToPrimitive(byteList.toArray(new Byte[0])));
        clientPlayerEntity.networkHandler
                .sendPacket(new CustomPayloadC2SPacket(SkillC2SPacket.USE_SKILL, buf));
    }

    public static void writeC2SSetSlotSkillPacket(ClientPlayerEntity clientPlayerEntity, int slot, Skill skill) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        List<Byte> byteList = new ArrayList<>();
        String uuid = clientPlayerEntity.getUuidAsString();
        for (byte b : NumberUtils.intToBytes(uuid.length()))
            byteList.add(b);
        for (byte b : uuid.getBytes(StandardCharsets.UTF_8))
            byteList.add(b);
        for (byte b : NumberUtils.intToBytes(slot))
            byteList.add(b);
        String skillId = skill == null ? "null" : skill.getId().toString();
        for (byte b : NumberUtils.intToBytes(skillId.length()))
            byteList.add(b);
        for (byte b : skillId.getBytes(StandardCharsets.UTF_8))
            byteList.add(b);
        buf.writeByteArray(ListUtils.classToPrimitive(byteList.toArray(new Byte[0])));
        clientPlayerEntity.networkHandler
                .sendPacket(new CustomPayloadC2SPacket(SkillC2SPacket.SET_SLOT_SKILL, buf));
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
