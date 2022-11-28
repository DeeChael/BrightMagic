package net.deechael.fabric.brightmagic.registry;

import net.deechael.fabric.brightmagic.element.Element;
import net.deechael.fabric.brightmagic.element.reaction.ElementReaction;
import net.deechael.fabric.brightmagic.element.reaction.defaults.*;

public class BrightMagicElementReactions {

    public final static ElementReaction VAPORIZE = new VaporizeReaction();
    public final static ElementReaction OVERLOADED = new OverloadedReaction();
    public final static ElementReaction MELT = new MeltReaction();
    public final static ElementReaction FROZEN = new FrozenReaction();
    public final static ElementReaction SUPER_CONDUCT = new SuperConductReaction();
    public final static ElementReaction ELECTRO_CHARGED = new ElectroChargedReaction();

    public static void init() {
        Element.register(VAPORIZE);
        Element.register(OVERLOADED);
        Element.register(MELT);
        Element.register(FROZEN);
        Element.register(SUPER_CONDUCT);
        Element.register(ELECTRO_CHARGED);
    }

}
