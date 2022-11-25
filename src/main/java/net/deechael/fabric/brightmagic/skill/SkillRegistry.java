package net.deechael.fabric.brightmagic.skill;

import net.deechael.fabric.brightmagic.skill.defaults.HealSkill;
import net.deechael.fabric.brightmagic.skill.defaults.ResonanceSkill;

public final class SkillRegistry {

    public final static Skill HEAL;

    public final static Skill RESONANCE;

    static {
        HEAL = new HealSkill();
        RESONANCE = new ResonanceSkill();
    }

}
