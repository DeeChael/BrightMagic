package net.deechael.fabric.brightmagic.event;

import net.deechael.fabric.brightmagic.mana.ManaData;
import net.deechael.fabric.brightmagic.util.IDataHolder;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerTickEventListener implements ServerTickEvents.StartTick {

    private Map<UUID, Long> lastTickTime = new HashMap<>();

    @Override
    public void onStartTick(MinecraftServer server) {
        long current = System.currentTimeMillis();
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            if (!lastTickTime.containsKey(player.getUuid()))
                lastTickTime.put(player.getUuid(), 0L);
            if (current - lastTickTime.get(player.getUuid()) < (2 * 1000L))
                continue;
            this.lastTickTime.put(player.getUuid(), current);
            ManaData.addMana((IDataHolder) player, 1);
        }
    }

}
