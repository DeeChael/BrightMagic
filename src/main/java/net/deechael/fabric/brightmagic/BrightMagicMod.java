package net.deechael.fabric.brightmagic;

import net.deechael.fabric.brightmagic.element.ElementType;
import net.deechael.fabric.brightmagic.event.PlayerTickEventListener;
import net.deechael.fabric.brightmagic.networking.packet.ElementS2CPacket;
import net.deechael.fabric.brightmagic.networking.packet.ManaS2CPacket;
import net.deechael.fabric.brightmagic.registry.BrightMagicItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class BrightMagicMod implements ModInitializer {

	@Override
	public void onInitialize() {
		BrightMagicItems.init();
		ManaS2CPacket.init();
		ElementS2CPacket.init();
		ElementType.init();
		ServerTickEvents.START_SERVER_TICK.register(new PlayerTickEventListener());
		ServerTickEvents.START_WORLD_TICK.register((world) -> {

		});
	}

}
