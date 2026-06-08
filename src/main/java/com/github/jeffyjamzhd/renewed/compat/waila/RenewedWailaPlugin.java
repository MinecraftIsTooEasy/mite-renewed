package com.github.jeffyjamzhd.renewed.compat.waila;

import com.github.jeffyjamzhd.renewed.block.BlockCrate;
import com.github.jeffyjamzhd.renewed.block.BlockRootingSugarCane;
import com.github.jeffyjamzhd.renewed.block.BlockSugarCane;
import com.github.jeffyjamzhd.renewed.block.entity.TileEntityCrate;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.*;

import java.util.List;

import static mcp.mobius.waila.api.SpecialChars.*;

public class RenewedWailaPlugin implements IWailaDataProvider {
    private static final RenewedWailaPlugin INSTANCE = new RenewedWailaPlugin();

    public static void register(IWailaRegistrar registrar) {
        registrar.registerStackProvider(INSTANCE, BlockSugarCane.class);
        registrar.registerStackProvider(INSTANCE, BlockRootingSugarCane.class);

        registrar.registerHeadProvider(INSTANCE, Block.class);

        registrar.registerBodyProvider(INSTANCE, BlockCrate.class);
        registrar.registerBodyProvider(INSTANCE, BlockSugarCane.class);
        registrar.registerBodyProvider(INSTANCE, BlockRootingSugarCane.class);
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        Block block = accessor.getBlock();
        if (block instanceof BlockRootingSugarCane || block instanceof BlockSugarCane) {
            return new ItemStack(Item.reed);
        }
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        Block block = accessor.getBlock();
        if (block instanceof BlockSugarCane cane) {
            World world = accessor.getWorld();
            RaycastCollision ray = accessor.getPosition();
            int x = ray.block_hit_x;
            int y = ray.block_hit_y;
            int z = ray.block_hit_z;

            boolean isRoot = cane.isRootCane(world, x, y, z);
            if (isRoot) {
                currentTip.set(0, WHITE + I18n.getString("tile.sugarCaneRoots.name"));
            }
        }

        return currentTip;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        Block block = accessor.getBlock();

        if (block instanceof BlockCrate) {
            TileEntityCrate te = (TileEntityCrate) accessor.getTileEntity();

            int storageCapacity = te.getStorageCapacity();
            int currentStorage = te.heldItemCount;
            String storage = I18n.getString("tooltip.waila.crate.storage");
            currentTip.add(storage.formatted(currentStorage, storageCapacity));

            ItemStack stack = te.getStackInSlot(0);
            String contains = I18n.getString("tooltip.waila.crate.contains");
            if (stack != null) {
                currentTip.add(contains.formatted(stack.getItem().getItemDisplayName(stack)));
            }
        }

        if (block instanceof BlockRootingSugarCane cane) {
            int meta = accessor.getMetadata();
            int growthStage = cane.getGrowthStage(meta);
            boolean hasGrown = cane.hasGrownToday(meta);

            float growthPercent = growthStage / 8F;
            growthStage = (int) (growthPercent * 100F);

            String growth = I18n.getString("tooltip.waila.sugarCane.stage");
            currentTip.add(growth.formatted(growthStage));

            String grown = I18n.getString("tooltip.waila.sugarCane." + (hasGrown ? "has" : "hasnt") + "Grown");
            currentTip.add(grown);
        }

        return currentTip;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currentTip;
    }

    @Override
    public NBTTagCompound getNBTData(ServerPlayer player, TileEntity te, NBTTagCompound tag, World world, int x, int y, int z) {
        return tag;
    }
}
