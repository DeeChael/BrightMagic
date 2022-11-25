package net.deechael.fabric.brightmagic.networking.packet;

import net.deechael.fabric.brightmagic.element.ElementData;
import net.deechael.fabric.brightmagic.element.Element;
import net.deechael.fabric.brightmagic.util.ElementContainer;
import net.deechael.fabric.brightmagic.util.IDataHolder;
import net.deechael.fabric.brightmagic.util.NumberUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ElementC2SPacket {

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(ElementS2CPacket.ELEMENT_UPDATE, (client, handler, buffer, responseSender) -> {
            byte[] bytes = buffer.readByteArray();
            int entityId = readInt(bytes, 0);
            client.execute(() -> {
                if (client.player.world.getEntityById(entityId) != null) {
                    int unlocked = readInt(bytes, 4);
                    IDataHolder dataHolder = (IDataHolder) client.player.world.getEntityById(entityId);
                    if (unlocked > 0) {
                        List<Element> elements = new ArrayList<>();
                        for (int i = 8; i < bytes.length;) {
                            int length = readInt(bytes, i);
                            elements.add(Element.get(new Identifier(readString(bytes, i + 4, length))));
                            i += 4 + length;
                        }
                        ElementData.setUnlocked(dataHolder, elements);
                    } else {
                        ElementData.setUnlocked(dataHolder, new ArrayList<>());
                    }
                }
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(ElementS2CPacket.ELEMENT_ON_BODY_UPDATE, (client, handler, buffer, responseSender) -> {
            byte[] bytes = buffer.readByteArray();
            int entityId = readInt(bytes, 0);
            client.execute(() -> {
                if (client.player.world.getEntityById(entityId) != null) {
                    int unlocked = readInt(bytes, 4);
                    ElementContainer container = (ElementContainer) client.player.world.getEntityById(entityId);
                    if (unlocked > 0) {
                        List<Element> elements = new ArrayList<>();
                        for (int i = 8; i < bytes.length;) {
                            int length = readInt(bytes, i);
                            elements.add(Element.get(new Identifier(readString(bytes, i + 4, length))));
                            i += 4 + length;
                        }
                        container.updateElement(elements);
                    } else {
                        container.updateElement(new ArrayList<>());
                    }
                }
            });
        });
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
