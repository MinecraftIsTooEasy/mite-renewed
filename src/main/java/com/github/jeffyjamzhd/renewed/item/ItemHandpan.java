package com.github.jeffyjamzhd.renewed.item;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import net.minecraft.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemHandpan extends Item implements IDamageableItem {
    public static final int EMPTY = 0;
    public static final int SINEW = 1;
    public static final int SILK = 2;

    private Icon[] icons;
    private String[] suffix = new String[]{"empty", "sinew", "silk"};

    public ItemHandpan(int id) {
        super(id, Material.wood, "handpan");
        this.setCreativeTab(CreativeTabs.tabTools);
        this.setCraftingDifficultyAsComponent(200F);
        this.setMaxStackSize(1);
        this.setMaxDamage(160);
    }

    @Override
    public void registerIcons(IconRegister register) {
        this.icons = new Icon[3];
        for (int i = 0; i < 3; i++)
            icons[i] = register.registerIcon(this.getIconString() + "_" + suffix[i]);
    }

    @Override
    public void getSubItems(int id, CreativeTabs tab, List list) {
        for(int sub = 0; sub < 3; sub++)
            list.add(new ItemStack(id, 1, sub));
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        if (stack == null) {
            return super.getUnlocalizedName(stack);
        } else {
            int subType = stack.getItemSubtype();
            return super.getUnlocalizedName() + "." + suffix[subType];
        }
    }

    @Override
    public int getSimilarityToItem(Item item) {
        if (item instanceof ItemHandpan)
            return 2;
        return super.getSimilarityToItem(item);
    }

    @Override
    public boolean onItemRightClick(EntityPlayer player, float partial_tick, boolean ctrl_is_down) {
        ItemStack stack = player.getHeldItemStack();
        NBTTagCompound nbt = stack.getTagCompound();
        short store = nbt == null ? 0 : nbt.getShort("handpanContent");

        // Add block to sieve
        if (hasMesh(stack) && store == 0) {
            if (player.onClient()) {
                // Client process
                player.swingArm();
                return true;
            } else {
                // Server process
                ItemStack block = getBlockFromHotbar(player);
                if (block != null) {
                    stack.setTagInfo("handpanContent", new NBTTagShort("handpanContent", (short) block.itemID));
                    player.worldObj.playSoundAtEntity(player, MiTERenewed.RESOURCE_ID + "item.handpan.insert", 1F, 1F);
                    return true;
                }
                return false;
            }
        } else if (hasMesh(stack) && store != 0) {
            if (player.isInWater())
                return player.setHeldItemInUse();
        }
        return super.onItemRightClick(player, partial_tick, ctrl_is_down);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int use) {
        if (stack.hasTagCompound()) {
            // Get NBT and accumulate handpan progress
            stack.setTagInfo("handpanProgress", new NBTTagShort("handpanProgress", (short) use));
        }
    }

    @Override
    public void onItemUseFinish(ItemStack stack, World world, EntityPlayer player) {
        if (player.onServer()) {
            player.tryDamageHeldItem(DamageSource.generic, 1);
        }
        stack.setTagInfo("handpanContent", new NBTTagShort("handpanContent", (short) 0));
        stack.setTagInfo("handpanProgress", new NBTTagShort("handpanProgress", (short) 200));
    }

    public boolean hasMesh(ItemStack stack) {
        return stack.getItemSubtype() != EMPTY;
    }

    private ItemStack getBlockFromHotbar(EntityPlayer player) {
        // Get inventory
        InventoryPlayer inventory = player.inventory;
        int[] map = new int[]{-1,-1,-1};
        int selected = inventory.currentItem;

        // Iterate through hotbar
        for (int i = 0; i < 9; i++) {
            // Get item and ensure it's a block
            ItemStack at = inventory.getStackInSlot(i);
            if (at == null || !at.isBlock())
                continue;

            // Check id from the highest priority to least
            if (at.itemID == Block.gravel.blockID && map[0] == -1){
                map[0] = i;
                continue;
            }
            if (at.itemID == Block.dirt.blockID && map[1] == -1) {
                map[1] = i;
                continue;
            }
            if (at.itemID == Block.sand.blockID && map[2] == -1) {
                map[2] = i;
            }
        }

        // Iterate through map
        for (int i = 0; i < map.length; i++) {
            if (map[i] != -1)
                return inventory.decrStackSize(map[i], 1);
        }
        return null;
    }

    @Override
    public void addInformation(ItemStack item_stack, EntityPlayer player, List info, boolean extended_info, Slot slot) {
        if (extended_info) {
            if (item_stack.hasTagCompound()) {
                // Get info
                NBTTagCompound nbt = item_stack.getTagCompound();
                if (nbt.hasKey("handpanContent")) {
                    short blockID = nbt.getShort("handpanContent");
                    if (blockID != 0) {
                        Block block = Block.getBlock(blockID);
                        String translatedName = block.getLocalizedName();
                        info.add(EnumChatFormatting.DARK_GRAY
                                + Translator.getFormatted("tooltip.handpan.contains")
                                + " " + translatedName);
                        return;
                    }
                }
            }

            info.add(EnumChatFormatting.DARK_GRAY
                    + Translator.getFormatted("tooltip.handpan.contains")
                    + " " + Translator.getFormatted("tooltip.handpan.nothing"));
        }
    }

    @Override
    public Icon getIconFromSubtype(int sub) {
        return this.icons[sub];
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return getWeightedMaxUseDuration(stack);
    }

    public static int getWeightedMaxUseDuration(ItemStack stack) {
        if (stack.hasTagCompound()) {
            NBTTagCompound nbt = stack.getTagCompound();
            if (nbt.hasKey("handpanProgress"))
                return stack.getTagCompound().getShort("handpanProgress");
        }
        return 200;
    }

    public static int getTicksUsing(ItemStack stack, int use) {
        return stack.getMaxItemUseDuration() - use;
    }

    public static float getFractionUsing(ItemStack stack, int use) {
        float ticks = (float)getTicksUsing(stack, use);
        return Math.min(ticks / 200F, 1.0F);
    }

    @Override
    public EnumItemInUseAction getItemInUseAction(ItemStack par1ItemStack, EntityPlayer player) {
        return EnumItemInUseAction.BOW;
    }

    @Override
    public int getNumComponentsForDurability() {
        return 0;
    }

    @Override
    public int getRepairCost() {
        return 0;
    }

}
