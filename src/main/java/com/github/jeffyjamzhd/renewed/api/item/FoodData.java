package com.github.jeffyjamzhd.renewed.api.item;

public class FoodData {
    public static final int HAS_PROTEIN = 1 << 0;
    public static final int HAS_FATS = 1 << 1;
    public static final int HAS_PHYTO = 1 << 2;

    public static Data foodData(int nutrient, int satiation, int sugar, int flags) {
        return new Data(nutrient, satiation, sugar, flags);
    }

    public static Effect foodEffect(int id, float chance, int length) {
        return new Effect(id, chance, length);
    }

    public record Effect(int id, float chance, int length) {
    }

    public record Data(int nutrient, int satiation, int sugar, int flags) {
        public boolean hasProtein() {
            return (flags & HAS_PROTEIN) == HAS_PROTEIN;
        }

        public boolean hasFats() {
            return (flags & HAS_FATS) == HAS_FATS;
        }

        public boolean hasPhyto() {
            return (flags & HAS_PHYTO) == HAS_PHYTO;
        }
    }
}
