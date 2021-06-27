package com.github.brachy84.wthitplusplus.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;

public class Draw {

    public static void texture(Identifier path, MatrixStack matrices, int x, int y, int x1, int y1, float z, float u0, float v0, float u1, float v1) {

        Matrix4f matrix4f = matrices.peek().getModel();

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
