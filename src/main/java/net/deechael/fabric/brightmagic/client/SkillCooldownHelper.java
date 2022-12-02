package net.deechael.fabric.brightmagic.client;

import net.deechael.fabric.brightmagic.skill.Skill;

import java.util.HashMap;
import java.util.Map;

public class SkillCooldownHelper {

    private final static Map<Skill, Long> map = new HashMap<>();

    public static void use(Skill skill) {
        map.put(skill, System.currentTimeMillis());
    }

    public static int getCd(Skill skill) {
        if (!map.containsKey(skill))
            return 0;
        long current = System.currentTimeMillis();
        if (current - map.get(skill) >= skill.getCd() * 1000L)
            return 0;
        return (int) ((current - map.get(skill)) / 1000L);
    }

}
