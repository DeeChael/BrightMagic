package net.deechael.fabric.brightmagic.util;

import net.minecraft.nbt.NbtCompound;

public interface IDataHolder {

    NbtCompound getManaData();

    NbtCompound getElementData();

    NbtCompound getSkillData();

}
