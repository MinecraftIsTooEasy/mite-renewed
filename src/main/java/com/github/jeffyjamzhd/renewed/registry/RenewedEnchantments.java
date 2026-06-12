package com.github.jeffyjamzhd.renewed.registry;

import com.github.jeffyjamzhd.renewed.enchantment.EnchantmentSoulbound;
import com.github.jeffyjamzhd.renewed.enchantment.EnchantmentVacuum;
import com.github.jeffyjamzhd.renewed.enchantment.EnchantmentHolding;
import com.github.jeffyjamzhd.renewed.enchantment.EnchantmentFeatherWeight;
import net.minecraft.Enchantment;
import net.xiaoyu233.fml.reload.event.EnchantmentRegistryEvent;
import net.xiaoyu233.fml.reload.utils.IdUtil;

public class RenewedEnchantments {
    public static final Enchantment ENCHANTMENT_VACUUM = new EnchantmentVacuum(next());
    public static final Enchantment ENCHANTMENT_HOLDING = new EnchantmentHolding(next());
    public static final Enchantment ENCHANTMENT_FEATHER_WEIGHT = new EnchantmentFeatherWeight(next());
    public static final Enchantment ENCHANTMENT_SOUL_BOUND = new EnchantmentSoulbound(next());

    public static void register(EnchantmentRegistryEvent event) {
        event.registerEnchantment(
                ENCHANTMENT_VACUUM,
                ENCHANTMENT_HOLDING,
                ENCHANTMENT_FEATHER_WEIGHT,
                ENCHANTMENT_SOUL_BOUND
        );
    }

    private static int next() {
        return IdUtil.getNextEnchantmentID();
    }
}
