package com.github.jeffyjamzhd.renewed.api;

import com.github.jeffyjamzhd.renewed.util.RenderUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.*;

public interface IBlock {
    /**
     * {@code true} if this block uses the extended interaction API
     */
    default boolean mr$useExtendedAPI() { return false; }

    /**
     * Extended block hardness check.
     */
    default float mr$getBlockHardness(World world, int x, int y, int z, int metadata) {
        return 0F;
    }

    /**
     * Extended block clicked hook
     * @return true if interaction should be sent to server
     */
    default boolean mr$onBlockClicked(World world, EnumFace face, int x, int y, int z, EntityPlayer player) { return false; }

    /**
     * {@code true} if this block should use more granular rendering
     * (additional icon checks)
     */
    @Environment(EnvType.CLIENT)
    default boolean mr$useSpecialCrossedRenderer() {
        return false;
    }

    /**
     * If true, uses the new render block methods instead of the original hardcoded list
     */
    @Environment(EnvType.CLIENT)
    default boolean mr$useBlockRenderAPI() { return false; }

    /**
     * Renders a block
     */
    @Environment(EnvType.CLIENT)
    default boolean mr$renderBlock(RenderBlocks renderer, IBlockAccess accessor, int x, int y, int z) {
        return renderer.renderStandardBlock((Block) this, x, y, z);
    }

    /**
     * Called when the block renderer has successfully rendered the first pass of a block.
     * The second pass allows you to render overlays or whatever else.
     */
    @Environment(EnvType.CLIENT)
    default void mr$renderBlockSecondPass(RenderBlocks renderer, IBlockAccess accessor, int x, int y, int z) {
    }

    /**
     * Called when block renderer is rendering the block breaking overlay
     */
    @Environment(EnvType.CLIENT)
    default void mr$renderBlockBreaking(RenderBlocks renderer, IBlockAccess accessor, int x, int y, int z) {
        renderer.renderStandardBlock((Block) this, x, y, z);
    }

    @Environment(EnvType.CLIENT)
    default void mr$renderBlockAsItem(RenderBlocks renderer, int metadata, float color) {
        RenderUtils.renderStandardBlockAsItemWithDefaultBounds(renderer, (Block) this, metadata);
    }
}
