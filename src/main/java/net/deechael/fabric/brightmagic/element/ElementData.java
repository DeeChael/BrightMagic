package net.deechael.fabric.brightmagic.element;

import net.deechael.fabric.brightmagic.util.IDataHolder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ElementData {

    public static Element[] getUnlockedElements(IDataHolder dataHolder) {
        NbtCompound elementData = dataHolder.getElementData();
        int unlocked = elementData.getInt("unlocked");
        Element[] types = new Element[unlocked];
        NbtList elements = elementData.getList("unlockedElements", NbtElement.STRING_TYPE);
        for (int i = 0; i < unlocked; i++) {
            types[i] = Element.get(new Identifier(elements.get(i).asString()));
        }
        return types;
    }

    public static int getUnlocked(IDataHolder dataHolder) {
        NbtCompound elementData = dataHolder.getElementData();
        return elementData.getInt("unlocked");
    }

    public static int setUnlocked(IDataHolder dataHolder, Element... elements) {
        return setUnlocked(dataHolder, List.of(elements));
    }

    public static boolean isUnlocked(IDataHolder dataHolder, Element element) {
        return List.of(getUnlockedElements(dataHolder)).contains(element);
    }

    public static int setUnlocked(IDataHolder dataHolder, Iterable<Element> elementTypes) {
        NbtCompound elementData = dataHolder.getElementData();
        NbtList list = new NbtList();
        for (Element element : elementTypes) {
            list.add(NbtString.of(element.getId().toString()));
        }
        elementData.put("unlockedElements", list);
        elementData.putInt("unlocked", list.size());
        return list.size();
    }

    public static boolean hasUnlocked(IDataHolder dataHolder, Element element) {
        List<Element> elements = List.of(getUnlockedElements(dataHolder));
        return elements.contains(element);
    }

    public static void addUnlocked(IDataHolder dataHolder, Element element) {
        if (hasUnlocked(dataHolder, element) || getUnlocked(dataHolder) >= 4)
            return;
        NbtCompound elementData = dataHolder.getElementData();
        List<Element> elements = new ArrayList<>(List.of(getUnlockedElements(dataHolder)));
        elements.add(element);
        elementData.putInt("unlocked", getUnlocked(dataHolder) + 1);
        NbtList list = new NbtList();
        for (Element e : elements) {
            list.add(NbtString.of(e.getId().toString()));
        }
        elementData.put("unlockedElements", list);
    }

    public static void removeUnlocked(IDataHolder dataHolder, Element element) {
        if (!hasUnlocked(dataHolder, element))
            return;
        NbtCompound elementData = dataHolder.getElementData();
        List<Element> elements = new ArrayList<>(List.of(getUnlockedElements(dataHolder)));
        elements.remove(element);
        elementData.putInt("unlocked", getUnlocked(dataHolder) - 1);
        NbtList list = new NbtList();
        for (Element e : elements) {
            list.add(NbtString.of(e.getId().toString()));
        }
        elementData.put("unlockedElements", list);
    }

    public static void clearUnlocked(IDataHolder dataHolder) {
        NbtCompound elementData = dataHolder.getElementData();
        elementData.putInt("unlocked", 0);
        elementData.put("unlockedElements", new NbtList());
    }

}
