package net.deechael.fabric.brightmagic;

import net.deechael.fabric.brightmagic.element.ElementType;
import net.deechael.fabric.brightmagic.event.PlayerTickEventListener;
import net.deechael.fabric.brightmagic.networking.packet.ElementS2CPacket;
import net.deechael.fabric.brightmagic.networking.packet.ManaS2CPacket;
import net.deechael.fabric.brightmagic.networking.packet.SkillS2CPacket;
import net.deechael.fabric.brightmagic.registry.BrightMagicElementReactions;
import net.deechael.fabric.brightmagic.registry.BrightMagicItems;
import net.deechael.fabric.brightmagic.registry.BrightMagicSkills;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class BrightMagicMod implements ModInitializer {

	@Override
	public void onInitialize() {
		ElementType.init();
		BrightMagicElementReactions.init();
		BrightMagicSkills.init();
		BrightMagicItems.init();

		ManaS2CPacket.init();
		ElementS2CPacket.init();
		SkillS2CPacket.init();
		ServerTickEvents.START_SERVER_TICK.register(new PlayerTickEventListener());
	}

}
