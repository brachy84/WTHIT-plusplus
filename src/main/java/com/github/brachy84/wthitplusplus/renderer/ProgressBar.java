package com.github.brachy84.wthitplusplus.renderer;

import com.github.brachy84.wthitplusplus.WthitPlusPlus;
import mcp.mobius.waila.api.ICommonAccessor;
import mcp.mobius.waila.api.ITooltipRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.awt.*;

public class ProgressBar implements ITooltipRenderer {

    public static final Identifier ID = WthitPlusPlus.id("progressbar");

    public static Color outline = Color.of(200, 200, 200, 150);
    public static Color filling = Color.of(100, 100, 100, 0);

    public static DrawableTextPlus createText(float progress, int width, int height, int color) {
        return DrawableTextPlus.of(ID, createTag(progress, width, height, color));
    }

    public static NbtCompound createTag(float progress, int width, int height, int color) {
        NbtCompound tag = new NbtCompound();
        tag.putFloat("progress", progress);
        tag.putInt("width", width);
        tag.putInt("height", height);
        tag.putInt("color", color);
        return tag;
    }

    @Override
    public Dimension getSize(NbtCompound tag, ICommonAccessor iCommonAccessor) {
        return new Dimension(tag.getInt("width"), tag.getInt("height") + 2);
    }

    @Override
    public void draw(MatrixStack matrices, NbtCompound tag, ICommonAccessor iCommonAccessor, int x, int y) {
        float progress = tag.getFloat("progress");
        int h = tag.getInt("height"), w = tag.getInt("width");
        int color = tag.getInt("color");
        // outline
        // top
        DrawableHelper.fill(matrices, x, y, x + w, y + 1, outline.asInt());
        // bottom
        DrawableHelper.fill(matrices, x, y + h - 1, x + w, y + h, outline.asInt());
        // left
        DrawableHelper.fill(matrices, x, y + 1, x + 1, y + h - 1, outline.asInt());
        // right
        DrawableHelper.fill(matrices, x + w - 1, y + 1, x + w, y + h - 1, outline.asInt());

        // filling
        //DrawableHelper.fill(matrices, x + 1, y + 1, x + w - 1, y + h - 1, filling.asInt());

        DrawableHelper.fill(matrices, x + 1, y + 1, (int) (x + 1 + (progress * (w - 2))), y + h - 1, color);
        int percent = (int) (progress * 100 + 0.5);
        matrices.push();
        matrices.scale(0.7f, 0.7f, 0);
        matrices.translate(160.5, 21.5, 0);
        MinecraftClient.getInstance().textRenderer.draw(matrices, percent + "%", x + 4, y + 2, Color.of(1f, 1f, 1f).asInt());
        matrices.pop();
    }
}
