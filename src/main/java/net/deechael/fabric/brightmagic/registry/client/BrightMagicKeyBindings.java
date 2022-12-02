package net.deechael.fabric.brightmagic.registry.client;

import net.deechael.fabric.brightmagic.client.SkillCooldownHelper;
import net.deechael.fabric.brightmagic.gui.ElementScreen;
import net.deechael.fabric.brightmagic.networking.packet.SkillC2SPacket;
import net.deechael.fabric.brightmagic.skill.SkillData;
import net.deechael.fabric.brightmagic.util.IDataHolder;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class BrightMagicKeyBindings {

    private final static KeyBinding openElements = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.brightmagic.openElements",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_I,
            "category.brightmagic"
    ));

    private final static KeyBinding skill_1 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.brightmagic.skill_1",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_H,
            "category.brightmagic"
    ));

    private final static KeyBinding skill_2 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.brightmagic.skill_2",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_J,
            "category.brightmagic"
    ));

    private final static KeyBinding skill_3 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.brightmagic.skill_3",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            "category.brightmagic"
    ));

    private final static KeyBinding skill_4 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.brightmagic.skill_4",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_N,
            "category.brightmagic"
    ));

    public static void init() {
        ClientTickEvents.START_CLIENT_TICK.register((client) -> {
            if (client.world == null)
                return;
            if (openElements.isPressed()) {
                while (openElements.wasPressed()) {
                }
                ElementScreen screen = new ElementScreen(client.currentScreen);
                client.setScreen(screen);
            }
            if (skill_1.wasPressed()) {
                while (skill_1.wasPressed()) {
                }
                SkillC2SPacket.writeC2SUseSkillPacket(client.player, 1);
                SkillCooldownHelper.use(SkillData.getSlot((IDataHolder) client.player, 1));
            }
            if (skill_2.isPressed()) {
                while (skill_2.wasPressed()) {
                }
                SkillC2SPacket.writeC2SUseSkillPacket(client.player, 2);
                SkillCooldownHelper.use(SkillData.getSlot((IDataHolder) client.player, 2));
            }
            if (skill_3.isPressed()) {
                while (skill_3.wasPressed()) {
                }
                SkillC2SPacket.writeC2SUseSkillPacket(client.player, 3);
                SkillCooldownHelper.use(SkillData.getSlot((IDataHolder) client.player, 3));
            }
            if (skill_4.isPressed()) {
                while (skill_4.wasPressed()) {
                }
                SkillC2SPacket.writeC2SUseSkillPacket(client.player, 4);
                SkillCooldownHelper.use(SkillData.getSlot((IDataHolder) client.player, 4));
            }
        });
    }

}
