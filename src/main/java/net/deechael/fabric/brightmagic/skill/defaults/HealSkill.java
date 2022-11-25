package net.deechael.fabric.brightmagic.skill.defaults;

import net.deechael.fabric.brightmagic.Constants;
import net.deechael.fabric.brightmagic.element.Element;
import net.deechael.fabric.brightmagic.registry.client.BrightMagicTextures;
import net.deechael.fabric.brightmagic.skill.Skill;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class HealSkill extends Skill {

    public HealSkill() {
        super(new Identifier(Constants.MOD_ID, "none_heal"), null, 10, BrightMagicTextures.SKILL_NONE_HEAL);
    }

    @Override
    public String getTranslateKey() {
        Identifier identifier = this.getIdentifier();
        return "magic." + identifier.getNamespace() + "." + identifier.getPath();
    }

    @Override
    public void use(PlayerEntity entity, World world, ItemStack wand) {
        entity.heal(2f);
    }

}
