package com.github.jeffyjamzhd.renewed.item;

import net.minecraft.*;

import java.util.List;

public class ItemRenewedBucket extends ItemBucket implements IDamageableItem {
    public ItemRenewedBucket(int id, Material material, Material contents) {
        super(id, material, contents);
        this.setMaxDamage(getDurabilityForMaterial(material));
    }

    @Override
    public boolean onItemRightClick(EntityPlayer player, float partial_tick, boolean ctrl_is_down) {
        RaycastCollision rc = player.getSelectedObject(partial_tick, true);
        if (rc == null || !rc.isBlock()) {
            return false;
        }

        int x, y, z;
        ItemStack item_stack = player.getHeldItemStack();

        if (this.isEmpty()) {
            Material material;
            if (rc.getBlockHitMaterial().isLiquid()) {
                x = rc.block_hit_x;
                y = rc.block_hit_y;
                z = rc.block_hit_z;
                material = rc.getBlockHitMaterial();
            } else {
                x = rc.neighbor_block_x;
                y = rc.neighbor_block_y;
                z = rc.neighbor_block_z;
                material = rc.getNeighborOfBlockHitMaterial();
            }
            if (material == null || !material.isLiquid()) {
                return false;
            }
            if (player.inCreativeMode() && !player.canMineAndEditBlock(x, y, z)) {
                return false;
            }
            if (player.onServer()) {
                if (player.inCreativeMode() || ctrl_is_down) {
                    rc.world.setBlockToAir(x, y, z);
                }
                if (!player.inCreativeMode()) {
                    player.convertOneOfHeldItem(this.convertTo(item_stack, this.getPeerForContents(material)));
                }
            }
            return true;
        }
        if (this.contains(Material.stone)) {
            return false;
        }
        if (this.contains(Material.water)) {
            Block block = rc.getBlockHit();
            x = rc.block_hit_x;
            y = rc.block_hit_y;
            z = rc.block_hit_z;
            EnumFace face_hit = rc.face_hit;
            if (rc.world.getBlock(x, y - 1, z) == Block.tilledField) {
                block = rc.world.getBlock(x, --y, z);
                face_hit = EnumFace.TOP;
            }
            if (block == Block.tilledField && face_hit == EnumFace.TOP) {
                if (BlockFarmland.fertilize(rc.world, x, y, z, player.getHeldItemStack(), player)) {
                    if (player.onServer() && !player.inCreativeMode()) {
                        player.convertOneOfHeldItem(this.convertTo(item_stack, this.getEmptyVessel()));
                    }
                    return true;
                }
                return false;
            }
        }
        if (!(player.inCreativeMode() || rc.getBlockHitMaterial() != this.getContents() && rc.getNeighborOfBlockHitMaterial() != this.getContents())) {
            if (player.onServer()) {
                player.convertOneOfHeldItem(this.convertTo(item_stack, this.getEmptyVessel()));
            }
            return true;
        }
        if (rc.getBlockHit().isLiquid() || rc.isBlockHitReplaceableBy(this.getBlockForContents(), 0)) {
            x = rc.block_hit_x;
            y = rc.block_hit_y;
            z = rc.block_hit_z;
        } else {
            x = rc.neighbor_block_x;
            y = rc.neighbor_block_y;
            z = rc.neighbor_block_z;
        }
        if (!player.canPlayerEdit(x, y, z, item_stack)) {
            return false;
        }
        if (this.tryPlaceContainedLiquid(rc.world, player, x, y, z, ItemBucket.shouldContainedLiquidBePlacedAsSourceBlock(player, ctrl_is_down))) {
            if (player.onServer() && !player.inCreativeMode() && player.hasHeldItem()) {
                player.convertOneOfHeldItem(this.convertTo(item_stack, this.getEmptyVessel()));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean tryEntityInteraction(Entity entity, EntityPlayer player, ItemStack item_stack) {
        if (entity instanceof EntityLivestock) {
            EntityLivestock livestock;
            if (this.isEmpty()) {
                if (entity instanceof EntityCow cow && cow.getMilk() >= this.standard_volume * 25) {
                    if (player.onServer()) {
                        cow.setMilk(cow.getMilk() - this.standard_volume * 25);
                        if (!player.inCreativeMode()) {
                            player.convertOneOfHeldItem(this.convertTo(item_stack, this.getPeerForContents(Material.milk)));
                        }
                    }
                    return true;
                }
            } else if (this.contains(Material.water) && (livestock = (EntityLivestock)entity).isThirsty()) {
                if (player.onServer()) {
                    livestock.addWater(0.25f * (float)this.standard_volume);
                    if (!player.inCreativeMode()) {
                        player.convertOneOfHeldItem(this.convertTo(item_stack, this.getEmptyVessel()));
                    }
                }
                return true;
            }
        } else if (this.canContentsDouseFire()) {
            EntityEarthElemental elemental;
            if (entity instanceof EntityFireElemental) {
                if (player.onServer()) {
                    entity.attackEntityFrom(new Damage(DamageSource.water, 20.0f));
                    entity.causeQuenchEffect();
                    if (!player.inCreativeMode()) {
                        player.convertOneOfHeldItem(this.convertTo(item_stack, this.getEmptyVessel()));
                    }
                }
                return true;
            }
            if (entity instanceof EntityNetherspawn) {
                if (player.onServer()) {
                    entity.attackEntityFrom(new Damage(DamageSource.water, 8.0f));
                    entity.causeQuenchEffect();
                    if (!player.inCreativeMode()) {
                        player.convertOneOfHeldItem(this.convertTo(item_stack, this.getEmptyVessel()));
                    }
                }
                return true;
            }
            if (entity instanceof EntityEarthElemental && (elemental = (EntityEarthElemental)entity).isMagma()) {
                if (player.onServer()) {
                    elemental.convertToNormal(true);
                    if (!player.inCreativeMode()) {
                        player.convertOneOfHeldItem(this.convertTo(item_stack, this.getEmptyVessel()));
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean tryPlaceContainedLiquid(World world, EntityPlayer player,
                                           int x, int y, int z,
                                           boolean allow_placement_of_source_block) {
        boolean result = super.tryPlaceContainedLiquid(world, player, x, y, z, allow_placement_of_source_block);
        if (result) {
            if (world.isWorldClient()) {
                return true;
            }
            if (this.getsDamaged() && this.isHoldingLava()) {
                player.tryDamageHeldItem(DamageSource.generic, 1);
            }
        }
        return result;
    }

    @Override
    public int getItemStackLimit(int subtype, int damage) {
        return 1;
    }

    @Override
    public void addInformation(ItemStack item_stack, EntityPlayer player, List info, boolean extended_info, Slot slot) {
        if (extended_info && player != null && player.experience >= 100 && (this.contains(Material.water) || this.contains(Material.lava))) {
            info.add((this.contains(Material.water) ? EnumChatFormatting.BLUE : EnumChatFormatting.RED) + Translator.get("item.tooltip.placeBucketAsSource"));
        }
    }

    @Override
    public Material getMaterialForDurability() {
        return this.getVesselMaterial();
    }

    public static int getDurabilityForMaterial(Material material) {
        // Low quality metals = 1 lava use
        if (material == Material.copper
                || material == Material.silver
                || material == Material.gold) {
            return 1;
        }

        return (int) material.durability;
    }

    public ItemStack convertTo(ItemStack from, Item to) {
        return new ItemStack(to).setItemDamage(from.getItemDamage());
    }

    /**
     * {@code true} if this bucket can be damaged
     */
    public boolean getsDamaged() {
        return this.getVesselMaterial() != Material.adamantium;
    }

    /**
     * {@code true} if holding lava
     */
    public boolean isHoldingLava() {
        return this.getContents() == Material.lava;
    }

    @Override
    public boolean isRepairable() {
        return false;
    }

    @Override
    public boolean hasRepairCost() {
        return false;
    }

    @Override
    public Item getRepairItem() {
        return null;
    }

    @Override
    public Material getMaterialForRepairs() {
        return null;
    }

    @Override
    public int getRepairCost() {
        return 0;
    }

    @Override
    public int getNumComponentsForDurability() {
        return 1;
    }
}
