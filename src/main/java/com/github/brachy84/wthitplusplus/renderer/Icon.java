package com.github.brachy84.wthitplusplus.renderer;

import com.github.brachy84.wthitplusplus.WthitPlusPlus;
import com.mojang.blaze3d.systems.RenderSystem;
import mcp.mobius.waila.api.ICommonAccessor;
import mcp.mobius.waila.api.IDrawableText;
import mcp.mobius.waila.api.ITooltipRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;

import java.awt.*;

public class Icon implements ITooltipRenderer {

    public static final Identifier ID = WthitPlusPlus.id("sprite_renderer");

    public static DrawableTextPlus createText(int width, int height, Identifier path) {
        return DrawableTextPlus.of(ID, createTag(width, height, path));
    }

    public static DrawableTextPlus createText(int width, int height, Identifier path, float u0, float v0, float u1, float v1) {
        return DrawableTextPlus.of(ID, createTag(width, height, path, u0, v0, u1, v1));
    }

    public static NbtCompound createTag(int width, int height, Identifier path) {
        return createTag(width, height, path, 0, 0, 1, 1);
    }

    public static NbtCompound createTag(int width, int height, Identifier path, float u0, float v0, float u1, float v1) {
        NbtCompound tag = new NbtCompound();
        tag.putInt("width", width);
        tag.putInt("height", height);
        tag.putFloat("u0", u0);
        tag.putFloat("v0", v0);
        tag.putFloat("u1", u1);
        tag.putFloat("v1", v1);
        tag.putString("path", path.toString());
        return tag;
    }

    @Override
    public Dimension getSize(NbtCompound tag, ICommonAccessor iCommonAccessor) {
        return new Dimension(tag.getInt("width"), tag.getInt("height"));
    }

    @Override
    public void draw(MatrixStack matrixStack, NbtCompound tag, ICommonAccessor iCommonAccessor, int x, int y) {
        Identifier path = new Identifier(tag.getString("path"));
        int h = tag.getInt("height"), w = tag.getInt("width");
        int x1 = x + w, y1 = y + h;
        float u0 = tag.getFloat("u0"), v0 = tag.getFloat("v0"), u1 = tag.getFloat("u1"), v1 = tag.getFloat("v1");

        Matrix4f matrix4f = matrixStack.peek().getModel();

        float z = 100f;

        RenderSystem.setShaderTexture(0, path);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(matrix4f, x, y1, z).texture(u0, v1).next();
        bufferBuilder.vertex(matrix4f, x1, y1, z).texture(u1, v1).next();
        bufferBuilder.vertex(matrix4f, x1, y, z).texture(u1, v0).next();
        bufferBuilder.vertex(matrix4f, x, y, z).texture(u0, v0).next();
        bufferBuilder.end();
        BufferRenderer.draw(bufferBuilder);
    }
}
