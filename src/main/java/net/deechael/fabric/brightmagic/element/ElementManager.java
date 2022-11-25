package net.deechael.fabric.brightmagic.element;

import net.deechael.fabric.brightmagic.element.reaction.ElementReaction;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ElementManager {

    final static Map<Identifier, Element> elements = new HashMap<>();
    final static Map<Identifier, ElementReaction> reactions = new HashMap<>();
    final static Map<Identifier, List<Identifier>> reactionRelations = new HashMap<>();

}
