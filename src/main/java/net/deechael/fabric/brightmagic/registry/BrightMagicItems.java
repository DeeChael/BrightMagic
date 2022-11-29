package net.deechael.fabric.brightmagic.registry;

import net.deechael.fabric.brightmagic.Constants;
import net.deechael.fabric.brightmagic.basic.BasicWandItem;
import net.deechael.fabric.brightmagic.basic.ElementUnlockItem;
import net.deechael.fabric.brightmagic.basic.SkillScrollItem;
import net.deechael.fabric.brightmagic.element.Element;
import net.deechael.fabric.brightmagic.element.ElementType;
import net.deechael.fabric.brightmagic.skill.Skill;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

public final class BrightMagicItems {

    // Limited Skill
    // None Element
    public final static Item HEALING_WAND = register("healing_wand", new BasicWandItem(new Item.Settings().maxCount(1).group(ItemGroup.COMBAT), new Element[0], BrightMagicSkills.HEAL, 10, 5, 0));

    // Hydro Element
    public final static Item RESONANCE_WAND = register("resonance_wand", new BasicWandItem(new Item.Settings().maxCount(1).group(ItemGroup.COMBAT), new Element[] {ElementType.HYDRO}, BrightMagicSkills.RESONANCE, 20, 10, 5));


    // Skill unlimited
    public final static Item FINAL_WAND = register("final_wand", new BasicWandItem(new Item.Settings().maxCount(1).group(ItemGroup.COMBAT), Element.getAll(), null, -1, -1, 35));


    // Items to unlock elements
    public final static Item FIRE_IN_ICE = register("fire_in_ice", new ElementUnlockItem(new Item.Settings().maxCount(1).group(ItemGroup.MATERIALS), ElementType.PYRO));
    public final static Item HEART_OF_WATER = register("heart_of_water", new ElementUnlockItem(new Item.Settings().maxCount(1).group(ItemGroup.MATERIALS), ElementType.HYDRO));
    public final static Item ELECTRO_SHARP = register("electro_sharp", new ElementUnlockItem(new Item.Settings().maxCount(1).group(ItemGroup.MATERIALS), ElementType.ELECTRO));
    public final static Item ICY_SWORD = register("icy_sword", new ElementUnlockItem(new Item.Settings().maxCount(1).group(ItemGroup.MATERIALS), ElementType.CRYO));

    public final static Item SKILL_SCROLL = register("skill_scroll", new SkillScrollItem(new Item.Settings().maxCount(1)));

    public static void init() {
        DefaultedList<ItemStack> list = DefaultedList.of();
        for (Skill skill : BrightMagicSkills.getAll())
            list.add(createStackWithSkill(skill.getId().toString()));
        ItemGroup.COMBAT.appendStacks(list);
    }

    private static ItemStack createStackWithSkill(String skill) {
        ItemStack itemStack = new ItemStack(SKILL_SCROLL);
        NbtCompound nbtCompound = new NbtCompound();
        NbtCompound brightMagic = new NbtCompound();
        brightMagic.putString("item-type", "skill_scroll");
        brightMagic.putString("skill", skill);
        nbtCompound.put("brightmagic", brightMagic);
        itemStack.setNbt(nbtCompound);
        return itemStack;
    }

    private static Item register(String id, Item entry) {
        return Registry.register(Registry.ITEM, new Identifier(Constants.MOD_ID, id), entry);
    }

}
