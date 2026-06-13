package com.github.jeffyjamzhd.renewed.registry;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import moddedmite.rustedironcore.api.util.PotionExtend;
import net.minecraft.Potion;
import net.minecraft.SharedMonsterAttributes;
import net.xiaoyu233.fml.reload.utils.IdUtil;

public class RenewedPotion extends PotionExtend {
    public static final Potion ENCUMBERED = new RenewedPotion(next(), true, 0x111111, "encumbered")
            .func_111184_a(SharedMonsterAttributes.attackDamage, "22653B89-116E-49DC-9B6B-9971489B5BE5", -.2F, 1)
            .func_111184_a(SharedMonsterAttributes.movementSpeed, "7107DE5E-7CE8-4030-940E-514C1F160890", -.2F, 2);

    public RenewedPotion(int id, boolean bad, int color, String texture) {
        super(id, bad, color, MiTERenewed.of("textures/gui/effect/%s.png".formatted(texture)));
        this.setPotionName("potion.renewed.%s".formatted(texture));
    }

    public static void register() {
        MiTERenewed.LOGGER.info("Registering potion effects");
    }

    private static int next() {
        return IdUtil.getNextPotionId();
    }
}
