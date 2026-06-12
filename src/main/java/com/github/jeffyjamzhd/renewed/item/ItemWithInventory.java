package com.github.jeffyjamzhd.renewed.item;

import com.github.jeffyjamzhd.renewed.api.inventory.BackpackInventory;
import com.github.jeffyjamzhd.renewed.network.C2SItemUpdateScroll;
import com.github.jeffyjamzhd.renewed.network.S2CItemInsertSFX;
import com.github.jeffyjamzhd.renewed.registry.RenewedEnchantments;
import com.github.jeffyjamzhd.renewed.registry.RenewedSounds;
import com.github.jeffyjamzhd.renewed.util.ItemUtils;
import com.jeffyjamzhd.jeffylib.api.IItemExtendedInteraction;
import com.jeffyjamzhd.jeffylib.api.impl.IItem;
import moddedmite.rustedironcore.network.Network;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.*;

import java.util.List;

public class ItemWithInventory extends Item implements IItem, IDamageableItem, IItemExtendedInteraction {
    /**
     * Arrangement of the rendering grid
     */
    private final int gridX, gridY;

    /**
     * Constructor for {@code ItemWithInventory}
     * @param id Item ID to occupy
     * @param x The amount of slots to render horizontally
     * @param y The amount of slots to render vertically
     */
    public ItemWithInventory(int id, String texture, int x, int y) {
        super(id, "");

        this.gridX = x;
        this.gridY = y;

        this.setMaxDamage(64);
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    @Override
    public void jl$onItemDestroyed(ItemStack stack, World world, double x, double y, double z) {
        if (!world.isRemote) {
            // Create inventory
            BackpackInventory inv = createInventory(stack);

            // Iterate and spill contents into world
            for (int slot = 0; slot < inv.getSizeInventory(); slot++) {
                // Get stack
                ItemStack stackInSlot = inv.getStackInSlot(slot);
                if (stackInSlot == null)
                    continue;

                // Create entity
                ItemUtils.ejectStackWithRandomVelocity(world, x, y, z, stackInSlot);
            }
        }
    }

    /**
     * Update method, checks if NBT data needs to be initialized
     */
    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int iInventorySlot, boolean bIsHandHeldItem) {
        super.onUpdate(stack, world, entity, iInventorySlot, bIsHandHeldItem);

        if (!hasProperCompoundTag(stack)) {
            // Create tag for this item, if one doesn't exist
            BackpackInventory inv = createInventory(stack);
            stack.stackTagCompound = inv.writeToNBT(stack.getTagCompound());
        }

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public void addInformation(ItemStack stack, EntityPlayer player, List stringList, boolean shift, Slot slot) {
        BackpackInventory inv = createInventory(stack);
        int slotCount = this.getInventorySize(stack) - inv.getSizeInventory();

        super.addInformation(stack, player, stringList, shift, slot);
        if (hasProperCompoundTag(stack)) {
            // Determine tooltip
            String slotCountString;
            if (slotCount > 1)
                slotCountString = I18n.getStringParams("tooltip.backpack.slots", slotCount);
            else if (slotCount == 1)
                slotCountString = I18n.getStringParams("tooltip.backpack.slot", slotCount);
            else
                slotCountString = I18n.getString("tooltip.backpack.full");

            // Add to list
            slotCountString = addStringFormatting(slotCountString);
            stringList.add(slotCountString);
        }

        // Add filter field
        if (hasFilterTag(stack)) {
            String filterString = addStringFormatting(I18n.getString("tooltip.backpack.filtered"));
            stringList.add(filterString);
        }
    }

    //***       IItemExtendedInteraction        ***//

    @Override
    @Environment(EnvType.CLIENT)
    public void beforeExtendedInteraction(ItemStack item, int slotID, boolean holdingShift) {
        if (holdingShift) {
            return;
        }
        int selectedSlot = getSelectedSlot(item);
        sendInventoryPositionPacket(slotID, selectedSlot);
    }

    @Override
    public boolean canInteractWithSelf(ItemStack itemStack, ItemStack itemStack1) {
        return false;
    }

    @Override
    public void itemRightClicked(ItemStack item, EntityPlayer player,
                                 World world, boolean holdingShift) {
        // JeffyBackpacks.logInfo("Item right clicked on {}!", !world.isRemote ? "server" : "client");

        // Get item from inventory
        BackpackInventory inv = createInventory(item);
        ItemStack invStack = null;

        if (!holdingShift && !inv.inventory.isEmpty()) {
            invStack = inv.getStackInSlot(inv.currentSlotID);
            inv.setInventorySlotContents(inv.currentSlotID, null);
        } else {
            ItemStack first = inv.popFirstStack();
            if (first != null) {
                ItemUtils.givePlayerStackOrEject(player, first);
            }
        }

        item.stackTagCompound = inv.writeToNBT(item.stackTagCompound);
        player.inventory.setItemStack(invStack);

        if (invStack != null) {
            playExtractSFX(world, player);
        }

        // Mark for update
        player.inventory.onInventoryChanged();
    }

    @Override
    public ItemStack itemRightClickAsMouseStack(ItemStack item, EntityPlayer player,
                                                World world, boolean holdingShift) {
        // JeffyBackpacks.logInfo("Item right clicked as mouse stack on {}!", !world.isRemote ? "server" : "client");

        // Get item from inventory
        BackpackInventory inv = createInventory(item);
        ItemStack invStack;

        if (!holdingShift && !inv.inventory.isEmpty()) {
            invStack = inv.getStackInSlot(inv.currentSlotID);
            inv.setInventorySlotContents(inv.currentSlotID, null);
        } else {
            invStack = inv.popFirstStack();
        }
        item.stackTagCompound = inv.writeToNBT(item.stackTagCompound);

        if (invStack != null) {
            playExtractSFX(world, player);
        }

        // Mark for update
        player.inventory.onInventoryChanged();
        return invStack;
    }

    @Override
    public void itemRightClickedWithStack(ItemStack itemStack, ItemStack mouseStack,
                                          EntityPlayer player, World world, boolean holdingShift) {
        // JeffyBackpacks.logInfo("Item right clicked with stack on {}!", !world.isRemote ? "server" : "client");

        if (isItemValidForInsertion(mouseStack)) {
            // Attempt to merge with inventory
            BackpackInventory inv = createInventory(itemStack);
            ItemStack result = inv.putStackSmart(mouseStack);
            itemStack.stackTagCompound = inv.writeToNBT(itemStack.stackTagCompound);

            if (result == null || !ItemUtils.areItemsEqual(mouseStack, result, false)) {
                playInsertSFX(world, player);
            } else {
                playFullSFX(world, player);
            }

            // Mark for update
            player.inventory.onInventoryChanged();
            player.inventory.setItemStack(result);
        }
    }

    @Override
    public ItemStack itemRightClickedWithStackAsMouseStack(ItemStack slotStack, ItemStack mouseStack,
                                                           EntityPlayer player, World world, boolean holdingShift) {
        // JeffyBackpacks.logInfo("Stack right clicked with item on {}!", !world.isRemote ? "server" : "client");

        if (isItemValidForInsertion(slotStack)) {
            // Attempt to merge with inventory
            BackpackInventory inv = createInventory(mouseStack);
            ItemStack result = inv.putStackSmart(slotStack);
            mouseStack.stackTagCompound = inv.writeToNBT(mouseStack.stackTagCompound);

            if (result == null || !ItemUtils.areItemsEqual(slotStack, result, false)) {
                playInsertSFX(world, player);
            } else {
                playFullSFX(world, player);
            }

            // Mark for update
            player.inventory.onInventoryChanged();
            return result;
        }
        return slotStack;
    }

    @Override
    public void itemScrolled(ItemStack item, EntityPlayer player,
                             World world, int direction, boolean holdingShift) {
        // Don't run: stack has no tag data or is holding shift
        if (!hasProperCompoundTag(item) || holdingShift) {
            return;
        }

        // Set scroll
        BackpackInventory inv = createInventory(item);
        int newScroll = inv.scrollCurrentSlotID(direction);
        setSelectedSlot(inv, item, newScroll);

        // Play sound
        Minecraft.getMinecraft().sndManager.playSoundFX("random.click", 0.5F, 1.8F + (itemRand.nextFloat() * 0.2F));

        // Mark for update
        player.inventory.onInventoryChanged();
    }

    @Override
    public int getNumComponentsForDurability() {
        return 1;
    }

    @Override
    public int getRepairCost() {
        return this.getNumComponentsForDurability() * 2;
    }

    @Override
    public Item getRepairItem() {
        return Item.sinew;
    }

    @Override
    public Material getMaterialForRepairs() {
        return Material.leather;
    }

    @Override
    public Material getMaterialForDurability() {
        return Material.leather;
    }

    @Override
    public int getItemEnchantability() {
        return EnumEquipmentMaterial.leather.enchantability * 3;
    }

    @Override
    public Material getMaterialForEnchantment() {
        return Material.leather;
    }

    //***       Class specific methods        ***//

    /**
     * Called by player inventory, for merging stack from world -> item inventory
     */
    public int putStackInInventory(ItemStack backpack, ItemStack stack,
                                   EntityPlayer player, World world) {

        if (isItemValidForInsertion(stack)) {
            // Create inventory and put stack inside
            BackpackInventory inv = createInventory(backpack);
            ItemStack result = inv.putStackSmart(stack);
            backpack.stackTagCompound = inv.writeToNBT(backpack.getTagCompound());

            // Play sfx and return stack size
            if (result == null || !ItemUtils.areItemsEqual(stack, result, false)) {
                if (!world.isRemote)
                    Network.sendToClient((ServerPlayer) player, new S2CItemInsertSFX());
            }

            // Mark for update
            player.inventory.onInventoryChanged();
            return result == null ? 0 : result.stackSize;
        }
        return stack.stackSize;
    }

    /**
     * Gets the amount of items currently in the provided stack.
     */
    public int getItemCountInStack(ItemStack stack) {
        BackpackInventory inv = createInventory(stack);
        return inv.getSizeInventory();
    }

    /**
     * {@code true} if provided {@link ItemStack} is able to
     * be inserted into this item
     */
    public boolean isItemValidForInsertion(ItemStack stack) {
        return !(stack.getItem() instanceof ItemWithInventory);
    }

    /**
     * Called when item is inserted
     */
    public void playInsertSFX(World world, EntityPlayer player) {
        world.playSoundAtEntity(player, RenewedSounds.BACKPACK_INSERT.toString(),
                0.9f + world.rand.nextFloat() * 0.1f, 1.0f + world.rand.nextFloat() * 0.25f);
    }

    /**
     * Called when item is extracted
     */
    public void playExtractSFX(World world, EntityPlayer player) {
        world.playSoundAtEntity(player, RenewedSounds.BACKPACK_EXTRACT.toString(),
                0.9f + world.rand.nextFloat() * 0.1f, 1.0f + world.rand.nextFloat() * 0.25f);
    }

    /**
     * Called when item insertion is attempted, but fails
     */
    public void playFullSFX(World world, EntityPlayer player) {
        world.playSoundAtEntity(player, RenewedSounds.BACKPACK_FULL.toString(),
                0.9f + world.rand.nextFloat() * 0.1f, 1.0f + world.rand.nextFloat() * 0.25f);
    }

    /**
     * {@code true} if the provided stack has an inventory.
     */
    public boolean hasProperCompoundTag(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            return false;
        }
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null) {
            BackpackInventory inv = createInventory(stack);
            return tag.hasKey(inv.getRootTagString());
        }
        return false;
    }

    /**
     * {@code true} if the provided stack has a filter applied
     */
    public boolean hasFilterTag(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            return false;
        }
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null) {
            BackpackInventory inv = createInventory(stack);
            return tag.hasKey("FilterInventory") && tag.hasKey("BackpackInventory");
        }
        return false;
    }

    /**
     * {@code true} if the provided stack has the enchantment glint tag (for EMI display)
     */
    public boolean hasEffectEMI(ItemStack stack) {
        if (stack == null || !stack.hasTagCompound()) {
            return false;
        }
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null) {
            return super.hasEffect(stack);
        }
        return false;
    }

    /**
     * Sets selected slot in provided {@link ItemStack}
     * @param slotID Slot to select
     */
    private void setSelectedSlot(BackpackInventory inv, ItemStack stack, int slotID) {
        NBTTagCompound compound = stack.getTagCompound();
        stack.stackTagCompound = inv.writeCurrentSlotToNBT(compound, slotID);
    }

    /**
     * Shorthand for creating an inventory instance for interaction handling
     */
    public BackpackInventory createInventory(ItemStack stack) {
        return new BackpackInventory(stack, this.getInventorySize(stack));
    }

    public int getInventorySize(ItemStack stack) {
        return this.getGridX(stack) * this.getGridY(stack);
    }

    public int getGridX(ItemStack stack) {
        return this.gridX + getAdditionalSlotsX(stack);
    }

    public int getGridY(ItemStack stack) {
        return this.gridY + getAdditionalSlotsY(stack);
    }

    public int getAdditionalSlotsX(ItemStack stack) {
        int level = RenewedEnchantments.ENCHANTMENT_HOLDING.getLevel(stack);
        return level > 0 ? MathHelper.floor_float((level + 1) / 2F) : 0;
    }

    public int getAdditionalSlotsY(ItemStack stack) {
        int level = RenewedEnchantments.ENCHANTMENT_HOLDING.getLevel(stack);
        return level > 0 ? MathHelper.floor_float(level / 2F) : 0;
    }

    public void setSelectedSlot(ItemStack stack, int slotID) {
        BackpackInventory inv = createInventory(stack);
        setSelectedSlot(inv, stack, slotID);
    }

    public int getSelectedSlot(ItemStack stack) {
        BackpackInventory inv = createInventory(stack);
        return inv.currentSlotID;
    }

    //***       Clientside methods        ***//

    /**
     * Sends C2S sync packet for item inventory position
     */
    @Environment(EnvType.CLIENT)
    public void sendInventoryPositionPacket(int slotID, int currentSlot) {
        Network.sendToServer(new C2SItemUpdateScroll((short) slotID, (short) currentSlot));
    }

    @Environment(EnvType.CLIENT)
    public String addStringFormatting(String string) {
        return EnumChatFormatting.GRAY.toString() + EnumChatFormatting.ITALIC +
                string + EnumChatFormatting.RESET;
    }
}
