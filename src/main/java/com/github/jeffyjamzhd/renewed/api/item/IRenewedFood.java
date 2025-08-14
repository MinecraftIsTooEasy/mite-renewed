package com.github.jeffyjamzhd.renewed.api.item;

import net.minecraft.EntityPlayer;

public interface IRenewedFood {
    /**
     * Gets nutrition for item subtype
     */
    int getNutritionSubtype(int sub);

    /**
     * Gets satiation for item subtype
     */
    int getSatiationSubtype(EntityPlayer player, int sub);

    /**
     * Gets protein for item subtype
     */
    int getProteinSubtype(int sub);

    /**
     * Gets essential fats for item subtype
     */
    int getEssentialFatsSubtype(int sub);

    /**
     * Gets phytonutrients for item subtype
     */
    int getPhytonutrientsSubtype(int sub);

    /**
     * Gets sugar contents for item subtype
     */
    int getSugarSubtype(int sub);
}
