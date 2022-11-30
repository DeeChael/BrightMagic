package net.deechael.fabric.brightmagic.networking.packet;

import io.netty.buffer.Unpooled;
import net.deechael.fabric.brightmagic.Constants;
import net.deechael.fabric.brightmagic.item.UltimateWandItem;
import net.deechael.fabric.brightmagic.registry.BrightMagicItems;
import net.deechael.fabric.brightmagic.skill.Skill;
import net.deechael.fabric.brightmagic.skill.SkillData;
import net.deechael.fabric.brightmagic.util.IDataHolder;
import net.deechael.fabric.brightmagic.util.ListUtils;
import net.deechael.fabric.brightmagic.util.NumberUtils;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
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
            ItemStack inHand = player.getStackInHand(Hand.MAIN_HAND);
            if (!(inHand.getItem() instanceof UltimateWandItem))
                return;
            int slot = readInt(bytes, 4 + uuidLength);
            if (slot > 4 || slot < 1)
                return;
            Skill skill = SkillData.getSlot((IDataHolder) player, slot);
            if (skill == null)
                return;
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
            ((UltimateWandItem) inHand.getItem()).use(player.world, player, inHand, skill);
        }));
        ServerPlayNetworking.registerGlobalReceiver(SkillC2SPacket.SET_SLOT_SKILL, ((server, nothing, handler, buf, responseSender) ->  {
            byte[] bytes = buf.readByteArray();
            int uuidLength = readInt(bytes, 0);
            UUID uuid = UUID.fromString(readString(bytes, 4, uuidLength));
            if (uuid != nothing.getUuid())
                return;
            ServerPlayerEntity player = server.getPlayerManager().getPlayer(uuid);
            if (player == null)
                return;
            int slot = readInt(bytes, 4 + uuidLength);
            if (slot > 4 || slot < 1)
                return;
            int skillLength = readInt(bytes, 4 + uuidLength + 4);
            String skillId = readString(bytes, 4 + uuidLength + 4 + 4, skillLength);
            if (Objects.equals(skillId, "null"))
                SkillData.setSlot((IDataHolder) player, slot, null);
            else {
                Skill skill = Skill.get(new Identifier(skillId));
                if (skill == null)
                    return;
                if (SkillData.isUnlocked((IDataHolder) player, skill))
                    SkillData.setSlot((IDataHolder) player, slot, skill);
            }
        }));
    }

    public static void writeS2CSkillUpdatePacket(ServerPlayerEntity serverPlayerEntity) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        IDataHolder holder = (IDataHolder) serverPlayerEntity;
        List<Byte> bytes = new ArrayList<>();
        for (byte b : NumberUtils.intToBytes(serverPlayerEntity.getId()))
            bytes.add(b);
        for (int i = 0; i < 4; i++) {
            Skill skill = SkillData.getSlot(holder, i + 1);
            String id = skill == null ? "null" : skill.getId().toString();
            for (byte b : NumberUtils.intToBytes(id.length()))
                bytes.add(b);
            for (byte b : id.getBytes(StandardCharsets.UTF_8))
                bytes.add(b);
        }
        int unlocked = SkillData.getUnlocked(holder);
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
