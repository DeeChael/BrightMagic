package net.deechael.fabric.brightmagic.registry;

import net.deechael.fabric.brightmagic.element.Element;
import net.deechael.fabric.brightmagic.element.reaction.ElementReaction;
import net.deechael.fabric.brightmagic.element.reaction.defaults.VaporizeReaction;

public class BrightMagicElementReactions {

    public final static ElementReaction VAPORIZE = new VaporizeReaction();

    public static void init() {
        Element.register(VAPORIZE);
    }

}
