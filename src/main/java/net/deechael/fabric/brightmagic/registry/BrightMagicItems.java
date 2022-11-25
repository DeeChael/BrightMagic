package net.deechael.fabric.brightmagic.registry;

import net.deechael.fabric.brightmagic.Constants;
import net.deechael.fabric.brightmagic.basic.BasicWandItem;
import net.deechael.fabric.brightmagic.basic.ElementUnlockItem;
import net.deechael.fabric.brightmagic.element.Element;
import net.deechael.fabric.brightmagic.element.ElementType;
import net.deechael.fabric.brightmagic.skill.SkillRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class BrightMagicItems {

    // Limited Skill
    // None Element
    public final static Item HEALING_WAND = register("healing_wand", new BasicWandItem(new Item.Settings().maxCount(1).group(ItemGroup.COMBAT), new Element[0], SkillRegistry.HEAL, 10, 5, 0));

    // Hydro Element
    public final static Item RESONANCE_WAND = register("resonance_wand", new BasicWandItem(new Item.Settings().maxCount(1).group(ItemGroup.COMBAT), new Element[] {ElementType.HYDRO}, SkillRegistry.RESONANCE, 20, 10, 5));


    // Skill unlimited
    public final static Item FINAL_WAND = register("final_wand", new BasicWandItem(new Item.Settings().maxCount(1).group(ItemGroup.COMBAT), Element.getAll(), null, -1, -1, 35));


    // Items to unlock elements
    public final static Item FIRE_IN_ICE = register("fire_in_ice", new ElementUnlockItem(new Item.Settings().maxCount(1).group(ItemGroup.MATERIALS), ElementType.PYRO));
    public final static Item HEART_OF_WATER = register("heart_of_water", new ElementUnlockItem(new Item.Settings().maxCount(1).group(ItemGroup.MATERIALS), ElementType.HYDRO));

    public static void init() {}

    private static Item register(String id, Item entry) {
        return Registry.register(Registry.ITEM, new Identifier(Constants.MOD_ID, id), entry);
    }

}
