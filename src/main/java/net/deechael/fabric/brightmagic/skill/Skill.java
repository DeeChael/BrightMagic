package net.deechael.fabric.brightmagic.skill;

import net.deechael.fabric.brightmagic.element.Element;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class Skill {

    private final Identifier identifier;
    private final Element element;
    private final int manaCost;
    private final Identifier texture;

    public Skill(Identifier identifier, @Nullable Element element, int manaCost, Identifier texture) {
        this.identifier = identifier;
        this.element = element;
        this.manaCost = manaCost;
        this.texture = texture;
    }

    public Identifier getId() {
        return identifier;
    }

    public String getTranslateKey() {
        Identifier identifier = this.getId();
        return "magic." + identifier.getNamespace() + "." + identifier.getPath();
    }

    public int getCd() {
        return 10;
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

    public void use(LivingEntity entity, World world, ItemStack wand) {

    }

    @Environment(EnvType.CLIENT)
    public void render(PlayerEntity entity, World world, ItemStack hand) {

    }

    public static void register(Skill skill) {
        SkillManager.skills.put(skill.getId(), skill);
        Identifier element = skill.getElement() != null ? skill.getElement().getId() : null;
        if (!SkillManager.elementSkillMap.containsKey(element))
            SkillManager.elementSkillMap.put(element, new ArrayList<>());
        SkillManager.elementSkillMap.get(element).add(skill.getId());
    }

    public static List<Skill> getSkills(@Nullable Element element) {
        return SkillManager.elementSkillMap.get(element == null ? null : element.getId()).stream().map(Skill::get).toList();
    }

    public static Skill get(Identifier identifier) {
        return SkillManager.skills.get(identifier);
    }

}
