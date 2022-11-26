package net.deechael.fabric.brightmagic.element;

import net.deechael.fabric.brightmagic.element.reaction.ElementReaction;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public interface Element {

    Identifier getId();

    Identifier getTexture();

    Formatting getColor();

    static Element[] getAll() {
        return ElementManager.elements.values().toArray(new Element[0]);
    }

    static void register(Element element) {
        ElementManager.elements.put(element.getId(), element);
        if (!ElementManager.reactionRelations.containsKey(element.getId()))
            ElementManager.reactionRelations.put(element.getId(), new ArrayList<>());
    }

    static Element get(Identifier identifier) {
        return ElementManager.elements.get(identifier);
    }

    static void register(ElementReaction reaction) {
        if (!ElementManager.reactionRelations.containsKey(reaction.getFirst().getId()))
            ElementManager.reactionRelations.put(reaction.getFirst().getId(), new ArrayList<>());
        if (!ElementManager.reactionRelations.containsKey(reaction.getSecond().getId()))
            ElementManager.reactionRelations.put(reaction.getSecond().getId(), new ArrayList<>());
        ElementManager.reactions.put(reaction.getId(), reaction);
        ElementManager.reactionRelations.get(reaction.getFirst().getId()).add(reaction.getId());
        ElementManager.reactionRelations.get(reaction.getSecond().getId()).add(reaction.getId());
    }

    static ElementReaction getReaction(Identifier identifier) {
        return ElementManager.reactions.get(identifier);
    }

    static List<ElementReaction> getAllReactions() {
        return new ArrayList<>(ElementManager.reactions.values());
    }

    static List<ElementReaction> availableReactions(Element element) {
        if (!ElementManager.reactionRelations.containsKey(element.getId()))
            ElementManager.reactionRelations.put(element.getId(), new ArrayList<>());
        return new ArrayList<>(ElementManager.reactionRelations.get(element.getId())).stream().map(Element::getReaction).toList();
    }

}
