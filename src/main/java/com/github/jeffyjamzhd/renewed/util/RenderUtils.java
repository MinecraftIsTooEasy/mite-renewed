package com.github.jeffyjamzhd.renewed.util;

import net.minecraft.Block;
import net.minecraft.RenderBlocks;
import net.minecraft.Tessellator;

public class RenderUtils {
    public static void renderStandardBlockAsItemWithDefaultBounds(RenderBlocks renderer, Block block, int metadata) {
        block.setBlockBoundsForItemRender(metadata);
        if (block.isAlwaysStandardFormCube()) {
            renderer.setRenderBoundsForStandardFormBlock();
        } else {
            renderer.setRenderBoundsForNonStandardFormBlock(block);
        }

        renderStandardBlockAsItem(renderer, block, metadata);
    }

    public static void renderStandardBlockAsItem(RenderBlocks renderer, Block block, int metadata) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setNormal(0F, -1F, 0F);
        renderer.renderFaceYNeg(block, 0F, 0F, 0F, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0F, 1F, 0F);
        renderer.renderFaceYPos(block, 0F, 0F, 0F, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0F, 0F, -1F);
        renderer.renderFaceZNeg(block, 0F, 0F, 0F, renderer.getBlockIconFromSideAndMetadata(block, 2, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0F, 0F, 1F);
        renderer.renderFaceZPos(block, 0F, 0F, 0F, renderer.getBlockIconFromSideAndMetadata(block, 3, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1F, 0F, 0F);
        renderer.renderFaceXNeg(block, 0F, 0F, 0F, renderer.getBlockIconFromSideAndMetadata(block, 4, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1F, 0F, 0F);
        renderer.renderFaceXPos(block, 0F, 0F, 0F, renderer.getBlockIconFromSideAndMetadata(block, 5, metadata));
        tessellator.draw();
    }
}
