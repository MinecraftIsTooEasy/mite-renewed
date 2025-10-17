package com.github.jeffyjamzhd.renewed.item;

import com.github.jeffyjamzhd.renewed.block.BlockAbstractReed;
import com.github.jeffyjamzhd.renewed.block.BlockSugarCane;
import com.github.jeffyjamzhd.renewed.registry.RenewedBlocks;
import net.minecraft.*;

public class ItemSugarCane extends Item {
    public ItemSugarCane(int itemID, String texture) {
        super(itemID, Material.plants, texture);
    }

    @Override
    public boolean onItemRightClick(EntityPlayer player, float partial_tick, boolean ctrl_is_down) {
        RaycastCollision rc = player.getSelectedObject(partial_tick, false);
        if (rc == null || !rc.isBlock()) {
            return false;
        }
        if (player.worldObj.areSkillsEnabled()
                && !player.hasSkill(Skill.FARMING)) {
            return false;
        }
        return player.tryPlaceHeldItemAsBlock(rc, RenewedBlocks.rootingSugarCane);
    }

    @Override
    public boolean tryPlaceAsBlock(RaycastCollision rc, Block block, EntityPlayer player, ItemStack item_stack) {
        int x = rc.block_hit_x;
        int y = rc.block_hit_y;
        int z = rc.block_hit_z;
        if (rc.getBlockHit() instanceof BlockSugarCane) {
            if (RenewedBlocks.sugarCane.tryPlaceBlock(rc.world, x, ++y, z, EnumFace.TOP, 0, player, true, true)) {
                return true;
            }
        }

        x = rc.neighbor_block_x;
        y = rc.neighbor_block_y;
        z = rc.neighbor_block_z;
        if (rc.world.getBlock(x, y, z) != block) {
            return super.tryPlaceAsBlock(rc, block, player, item_stack);
        }
        if (block.isLegalAt(rc.world, rc.block_hit_x, rc.block_hit_y, rc.block_hit_z, 0)
                && block.canReplaceBlock(0, rc.getBlockHit(), rc.block_hit_metadata)) {
            return super.tryPlaceAsBlock(rc, block, player, item_stack);
        }
        if (RenewedBlocks.rootingSugarCane.tryPlaceBlock(rc.world, x, ++y, z, EnumFace.TOP, 0, player, true, true)) {
            return true;
        }

        return super.tryPlaceAsBlock(rc, block, player, item_stack);
    }
}
