package net.deechael.fabric.brightmagic.util;

import net.deechael.fabric.brightmagic.element.Element;
import net.deechael.fabric.brightmagic.skill.Skill;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface ElementContainer {

    List<Element> owningElement();

    void addElement(Element element);

    void addElement(Element element, ItemStack itemStack, Skill skill);

    void removeElement(Element element);

    void updateElement(List<Element> elements);

}
