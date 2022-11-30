package net.deechael.fabric.brightmagic.skill;

import net.deechael.fabric.brightmagic.util.IDataHolder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SkillData {

    public static Skill getSlot(IDataHolder dataHolder, int slot) {
        NbtCompound skillData = dataHolder.getSkillData();
        String id = skillData.getString("slot_" + slot);
        if (Objects.equals(id, "null"))
            return null;
        return Skill.get(new Identifier(id));
    }

    public static void setSlot(IDataHolder dataHolder, int slot, Skill skill) {
        if (!(slot <= 4 && slot >= 1))
            return;
        NbtCompound skillData = dataHolder.getSkillData();
        skillData.putString("slot_" + slot, skill == null ? "null" : skill.getId().toString());
    }

    public static Skill[] getSlots(IDataHolder dataHolder) {
        Skill[] slots = new Skill[4];
        NbtCompound skillData = dataHolder.getSkillData();
        slots[0] = Objects.equals(skillData.getString("slot_1"), "null") ? null : Skill.get(new Identifier(skillData.getString("slot_1")));
        slots[1] = Objects.equals(skillData.getString("slot_2"), "null") ? null : Skill.get(new Identifier(skillData.getString("slot_2")));
        slots[2] = Objects.equals(skillData.getString("slot_3"), "null") ? null : Skill.get(new Identifier(skillData.getString("slot_3")));
        slots[3] = Objects.equals(skillData.getString("slot_4"), "null") ? null : Skill.get(new Identifier(skillData.getString("slot_4")));
        return slots;
    }
    
    public static Skill[] getUnlockedSkills(IDataHolder dataHolder) {
        NbtCompound skillData = dataHolder.getSkillData();
        int unlocked = skillData.getInt("unlocked");
        Skill[] types = new Skill[unlocked];
        NbtList skills = skillData.getList("skills", NbtElement.STRING_TYPE);
        for (int i = 0; i < unlocked; i++) {
            types[i] = Skill.get(new Identifier(skills.get(i).asString()));
        }
        return types;
    }

    public static int getUnlocked(IDataHolder dataHolder) {
        NbtCompound elementData = dataHolder.getSkillData();
        return elementData.getInt("unlocked");
    }

    public static int setUnlocked(IDataHolder dataHolder, Skill... skills) {
        return setUnlocked(dataHolder, List.of(skills));
    }

    public static boolean isUnlocked(IDataHolder dataHolder, Skill skill) {
        return List.of(getUnlockedSkills(dataHolder)).contains(skill);
    }

    public static int setUnlocked(IDataHolder dataHolder, Iterable<Skill> skills) {
        NbtCompound skillData = dataHolder.getSkillData();
        NbtList list = new NbtList();
        for (Skill skill : skills) {
            list.add(NbtString.of(skill.getId().toString()));
        }
        skillData.put("skills", list);
        skillData.putInt("unlocked", list.size());
        return list.size();
    }

    public static boolean hasUnlocked(IDataHolder dataHolder, Skill skill) {
        List<Skill> skills = List.of(getUnlockedSkills(dataHolder));
        return skills.contains(skill);
    }

    public static void addUnlocked(IDataHolder dataHolder, Skill skill) {
        if (hasUnlocked(dataHolder, skill) || getUnlocked(dataHolder) >= 25)
            return;
        NbtCompound skillData = dataHolder.getSkillData();
        List<Skill> skills = new ArrayList<>(List.of(getUnlockedSkills(dataHolder)));
        skills.add(skill);
        skillData.putInt("unlocked", getUnlocked(dataHolder) + 1);
        NbtList list = new NbtList();
        for (Skill s : skills) {
            list.add(NbtString.of(s.getId().toString()));
        }
        skillData.put("skills", list);
    }

    public static void removeUnlocked(IDataHolder dataHolder, Skill skill) {
        if (!hasUnlocked(dataHolder, skill))
            return;
        NbtCompound skillData = dataHolder.getSkillData();
        List<Skill> skills = new ArrayList<>(List.of(getUnlockedSkills(dataHolder)));
        skills.remove(skill);
        skillData.putInt("unlocked", getUnlocked(dataHolder) - 1);
        NbtList list = new NbtList();
        for (Skill s : skills) {
            list.add(NbtString.of(s.getId().toString()));
        }
        skillData.put("skills", list);
    }

    public static void clearUnlocked(IDataHolder dataHolder) {
        NbtCompound skillData = dataHolder.getSkillData();
        skillData.putInt("unlocked", 0);
        skillData.put("skills", new NbtList());
    }
    
}
