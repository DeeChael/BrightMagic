package net.deechael.fabric.brightmagic.networking.packet;

import io.netty.buffer.Unpooled;
import net.deechael.fabric.brightmagic.Constants;
import net.deechael.fabric.brightmagic.element.ElementData;
import net.deechael.fabric.brightmagic.element.Element;
import net.deechael.fabric.brightmagic.util.ElementContainer;
import net.deechael.fabric.brightmagic.util.IDataHolder;
import net.deechael.fabric.brightmagic.util.ListUtils;
import net.deechael.fabric.brightmagic.util.NumberUtils;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ElementS2CPacket {

    public final static Identifier ELEMENT_UPDATE = new Identifier(Constants.MOD_ID, "element_update");
    public final static Identifier ELEMENT_ON_BODY_UPDATE = new Identifier(Constants.MOD_ID, "element_on_body_update");

    public static void init() {
    }

    public static void writeS2CElementUpdatePacket(ServerPlayerEntity serverPlayerEntity) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        IDataHolder holder = (IDataHolder) serverPlayerEntity;
        int unlocked = ElementData.getUnlocked(holder);
        List<Byte> bytes = new ArrayList<>();
        for (byte b : NumberUtils.intToBytes(serverPlayerEntity.getId()))
            bytes.add(b);
        for (byte b : NumberUtils.intToBytes(unlocked))
            bytes.add(b);
        if (unlocked > 0) {
            for (Element element : ElementData.getUnlockedElements(holder)) {
                String id = element.getId().toString();
                for (byte b : NumberUtils.intToBytes(id.length()))
                    bytes.add(b);
                for (byte b : id.getBytes(StandardCharsets.UTF_8))
                    bytes.add(b);
            }
        }
        buf.writeByteArray(ListUtils.classToPrimitive(bytes.toArray(new Byte[0])));
        serverPlayerEntity.networkHandler.sendPacket(new CustomPayloadS2CPacket(ElementS2CPacket.ELEMENT_UPDATE, buf));
    }

    public static void writeS2CElementOnBodyUpdatePacket(ServerPlayerEntity serverPlayerEntity) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        ElementContainer container = (ElementContainer) serverPlayerEntity;
        int unlocked = container.owningElement().size();
        List<Byte> bytes = new ArrayList<>();
        for (byte b : NumberUtils.intToBytes(serverPlayerEntity.getId()))
            bytes.add(b);
        for (byte b : NumberUtils.intToBytes(unlocked))
            bytes.add(b);
        if (unlocked > 0) {
            for (Element element : container.owningElement()) {
                String id = element.getId().toString();
                for (byte b : NumberUtils.intToBytes(id.length()))
                    bytes.add(b);
                for (byte b : id.getBytes(StandardCharsets.UTF_8))
                    bytes.add(b);
            }
        }
        buf.writeByteArray(ListUtils.classToPrimitive(bytes.toArray(new Byte[0])));
        serverPlayerEntity.networkHandler.sendPacket(new CustomPayloadS2CPacket(ElementS2CPacket.ELEMENT_ON_BODY_UPDATE, buf));
    }

}