package net.deechael.fabric.brightmagic.gui;

import net.deechael.fabric.brightmagic.element.Element;
import net.deechael.fabric.brightmagic.element.ElementType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ElementScreen extends Screen {

    private static void doNothing(ButtonWidget button) {}

    private final Screen parent;

    private TexturedButtonWidget pyro;
    private TexturedButtonWidget hydro;
    private TexturedButtonWidget electro;
    private TexturedButtonWidget cryo;
    private TexturedButtonWidget anemo;
    private TexturedButtonWidget geo;
    private TexturedButtonWidget hikari;
    private TexturedButtonWidget dark;

    private final List<TexturedButtonWidget> texturedButtonWidgets = new ArrayList<>();

    private ButtonWidget previousPage;
    private ButtonWidget nextPage;

    private int page = 1;

    public ElementScreen(Screen parent) {
        this(parent, Text.translatable("brightmagic.gui.element.title"));
    }

    public ElementScreen(Screen parent, Text title) {
        super(title);
        this.parent = parent;
    }

    @Override
    protected void init() {
        int left = (this.width - (16 * 7)) / 2;
        int top = (this.height - (16 * 7)) / 2;

        Element[] elements = Element.getAll();
        int pages = elements.length % 12 == 0 ? elements.length / 12 : elements.length / 12 + 1;
        if (this.page < 1)
            this.page = 1;
        if (this.page > pages)
            this.page = pages;
        for (int i = (page - 1) * 12; i < Math.min(elements.length, pages * 12); i++) {
            int j = i - (page - 1) * 12;
            int x = left + ((j % 4) * 32);
            int y = top + ((j / 4) * 32);
            this.texturedButtonWidgets.add(this.addDrawableChild(texture(elements[i], x, y)));
        }
        this.previousPage = this.addDrawableChild(new ButtonWidget(left, top + 96, 16, 16, Text.literal("<"), button -> {
            this.page -= 1;
            this.clearAndInit();
        }));
        this.nextPage = this.addDrawableChild(new ButtonWidget(left + 96, top + 96, 16, 16, Text.literal(">"), button -> {
            this.page += 1;
            this.clearAndInit();
        }));
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        this.init(client, width, height);
    }

    private TexturedButtonWidget texture(Element element, int x, int y) {
        return new TexturedButtonWidget(x, y, 16, 16, 0, 0, 16, element.getTexture(), 16, 16, ElementScreen::doNothing);
    }

    public void close() {
        this.client.setScreen(this.parent);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        for (TexturedButtonWidget texturedButtonWidget : this.texturedButtonWidgets) {
            texturedButtonWidget.render(matrices, mouseX, mouseY, delta);
        }
        this.previousPage.render(matrices, mouseX, mouseY, delta);
        this.nextPage.render(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
