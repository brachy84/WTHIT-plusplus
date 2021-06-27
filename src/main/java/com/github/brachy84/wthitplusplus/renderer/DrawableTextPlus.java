package com.github.brachy84.wthitplusplus.renderer;

import mcp.mobius.waila.api.IDrawableText;
import mcp.mobius.waila.overlay.DrawableText;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class DrawableTextPlus extends DrawableText {

    public static DrawableTextPlus of(Identifier id, NbtCompound tag) {
         return (DrawableTextPlus) new DrawableTextPlus().with(id, tag);
    }

    public DrawableTextPlus() {}

    private final List<Text> texts = new ArrayList<>();

    @Override
    public void render(MatrixStack matrixStack, int x, int y, float v) {
        super.render(matrixStack, x, y, v);
        int x0 = getSize().width + x + 2;
        for(Text text : texts) {
            x += MinecraftClient.getInstance().textRenderer.draw(matrixStack, text, x0, y, -1);
        }
    }

    @Override
    public MutableText append(Text text) {
        texts.add(text);
        return this;
    }

}
