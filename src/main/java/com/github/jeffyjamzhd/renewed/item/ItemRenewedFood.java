package com.github.jeffyjamzhd.renewed.item;

import com.github.jeffyjamzhd.renewed.api.IEntityPlayer;
import com.github.jeffyjamzhd.renewed.api.item.FoodData;
import com.github.jeffyjamzhd.renewed.api.item.IRenewedFood;
import net.minecraft.*;

import java.util.List;
import java.util.Random;

public class ItemRenewedFood extends Item implements IRenewedFood {
    private String[] names;
    private Icon[] icons;
    private ResourceLocation[] textures;

    public Item uncookedFood;
    public Item cookedFood;

    private int[] nutrition;
    private int[] satiation;
    private int[] sugar;
    private boolean[] hasProtein;
    private boolean[] hasFats;
    private boolean[] hasPhytonutrients;
    private int[] effectID;
    private float[] effectChance;
    private int[] effectDuration;
    private int[] xpReward;

    public ItemRenewedFood(int id, int subtypes, Material mat) {
        super(id, "", subtypes);
        this.setCreativeTab(CreativeTabs.tabFood);
        this.setMaterial(mat);

        // Adjust size of arrays
        this.icons = new Icon[subtypes + 1];
        this.nutrition = new int[subtypes + 1];
        this.satiation = new int[subtypes + 1];
        this.sugar = new int[subtypes + 1];
        this.hasProtein = new boolean[subtypes + 1];
        this.hasFats = new boolean[subtypes + 1];
        this.hasPhytonutrients = new boolean[subtypes + 1];
        this.effectID = new int[subtypes + 1];
        this.effectChance = new float[subtypes + 1];
        this.effectDuration = new int[subtypes + 1];
        this.xpReward = new int[subtypes + 1];
    }

    @Override
    public void onItemUseFinish(ItemStack item_stack, World world, EntityPlayer player) {
        if (player.onServer()) {
            ((IEntityPlayer)player).mr$addFoodValueSubtype(this, item_stack.getItemSubtype());
            world.playSoundAtEntity(player, "random.burp", 0.5F, player.getRNG().nextFloat() * 0.1F + 0.9F);
            this.onEaten(item_stack, player);
        }

        super.onItemUseFinish(item_stack, world, player);
    }

    private void onEaten(ItemStack stack, EntityPlayer player) {
        int sub = stack.getItemSubtype();
        if (this.effectID[sub] != 0) {
            Random rng = player.getRNG();
            boolean affect = this.effectChance[sub] < rng.nextFloat();
            if (affect)
                player.addPotionEffect(new PotionEffect(this.effectID[sub], this.effectDuration[sub], 0));
        }

    }

    @Override
    public ItemStack getItemProducedWhenDestroyed(ItemStack item_stack, DamageSource damage_source) {
        if (damage_source.isFireDamage()) {
            Item cooked_item = this.cookedFood;
            if (cooked_item != null) {
                return new ItemStack(cooked_item.itemID, item_stack.stackSize, item_stack.getItemSubtype());
            }
        }

        return super.getItemProducedWhenDestroyed(item_stack, damage_source);
    }

    @Override
    public void addInformation(ItemStack item_stack, EntityPlayer player, List info, boolean extended_info, Slot slot) {
        if (extended_info) {
            int subtype = item_stack.getItemSubtype();
            int satiation = this.getSatiationSubtype(player, subtype);
            int nutrition = this.getNutritionSubtype(subtype);
            if (satiation > 0 || nutrition > 0) {
                info.add("");

                if (satiation > 0)
                    info.add(EnumChatFormatting.BROWN + Translator.getFormatted("item.tooltip.satiation", satiation));
                if (nutrition > 0)
                    info.add(EnumChatFormatting.BROWN + Translator.getFormatted("item.tooltip.nutrition", nutrition));
            }
        }

    }

    public static void setRelations(ItemRenewedFood uncookedFood, ItemRenewedFood cookedFood) {
        cookedFood.uncookedFood = uncookedFood;
        uncookedFood.cookedFood = cookedFood;
    }

    public ItemRenewedFood setTranslationKeys(String... names) {
        this.names = names;
        return this;
    }

    public ItemRenewedFood setTextures(ResourceLocation... resources) {
        this.textures = resources;
        return this;
    }

    public ItemRenewedFood setXP(int... values) {
        this.xpReward = values;
        return this;
    }

    public ItemRenewedFood setData(FoodData.Data... data) {
        int pos = 0;
        for (FoodData.Data entry : data) {
            this.nutrition[pos] = entry.nutrient();
            this.satiation[pos] = entry.satiation();
            this.hasProtein[pos] = entry.hasProtein();
            this.hasFats[pos] = entry.hasFats();
            this.hasPhytonutrients[pos] = entry.hasPhyto();

            pos++;
        }
        return this;
    }

    public ItemRenewedFood setEffect(FoodData.Effect... effects) {
        int pos = 0;
        for (FoodData.Effect entry : effects) {
            this.effectID[pos] = entry.id();
            this.effectDuration[pos] = entry.length();
            this.effectChance[pos] = entry.chance();

            pos++;
        }
        return this;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public boolean isEatable(int item_subtype) {
        return true;
    }

    @Override
    public ItemRenewedFood setAlwaysEdible() {
        super.setAlwaysEdible();
        return this;
    }

    @Override
    public ItemRenewedFood setAnimalProduct() {
        super.setAnimalProduct();
        return this;
    }

    @Override
    public ItemRenewedFood setPlantProduct() {
        super.setPlantProduct();
        return this;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        if (stack == null)
            return super.getUnlocalizedName(null);
        return this.names[stack.getItemSubtype()];
    }

    @Override
    public void getSubItems(int id, CreativeTabs tabs, List list) {
        for (int sub = 0; sub < getNumSubtypes(); sub++)
            list.add(new ItemStack(id, 1, sub));
    }

    @Override
    public void registerIcons(IconRegister register) {
        this.icons = new Icon[getNumSubtypes()];
        for (int i = 0; i < getNumSubtypes(); ++i)
            this.icons[i] = register.registerIcon(this.textures[i].toString());
    }

    @Override
    public Icon getIconFromSubtype(int par1) {
        return this.icons[par1];
    }

    @Override
    public int getNutritionSubtype(int sub) {
        return this.nutrition[sub];
    }

    @Override
    public int getSatiationSubtype(EntityPlayer player, int sub) {
        return this.satiation[sub];
    }

    @Override
    public int getSugarSubtype(int sub) {
        return this.sugar[sub];
    }

    @Override
    public int getProteinSubtype(int sub) {
        return this.hasProtein[sub] ? this.getNutritionSubtype(sub) * 8000 : 0;
    }

    @Override
    public int getEssentialFatsSubtype(int sub) {
        return this.hasFats[sub] ? this.getNutritionSubtype(sub) * 8000 : 0;
    }

    @Override
    public int getPhytonutrientsSubtype(int sub) {
        return this.hasPhytonutrients[sub] ? this.getNutritionSubtype(sub) * 8000 : 0;
    }

    @Override
    public int getExperienceReward(int sub) {
        return this.xpReward[sub];
    }

    @Override
    public int getHeatLevel(ItemStack item_stack) {
        return -1;
    }
}
