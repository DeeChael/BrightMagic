package net.deechael.fabric.brightmagic.registry;

import net.deechael.fabric.brightmagic.skill.Skill;
import net.deechael.fabric.brightmagic.skill.defaults.HealSkill;
import net.deechael.fabric.brightmagic.skill.defaults.ResonanceSkill;

public final class BrightMagicSkills {

    public final static Skill HEAL;

    public final static Skill RESONANCE;

    static {
        HEAL = new HealSkill();
        RESONANCE = new ResonanceSkill();
    }

    public static void init() {
    }

}
