package net.deechael.fabric.brightmagic.networking.packet;

import io.netty.buffer.Unpooled;
import net.deechael.fabric.brightmagic.Constants;
import net.deechael.fabric.brightmagic.basic.BasicWandItem;
import net.deechael.fabric.brightmagic.element.Element;
import net.deechael.fabric.brightmagic.element.ElementData;
import net.deechael.fabric.brightmagic.mana.ManaData;
import net.deechael.fabric.brightmagic.registry.BrightMagicItems;
import net.deechael.fabric.brightmagic.skill.Skill;
import net.deechael.fabric.brightmagic.skill.SkillData;
import net.deechael.fabric.brightmagic.util.IDataHolder;
import net.deechael.fabric.brightmagic.util.ListUtils;
import net.deechael.fabric.brightmagic.util.NumberUtils;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class SkillS2CPacket {

    public final static Identifier SKILL_UPDATE = new Identifier(Constants.MOD_ID, "skill_update");

    private final static Map<UUID, Map<Skill, Long>> lastInvoked = new HashMap<>();

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
            int slot = readInt(bytes, 4 + uuidLength);
            if (slot > 4 || slot < 1)
                return;
            Skill skill = SkillData.getSlot((IDataHolder) player, slot);
            if (skill == null)
                return;
            if (!player.isCreative()) {
                if (player.getStackInHand(Hand.MAIN_HAND).getItem() != BrightMagicItems.FINAL_WAND)
                    return;
                if (ManaData.getMana((IDataHolder) player) < skill.getManaCost()) {
                    player.sendMessage(Text.translatable("brightmagic.messages.mananotenough").formatted(Formatting.RED));
                    return;
                }
                if (skill.getElement() != null && !ElementData.hasUnlocked((IDataHolder) player, skill.getElement())) {
                    player.sendMessage(Text.translatable("brightmagic.messages.noelement").formatted(Formatting.RED));
                    return;
                }
                ManaData.removeMana((IDataHolder) player, skill.getManaCost());
            }
            long current = System.currentTimeMillis();
            if (!lastInvoked.containsKey(uuid))
                lastInvoked.put(uuid, new HashMap<>());
            if (lastInvoked.get(uuid).containsKey(skill)) {
                if (current - lastInvoked.get(uuid).get(skill) < skill.getCd() * 1000L) {
                    player.sendMessage(Text.translatable("brightmagic.messages.currentincd").formatted(Formatting.RED));
                    return;
                }
            }
            lastInvoked.get(uuid).put(skill, current);
            skill.use(player, player.world, player.getStackInHand(Hand.MAIN_HAND));
        }));
    }

    public static void writeS2CSkillUpdatePacket(ServerPlayerEntity serverPlayerEntity) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        IDataHolder holder = (IDataHolder) serverPlayerEntity;
        int unlocked = ElementData.getUnlocked(holder);
        List<Byte> bytes = new ArrayList<>();
        for (byte b : NumberUtils.intToBytes(serverPlayerEntity.getId()))
            bytes.add(b);
        for (byte b : NumberUtils.intToBytes(unlocked))
            bytes.add(b);
        for (int i = 1; i <= 4; i++) {
            Skill skill = SkillData.getSlot(holder, i);
            String id = skill == null ? "null" : skill.getId().toString();
            for (byte b : NumberUtils.intToBytes(id.length()))
                bytes.add(b);
            for (byte b : id.getBytes(StandardCharsets.UTF_8))
                bytes.add(b);
        }
        for (byte b : NumberUtils.intToBytes(unlocked))
            bytes.add(b);
        if (unlocked > 0) {
            for (Skill skill : SkillData.getUnlockedSkills(holder)) {
                String id = skill.getId().toString();
                for (byte b : NumberUtils.intToBytes(id.length()))
                    bytes.add(b);
                for (byte b : id.getBytes(StandardCharsets.UTF_8))
                    bytes.add(b);
            }
        }
        buf.writeByteArray(ListUtils.classToPrimitive(bytes.toArray(new Byte[0])));
        serverPlayerEntity.networkHandler.sendPacket(new CustomPayloadS2CPacket(SkillS2CPacket.SKILL_UPDATE, buf));
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
