package com.github.jeffyjamzhd.renewed.item;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import com.github.jeffyjamzhd.renewed.item.recipe.HandpanRecipe;
import com.github.jeffyjamzhd.renewed.item.recipe.HandpanRecipeProcessor;
import net.minecraft.*;

import java.util.List;

public class ItemHandpan extends Item implements IDamageableItem {
    public static final int EMPTY = 0;
    public static final int SINEW = 1;
    public static final int SILK = 2;

    public static final String
            NBT_CONTENT = "handpanContent",
            NBT_DAMAGE = "handpanIncomingDamage",
            NBT_PROGRESS = "handpanProgress",
            NBT_SPEED = "handpanSpeed";

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
        short store = nbt == null ? 0 : nbt.getShort(NBT_CONTENT);

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
                    HandpanRecipe recipe = HandpanRecipeProcessor.getRecipe(block.itemID, stack.getItemSubtype());
                    int use = recipe.getSpeed();
                    int damage = recipe.getDamage();

                    stack.setTagInfo(NBT_CONTENT, new NBTTagShort(NBT_CONTENT, (short) block.itemID));
                    stack.setTagInfo(NBT_PROGRESS, new NBTTagShort(NBT_PROGRESS, (short) use));
                    stack.setTagInfo(NBT_SPEED, new NBTTagShort(NBT_SPEED, (short) use));
                    stack.setTagInfo(NBT_DAMAGE, new NBTTagByte(NBT_DAMAGE, (byte) damage));
                    player.worldObj.playSoundAtEntity(player, MiTERenewed.RESOURCE_ID + "item.handpan.insert", .7F, 1F);
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
            stack.setTagInfo(NBT_PROGRESS, new NBTTagShort(NBT_PROGRESS, (short) use));
        }
    }

    @Override
    public void onItemUseFinish(ItemStack stack, World world, EntityPlayer player) {
        if (player.onServer()) {
            // Process recipe
            short content = stack.getTagCompound().getShort(NBT_CONTENT);
            byte damage = stack.getTagCompound().getByte(NBT_DAMAGE);
            List<ItemStack> stacks = HandpanRecipeProcessor.processRecipe(world, stack, content);

            // Spew outputs infront of player
            for (ItemStack output : stacks) {
                player.dropItemStack(output);
            }
            player.tryDamageHeldItem(DamageSource.generic, damage);

            // Play sfx
            if (!stacks.isEmpty())
                world.playSoundAtEntity(player, "random.pop", .7F, 1F);
        }
        stack.setTagInfo(NBT_CONTENT, new NBTTagShort(NBT_CONTENT, (short) 0));
    }

    public boolean hasMesh(ItemStack stack) {
        return stack.getItemSubtype() != EMPTY;
    }

    private ItemStack getBlockFromHotbar(EntityPlayer player) {
        // Get inventory
        InventoryPlayer inventory = player.inventory;

        // Iterate through hotbar
        for (int i = 0; i < 9; i++) {
            // Get item and ensure it's a block
            ItemStack at = inventory.getStackInSlot(i);
            if (at == null || !at.isBlock())
                continue;

            // Check if it has recipe
            if (HandpanRecipeProcessor.hasRecipe(at, player.getHeldItemStack().getItemSubtype()))
                return inventory.decrStackSize(i, 1);
        }
        return null;
    }

    @Override
    public void addInformation(ItemStack item_stack, EntityPlayer player, List info, boolean extended_info, Slot slot) {
        if (extended_info) {
            if (item_stack.hasTagCompound()) {
                // Get info
                NBTTagCompound nbt = item_stack.getTagCompound();
                if (nbt.hasKey(NBT_CONTENT)) {
                    short blockID = nbt.getShort(NBT_CONTENT);
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
            if (nbt.hasKey(NBT_PROGRESS))
                return stack.getTagCompound().getShort(NBT_PROGRESS);
        }
        return 200;
    }

    public static int getIntendedSpeed(ItemStack stack) {
        if (stack.hasTagCompound()) {
            NBTTagCompound nbt = stack.getTagCompound();
            if (nbt.hasKey(NBT_SPEED))
                return stack.getTagCompound().getShort(NBT_SPEED);
        }
        return 200;
    }

    public static int getTicksUsing(ItemStack stack, int use) {
        return stack.getMaxItemUseDuration() - use;
    }

    public static float getFractionUsing(ItemStack stack, int use) {
        float ticks = (float)getTicksUsing(stack, use);
        return Math.min(ticks / (float)getIntendedSpeed(stack), 1.0F);
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
