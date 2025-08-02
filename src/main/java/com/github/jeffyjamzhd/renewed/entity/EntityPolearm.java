package com.github.jeffyjamzhd.renewed.entity;

import com.github.jeffyjamzhd.renewed.item.ItemPolearm;
import com.github.jeffyjamzhd.renewed.registry.RenewedItems;
import net.minecraft.*;
import net.xiaoyu233.fml.util.Log;

import java.util.Objects;
import java.util.Random;

public class EntityPolearm extends EntityThrowable implements IProjectile {
    private EntityLivingBase owner;
    private Entity lastHarmed;
    public int tX = -1, tY = -1, tZ = -1;

    private ItemPolearm item;
    private int durability;
    private double damage = 2.0D;
    private int knockback;
    private int ticksInGround, ticksInAir;
    private boolean inGround;
    private boolean canPickup;
    private int inTile;
    private int inData;

    public int polearmShake = 0;

    public EntityPolearm(World world) {
        super(world);
        this.setSize(1.5F, 1.5F);
        this.renderDistanceWeight = 10.0F;
        this.item = RenewedItems.flint_spear;
        this.damage = this.item.getScaledDamage(2.0F);
    }

    public EntityPolearm(World world, EntityLivingBase entity, float vel, ItemPolearm item, int durability) {
        super(world);
        this.setSize(1.5F, 1.5F);
        this.renderDistanceWeight = 10.0F;
        this.owner = entity;
        this.item = item;
        this.damage = this.item.getScaledDamage(2.0F);
        this.durability = durability;

        // Set location, angles
        this.setLocationAndAngles(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ, entity.rotationYaw, entity.rotationPitch);
        this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F) * (double)0.3F;
        this.posY -= 0.1F;
        this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F) * (double)0.3F;
        this.motionX = (-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
        this.motionZ = (MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
        this.motionY = (-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI));
        this.yOffset = 0.0F;
        this.setPosition(this.posX, this.posY, this.posZ);

        // Set stray from angle
        float wander = 1.0F;
        if (this.owner instanceof EntityPlayer player) {
            this.canPickup = true;
            int level = player.getExperienceLevel();
            if (level < 0)
                wander = Math.min(1.0F + (-0.75f * level), 8.0F);
        }
        if (entity.isSuspendedInLiquid())
            wander *= 2.0F;
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, vel * 1.5F, wander);
    }

    public EntityPolearm(WorldClient worldClient, double x, double y, double z) {
        super(worldClient, x, y, z);
    }

    @Override
    public void onUpdate() {
        // Update rotation/yaw
        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float var1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * (double) 180.0F / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(this.motionY, (double) var1) * (double) 180.0F / Math.PI);
        }

        // Check ground and tile
        int blockId = this.worldObj.getBlockId(this.tX, this.tY, this.tZ);
        if (blockId > 0) {
            AxisAlignedBB var2 = Block.blocksList[blockId].getCollisionBoundsCombined(this.worldObj, this.tX, this.tY, this.tZ, this, true);
            if (var2 != null && var2.isVecInside(this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ))) {
                this.inGround = true;
            }
        }

        // Progress anim
        if (this.polearmShake > 0) {
            --this.polearmShake;
        }

        // Handle fire
//        if (!this.worldObj.isRemote && this.isBurning()) {
//            Block block = this.inGround ? Block.getBlock(this.inTile) : null;
//            if (block != null && block.blockMaterial.isFreezing()) {
//                if (!this.isWet()) {
//                    this.causeQuenchEffect();
//                }
//
//                this.extinguish();
//            } else if (this.isInWater()) {
//                this.causeQuenchEffect();
//            } else if (this.isWet() && --this.ticks_until_next_fizz_sound <= 0) {
//                this.spawnSingleSteamParticle(true);
//                this.ticks_until_next_fizz_sound = this.rand.nextInt(17) + 3;
//            }
//        }

        // Handle ground collision and lifespan
        if (this.inGround) {
            int blockId2 = this.worldObj.getBlockId(this.tX, this.tY, this.tZ);
            int meta = this.worldObj.getBlockMetadata(this.tX, this.tY, this.tZ);
            if (blockId2 == this.inTile && (meta == this.inData || this.inTile == Block.grass.blockID)) {
                ++this.ticksInGround;
                if (this.ticksInGround >= (this.owner instanceof EntityPlayer ? 24000 : 1000)) {
                    this.setDead();
                }
            } else {
                this.inGround = false;
                Random rand = new Random(this.entityId);
                this.motionX *= (rand.nextFloat() * 0.2F);
                this.motionY *= (rand.nextFloat() * 0.2F);
                this.motionZ *= (rand.nextFloat() * 0.2F);
                this.ticksInGround = 0;
                this.ticksInAir = 0;
            }
        } else {
            // Handle airborne behavior
            ++this.ticksInAir;
            if (this.onServer()) {
                AxisAlignedBB bb = this.boundingBox.copy();
                if (this.worldObj.isBoundingBoxBurning(bb.contract(0.001, 0.001, 0.001), true)) {
                    this.setFire(8);
                } else if (this.worldObj.isBoundingBoxBurning(bb.contract(0.001, 0.001, 0.001).translate(this.motionX / 2.0F, this.motionY / 2.0F, this.motionZ / 2.0F), true)) {
                    this.setFire(8);
                }
            }

            // Do ray checks
            Vec3 current_pos = this.worldObj.getVec3(this.posX, this.posY, this.posZ);
            Vec3 future_pos = this.worldObj.getVec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            Raycast raycast = (new Raycast(this.worldObj, current_pos, future_pos)).setForPiercingProjectile(this).performVsBlocks();
            RaycastCollision collision = raycast.getBlockCollision();
            RaycastCollision collisionCopy = collision;
            if (collision != null) {
                raycast.setLimitToBlockCollisionPoint();
            }
            if (raycast.performVsEntities().hasEntityCollisions()) {
                collision = raycast.getNearestCollision();
            }
            if (collision != null && collision.getEntityHit() instanceof EntityPlayer player) {
                if (player.capabilities.disableDamage || this.owner instanceof EntityPlayer attacker && attacker.canAttackPlayer(player)) {
                    collision = null;
                }
            }
            if (collision == null || !collision.isEntity()) {
                this.lastHarmed = null;
            }
            if (collision != null) {
                if (!collision.isEntity()) {
                    this.handleCollisionWithBlock(collision);
                } else {
                    Entity hit = collision.getEntityHit();
                    float vel = this.getVelocity();
                    float damageMult = 1.0F;

                    // Entity hit is living
                    if (hit instanceof EntityLivingBase entityHit) {
                        if (entityHit.isEntityUndead() && this.item.getToolMaterial() == Material.silver)
                            damageMult *= 1.25F;
                        if (entityHit instanceof EntitySkeleton)
                            damageMult *= 0.25F;
                    }

                    // Add more damage if critical
                    if (this.getIsCritical())
                        damageMult *= 1.5F + this.rand.nextFloat() * 0.5F;

                    // Determine damage source
                    DamageSource damageSource;
                    damageSource = DamageSource.causeThrownDamage(this, Objects.requireNonNullElse(this.owner, this));

                    // Carry over from EntityArrow
                    if (this.isBurning() && !(hit instanceof EntityEnderman)) {
                        if (hit instanceof EntityGelatinousCube) {
                            if (this.onServer()) {
                                if (this.getVelocity() < 1.0F) {
                                    hit.attackEntityFrom(new Damage(DamageSource.inFire, 1.0F));
                                    this.extinguish(true);
                                } else {
                                    ++damageMult;
                                    hit.entityFX(EnumEntityFX.steam_with_hiss);
                                }
                            }
                        } else {
                            hit.setFire(5);
                        }
                    }

                    // Consume projectile if acidic
                    if (hit instanceof EntityGelatinousCube cube && cube.isAcidic() && this.item.isHarmedByAcid()) {
                        if (this.onServer())
                            this.entityFX(EnumEntityFX.steam_with_hiss);

                        this.setDead();
                    }

                    Damage damage = new Damage(damageSource, (float) (damageMult * this.damage));
                    boolean immuneToProjectile = hit.isEntityInvulnerable() || hit.isImmuneTo(damageSource);
                    if (hit != this.lastHarmed) {
                        if (!(this.getVelocity() < 1.0F) && !immuneToProjectile) {
                            // Server update
                            if (this.onServer()) {
                                EntityDamageResult result = hit.attackEntityFrom(damage);
                                if (result == null && hit instanceof EntityPhaseSpider) {
                                    if (collisionCopy != null)
                                        this.handleCollisionWithBlock(collisionCopy);
                                } else if (result != null && result.entityWasNegativelyAffected()) {
                                    this.lastHarmed = hit;
                                    if (hit instanceof EntityLivingBase target && this.owner != null) {
                                        this.owner.setLastAttackTarget(target);

                                        // Apply knockback
                                        if (this.knockback > 0) {
                                            float var27 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
                                            if (var27 > 0.0F)
                                                hit.addVelocity(this.motionX * this.knockback * (double) 0.6F / (double) var27, 0.1, this.motionZ * this.knockback * (double) 0.6F / (double) var27);
                                        }

                                        // Apply thorns
                                        if (this.owner != null) {
                                            if (this.worldObj.isRemote) {
                                                System.out.println("EntityPolearm.onUpdate() is calling EnchantmentThorns.func_92096_a() on client");
                                                Minecraft.temp_debug = "arrow";
                                            }

                                            EnchantmentThorns.func_92096_a(this.owner, target, this.rand);
                                        }

                                        if (this.owner != null && hit != this.owner && hit instanceof EntityPlayer && this.owner instanceof ServerPlayer serverPlayer)
                                            serverPlayer.playerNetServerHandler.sendPacketToPlayer(new Packet70GameEvent(6, 0));
                                    }
                                }

                                // Play sound and bounce back
                                this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                                if (!(hit instanceof EntityEnderman))
                                    this.bounceBack();

                                // Refresh owner despawn
                                if (this.owner != null && hit instanceof EntityPlayer)
                                    this.owner.refreshDespawnCounter(-9600);

                                this.onImpact(collision);

                            } else {
                                this.bounceBack();
                            }
                        }
                    } else {
                        this.bounceBack();
                    }
                }
            }

            // Move
            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            float var20 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * (double) 180.0F / Math.PI);

            for (this.rotationPitch = (float) (Math.atan2(this.motionY, (double) var20) * (double) 180.0F / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
            }

            while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
                this.prevRotationPitch += 360.0F;
            while (this.rotationYaw - this.prevRotationYaw < -180.0F)
                this.prevRotationYaw -= 360.0F;
            while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
                this.prevRotationYaw += 360.0F;

            this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
            this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;

            // Handle water movement
            float drag = 0.99F;
            float deaccel = 0.05F;
            float damp = 0.25F;
            if (this.isInWater()) {
                for (int i = 0; i < 4; ++i)
                    this.worldObj.spawnParticle(EnumParticle.bubble, this.posX - this.motionX * damp, this.posY - this.motionY * damp, this.posZ - this.motionZ * damp, this.motionX, this.motionY, this.motionZ);
                drag = 0.8F;
            }

            this.motionX *= drag;
            this.motionY *= drag;
            this.motionZ *= drag;
            this.motionY -= deaccel;
            this.setPosition(this.posX, this.posY, this.posZ);
            this.doBlockCollisions();
        }
    }

    @Override
    protected void onImpact(RaycastCollision col) {
        // Check item durability
        if (this.item != null && this.item.getItemStackForStatsIcon().getMaxDamage() - durability <= 0) {
            this.setDead();
            this.playSound("random.break", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));

            if (!this.worldObj.isRemote && col.getEntityHit() instanceof EntityLivingBase hit) {
                hit.entityFX(EnumEntityFX.item_breaking, new SignalData().setByte(0).setShort(this.item.itemID));
            }
        }
    }

    private void handleCollisionWithBlock(RaycastCollision col) {
        this.tX = col.block_hit_x;
        this.tY = col.block_hit_y;
        this.tZ = col.block_hit_z;
        this.inTile = this.worldObj.getBlockId(this.tX, this.tY, this.tZ);
        this.inData = this.worldObj.getBlockMetadata(this.tX, this.tY, this.tZ);
        this.motionX = col.position_hit.xCoord - this.posX;
        this.motionY = col.position_hit.yCoord - this.posY;
        this.motionZ = col.position_hit.zCoord - this.posZ;
        float var20 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
        this.posX -= this.motionX / var20 * 0.05F;
        this.posY -= this.motionY / var20 * 0.05F;
        this.posZ -= this.motionZ / var20 * 0.05F;
        this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
        this.inGround = true;
        this.polearmShake = 7;

        this.setIsCritical(false);
        if (this.inTile != 0) {
            Block.blocksList[this.inTile].onEntityCollidedWithBlock(this.worldObj, this.tX, this.tY, this.tZ, this);
        }

        if (this.onServer()) {
            this.sendPacketToAllPlayersTrackingEntity((new Packet85SimpleSignal(EnumSignal.arrow_hit_block)).setEntityID(this).setExactPosition(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ));
        }

        this.onImpact(col);
    }

    @Override
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9) {
        this.setPosition(par1, par3, par5);
        this.setRotation(par7, par8);
    }

    @Override
    public void setVelocity(double par1, double par3, double par5) {
        this.motionX = par1;
        this.motionY = par3;
        this.motionZ = par5;
        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float var7 = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
            this.prevRotationYaw = (float)(Math.atan2(par1, par5) * (double)180.0F / Math.PI);
            this.prevRotationPitch = (float)(Math.atan2(par3, var7) * (double)180.0F / Math.PI);
            this.ticksInGround = 0;
        }
    }

    @Override
    public void spentTickInLava() {
        if (this.inGround) {
            super.spentTickInLava();
        }
    }

    @Override
    public boolean cannotRaycastCollideWith(Entity entity) {
        return entity == this.owner && this.ticksInAir < 5 ? true : super.cannotRaycastCollideWith(entity);
    }

    public void setIsCritical(boolean par1) {
        byte var2 = this.dataWatcher.getWatchableObjectByte(16);
        if (par1) {
            this.dataWatcher.updateObject(16, (byte)(var2 | 1));
        } else {
            this.dataWatcher.updateObject(16, (byte)(var2 & -2));
        }
    }

    public boolean getIsCritical() {
        byte var1 = this.dataWatcher.getWatchableObjectByte(16);
        return (var1 & 1) != 0;
    }

    public float getVelocity() {
        return MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
    }

    private void bounceBack() {
        this.motionX *= -0.1F;
        this.motionY *= -0.1F;
        this.motionZ *= -0.1F;
        this.rotationYaw += 180.0F;
        this.prevRotationYaw += 180.0F;
        this.ticksInAir = 0;
        this.motionX /= 4.0F;
        this.motionY /= -4.0F;
        this.motionZ /= 4.0F;
    }

    @Override
    protected void entityInit() {
        this.dataWatcher.addObject(16, (byte)0);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setShort("xTile", (short)this.tX);
        nbt.setShort("yTile", (short)this.tY);
        nbt.setShort("zTile", (short)this.tZ);
        nbt.setByte("inTile", (byte)this.inTile);
        nbt.setByte("inData", (byte)this.inData);
        nbt.setByte("shake", (byte)this.polearmShake);
        nbt.setBoolean("inGround", this.inGround);
        nbt.setBoolean("pickup", this.canPickup);
        nbt.setDouble("damage", this.damage);
        nbt.setInteger("polearm_id", this.item.itemID);
        nbt.setInteger("polearm_durability", this.durability);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.tX = nbt.getShort("xTile");
        this.tY = nbt.getShort("yTile");
        this.tZ = nbt.getShort("zTile");
        this.inTile = nbt.getByte("inTile") & 255;
        this.inData = nbt.getByte("inData") & 255;
        this.polearmShake = nbt.getByte("shake") & 255;
        this.inGround = nbt.getBoolean("inGround");

        if (nbt.hasKey("pickup"))
            this.canPickup = nbt.getBoolean("pickup");
        if (nbt.hasKey("damage"))
            this.damage = nbt.getDouble("damage");

        this.item = (ItemPolearm) Item.itemsList[nbt.getInteger("polearm_id")];
        this.durability = nbt.getInteger("polearm_durability");
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer player) {
        if (!this.worldObj.isRemote && this.inGround && this.polearmShake <= 0) {
            if (player.inventory.addItemStackToInventory(new ItemStack(this.item, 1).setItemDamage(this.durability))) {
                this.playSound("random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                player.onItemPickup(this, 1);
                this.setDead();
            }
        }

    }

    @Override
    public Item getModelItem() {
        return this.item;
    }

    @Override
    public EntityLivingBase getThrower() {
        return this.owner;
    }

    @Override
    public boolean canCatchFire() {
        return false;
    }

    public void setDamage(double value) {
        this.damage = value;
    }

    public void setKnockback(int value) {
        this.knockback = value;
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public float getShadowSize() {
        return 0.0F;
    }

    @Override
    public void setThrowableHeading(double par1, double par3, double par5, float velocity, float par8) {
        float var9 = MathHelper.sqrt_double(par1 * par1 + par3 * par3 + par5 * par5);
        par1 /= var9;
        par3 /= var9;
        par5 /= var9;
        par1 += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * (double)0.0075F * (double)par8;
        par3 += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * (double)0.0075F * (double)par8;
        par5 += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * (double)0.0075F * (double)par8;
        par1 *= velocity;
        par3 *= velocity;
        par5 *= velocity;
        this.motionX = par1;
        this.motionY = par3;
        this.motionZ = par5;
        float var10 = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
        this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(par1, par5) * (double)180.0F / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(par3, var10) * (double)180.0F / Math.PI);
        this.ticksInGround = 0;
    }
}
