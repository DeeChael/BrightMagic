package net.deechael.fabric.brightmagic.registry;

import net.deechael.fabric.brightmagic.element.Element;
import net.deechael.fabric.brightmagic.element.reaction.ElementReaction;
import net.deechael.fabric.brightmagic.element.reaction.defaults.FrozenReaction;
import net.deechael.fabric.brightmagic.element.reaction.defaults.MeltReaction;
import net.deechael.fabric.brightmagic.element.reaction.defaults.OverloadedReaction;
import net.deechael.fabric.brightmagic.element.reaction.defaults.VaporizeReaction;

public class BrightMagicElementReactions {

    public final static ElementReaction VAPORIZE = new VaporizeReaction();
    public final static ElementReaction OVERLOADED = new OverloadedReaction();
    public final static ElementReaction MELT = new MeltReaction();
    public final static ElementReaction FROZEN = new FrozenReaction();

    public static void init() {
        Element.register(VAPORIZE);
        Element.register(OVERLOADED);
        Element.register(MELT);
        Element.register(FROZEN);
    }

}
