package com.github.brachy84.wthitplusplus.renderer;

import com.github.brachy84.wthitplusplus.WthitPlusPlus;
import mcp.mobius.waila.Waila;
import mcp.mobius.waila.api.ICommonAccessor;
import mcp.mobius.waila.api.ITooltipRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.awt.*;

public class IconText implements ITooltipRenderer {

    public static final Identifier ID = WthitPlusPlus.id("icon_text");

    public static NbtCompound createTag(int width, int height, Identifier path, String text) {
        return createTag(width, height, path, 0, 0, 1, 1, text);
    }

    public static NbtCompound createTag(int width, int height, Identifier path, float u0, float v0, float u1, float v1, String text) {
        return createTag(width, height, path, u0, v0, u1, v1, text, 1f);
    }

    public static NbtCompound createTag(int width, int height, Identifier path, float u0, float v0, float u1, float v1, String text, float scale) {
        NbtCompound tag = new NbtCompound();
        tag.putInt("width", width);
        tag.putInt("height", height);
        tag.putFloat("u0", u0);
        tag.putFloat("v0", v0);
        tag.putFloat("u1", u1);
        tag.putFloat("v1", v1);
        tag.putString("path", path.toString());
        tag.putString("text", text);
        tag.putFloat("scale", scale);
        return tag;
    }

    @Override
    public Dimension getSize(NbtCompound tag, ICommonAccessor iCommonAccessor) {
        int width = tag.getInt("width");
        width += MinecraftClient.getInstance().textRenderer.getWidth(tag.getString("text"));
        float scale = 1;//tag.getFloat("scale");
        return new Dimension((int) (width * scale), (int) (tag.getInt("height") * scale));
    }

    @Override
    public void draw(MatrixStack matrixStack, NbtCompound tag, ICommonAccessor iCommonAccessor, int x, int y) {
        Identifier path = new Identifier(tag.getString("path"));
        int h = tag.getInt("height"), w = tag.getInt("width");
        int x1 = x + w, y1 = y + h;
        float u0 = tag.getFloat("u0"), v0 = tag.getFloat("v0"), u1 = tag.getFloat("u1"), v1 = tag.getFloat("v1");
        float scale = tag.getFloat("scale");
        int width = w + MinecraftClient.getInstance().textRenderer.getWidth(tag.getString("text"));

        Draw.texture(path, matrixStack, x, y, x1, y1, 100, u0, v0, u1, v1);
        int textY = (int) ( y + (h - (h / 2 + 3.5)));
        MinecraftClient.getInstance().textRenderer.drawWithShadow(matrixStack, tag.getString("text"), x + w + 2, textY, Waila.config.get().getOverlay().getColor().getFontColor());
    }
}
