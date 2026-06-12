package com.github.jeffyjamzhd.renewed.registry;

import com.github.jeffyjamzhd.renewed.enchantment.EnchantmentFilling;
import com.github.jeffyjamzhd.renewed.enchantment.EnchantmentHolding;
import com.github.jeffyjamzhd.renewed.enchantment.EnchantmentVacuum;
import net.minecraft.Enchantment;
import net.xiaoyu233.fml.reload.event.EnchantmentRegistryEvent;
import net.xiaoyu233.fml.reload.utils.IdUtil;

public class RenewedEnchantments {
    public static final Enchantment ENCHANTMENT_FILLING = new EnchantmentFilling(next());
    public static final Enchantment ENCHANTMENT_HOLDING = new EnchantmentHolding(next());
    public static final Enchantment ENCHANTMENT_VACUUM = new EnchantmentVacuum(next());

    public static void register(EnchantmentRegistryEvent event) {
        event.registerEnchantment(ENCHANTMENT_FILLING, ENCHANTMENT_HOLDING, ENCHANTMENT_VACUUM);
    }

    private static int next() {
        return IdUtil.getNextEnchantmentID();
    }
}
