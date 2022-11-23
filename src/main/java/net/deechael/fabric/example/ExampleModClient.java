package net.deechael.fabric.example;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;

public class ExampleMod implements ClientModInitializer {

	private static ExampleConfig config;

	@Override
	public void onInitializeClient() {
		AutoConfig.register(ExampleConfig.class, GsonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(ExampleConfig.class).getConfig();
	}

	public static ExampleConfig getConfig() {
		return config;
	}

}
