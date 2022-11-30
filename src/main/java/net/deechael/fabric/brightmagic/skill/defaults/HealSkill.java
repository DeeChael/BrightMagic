package net.deechael.fabric.brightmagic.skill.defaults;

import net.deechael.fabric.brightmagic.Constants;
import net.deechael.fabric.brightmagic.registry.client.BrightMagicTextures;
import net.deechael.fabric.brightmagic.skill.Skill;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class HealSkill extends Skill {

    public HealSkill() {
        super(new Identifier(Constants.MOD_ID, "none_heal"), null, 10, BrightMagicTextures.SKILL_NONE_HEAL);
    }

    @Override
    public String getTranslateKey() {
        Identifier identifier = this.getId();
        return "magic." + identifier.getNamespace() + "." + identifier.getPath();
    }

    @Override
    public int getCd() {
        return 5;
    }

    @Override
    public void use(LivingEntity entity, World world, ItemStack wand) {
        entity.heal(2f);
    }

}
