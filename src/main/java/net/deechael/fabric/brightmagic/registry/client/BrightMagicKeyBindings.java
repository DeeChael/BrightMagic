package net.deechael.fabric.brightmagic.registry.client;

import net.deechael.fabric.brightmagic.gui.ElementScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.screen.NamedScreenHandlerFactory;
import org.lwjgl.glfw.GLFW;

public class BrightMagicKeyBindings {

    private final static KeyBinding openElements = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.brightmagic.openElements",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_K,
            "category.brightmagic"
    ));

    public static void init() {
        ClientTickEvents.START_CLIENT_TICK.register((client) -> {
            if (client.world == null)
                return;
            if (openElements.isPressed()) {
                ElementScreen screen = new ElementScreen(client.currentScreen);
                client.setScreen(screen);
            }
        });
    }

}
