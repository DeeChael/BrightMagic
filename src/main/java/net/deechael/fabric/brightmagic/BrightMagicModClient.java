package net.deechael.fabric.brightmagic;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.deechael.fabric.brightmagic.networking.packet.ElementC2SPacket;
import net.deechael.fabric.brightmagic.networking.packet.ManaC2SPacket;
import net.deechael.fabric.brightmagic.registry.client.BrightMagicKeyBindings;
import net.fabricmc.api.ClientModInitializer;

public class BrightMagicModClient implements ClientModInitializer {

	private static BrightMagicConfig config;

	@Override
	public void onInitializeClient() {
		ManaC2SPacket.init();
		ElementC2SPacket.init();
		BrightMagicKeyBindings.init();
		AutoConfig.register(BrightMagicConfig.class, GsonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(BrightMagicConfig.class).getConfig();
	}

	public static BrightMagicConfig getConfig() {
		return config;
	}

}
