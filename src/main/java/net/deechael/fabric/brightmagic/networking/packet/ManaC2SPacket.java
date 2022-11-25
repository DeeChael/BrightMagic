package net.deechael.fabric.brightmagic.networking.packet;

import net.deechael.fabric.brightmagic.mana.ManaData;
import net.deechael.fabric.brightmagic.util.IDataHolder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

@Environment(EnvType.CLIENT)
public class ManaC2SPacket {

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(ManaS2CPacket.MANA_UPDATE, (client, handler, buffer, responseSender) -> {
            int[] bufferArray = buffer.readIntArray();
            int entityId = bufferArray[0];
            int mana = bufferArray[1];
            int maxMana = bufferArray[2];
            client.execute(() -> {
                if (client.player.world.getEntityById(entityId) != null) {
                    IDataHolder dataHolder = (IDataHolder) client.player.world.getEntityById(entityId);
                    ManaData.setMana(dataHolder, mana);
                    ManaData.setMaxMana(dataHolder, maxMana);
                }
            });
        });
    }

}
