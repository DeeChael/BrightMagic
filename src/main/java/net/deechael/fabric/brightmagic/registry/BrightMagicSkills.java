package net.deechael.fabric.brightmagic.registry;

import net.deechael.fabric.brightmagic.skill.Skill;
import net.deechael.fabric.brightmagic.skill.defaults.HealSkill;
import net.deechael.fabric.brightmagic.skill.defaults.ResonanceSkill;

import java.util.ArrayList;
import java.util.List;

public final class BrightMagicSkills {

    private final static List<Skill> registered = new ArrayList<>();

    public final static Skill HEAL = register(new HealSkill());

    public final static Skill RESONANCE = register(new ResonanceSkill());

    public static void init() {
    }

    private static Skill register(Skill skill) {
        Skill.register(skill);
        registered.add(skill);
        return skill;
    }

    public static List<Skill> getAll() {
        return new ArrayList<>(registered);
    }

}
