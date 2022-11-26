package net.deechael.fabric.brightmagic.element;

import net.deechael.fabric.brightmagic.Constants;
import net.deechael.fabric.brightmagic.registry.client.BrightMagicTextures;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public enum ElementType implements Element {

    // Below comes from Genshin Impact
    PYRO(Formatting.RED, BrightMagicTextures.ELEMENT_PYRO), // 火
    HYDRO(Formatting.BLUE, BrightMagicTextures.ELEMENT_HYDRO), // 水
    CRYO(Formatting.AQUA, BrightMagicTextures.ELEMENT_CRYO), // 冰
    ELECTRO(Formatting.LIGHT_PURPLE, BrightMagicTextures.ELEMENT_ELECTRO), // 雷
    GEO(Formatting.GOLD, BrightMagicTextures.ELEMENT_GEO), // 土
    ANEMO(Formatting.GREEN, BrightMagicTextures.ELEMENT_ANEMO), // 风

    // Below is added by me
    HIKARI(Formatting.WHITE, BrightMagicTextures.ELEMENT_HIKARI), // 光
    DARK(Formatting.BLACK, BrightMagicTextures.ELEMENT_DARK); // 暗

    private final Formatting color;
    private final Identifier texture;

    ElementType(Formatting color, Identifier texture) {
        this.color = color;
        this.texture = texture;
    }

    @Override
    public Identifier getId() {
        return new Identifier(Constants.MOD_ID, this.name().toLowerCase());
    }

    public Identifier getTexture() {
        return texture;
    }

    public Formatting getColor() {
        return color;
    }

    public static void init() {
        for (ElementType elementType : values()) {
            Element.register(elementType);
        }
    }

}
