package net.deechael.fabric.brightmagic.mana;

import net.deechael.fabric.brightmagic.util.IDataHolder;
import net.minecraft.nbt.NbtCompound;

public class ManaData {

    public static int addMana(IDataHolder dataHolder, int mana) {
        NbtCompound manaData = dataHolder.getManaData();
        int currentMana = manaData.getInt("mana");
        int maxMana = manaData.getInt("maxMana");
        manaData.putInt("mana", currentMana = Math.min(currentMana + mana, maxMana));
        return currentMana;
    }

    public static int removeMana(IDataHolder dataHolder, int mana) {
        NbtCompound manaData = dataHolder.getManaData();
        int currentMana = manaData.getInt("mana");
        manaData.putInt("mana", currentMana = Math.max(currentMana - mana, 0));
        return currentMana;
    }

    public static int setMana(IDataHolder dataHolder, int mana) {
        NbtCompound manaData = dataHolder.getManaData();
        int maxMana = manaData.getInt("maxMana");
        manaData.putInt("mana", maxMana = Math.min(Math.max(mana, 0), maxMana));
        return maxMana;
    }

    public static int setMaxMana(IDataHolder dataHolder, int newMaxMana) {
        NbtCompound manaData = dataHolder.getManaData();
        int maxMana;
        manaData.putInt("maxMana", maxMana = Math.min(Math.max(newMaxMana, 0), 1000));
        return maxMana;
    }

    public static int getMana(IDataHolder dataHolder) {
        NbtCompound manaData = dataHolder.getManaData();
        return manaData.getInt("mana");
    }

    public static int getMaxMana(IDataHolder dataHolder) {
        NbtCompound manaData = dataHolder.getManaData();
        return manaData.getInt("maxMana");
    }

}
