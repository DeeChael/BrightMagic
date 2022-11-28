package net.deechael.fabric.brightmagic.skill;

import net.deechael.fabric.brightmagic.element.Element;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public abstract class Skill {

    private final Identifier identifier;
    private final Element element;
    private final int manaCost;
    private final Identifier texture;

    public Skill(Identifier identifier, Element element, int manaCost, Identifier texture) {
        this.identifier = identifier;
        this.element = element;
        this.manaCost = manaCost;
        this.texture = texture;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public String getTranslateKey() {
        Identifier identifier = this.getIdentifier();
        return "magic." + identifier.getNamespace() + "." + identifier.getPath();
    }

    public Element getElement() {
        return element;
    }

    public int getManaCost() {
        return manaCost;
    }

    public Identifier getTexture() {
        return texture;
    }

    public void use(PlayerEntity entity, World world, ItemStack wand) {

    }

    @Environment(EnvType.CLIENT)
    public void render(PlayerEntity entity, World world, ItemStack hand) {

    }

    public static void register(Skill skill) {
        SkillManager.skills.put(skill.getIdentifier(), skill);
    }

    public static Skill get(Identifier identifier) {
        return SkillManager.skills.get(identifier);
    }

}
