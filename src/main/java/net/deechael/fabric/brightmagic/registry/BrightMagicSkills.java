package net.deechael.fabric.brightmagic.registry;

import net.deechael.fabric.brightmagic.skill.Skill;
import net.deechael.fabric.brightmagic.skill.defaults.HealSkill;
import net.deechael.fabric.brightmagic.skill.defaults.ResonanceSkill;

public final class BrightMagicSkills {

    public final static Skill HEAL = register(new HealSkill());

    public final static Skill RESONANCE = register(new ResonanceSkill());

    public static void init() {
    }

    private static Skill register(Skill skill) {
        Skill.register(skill);
        return skill;
    }

}
