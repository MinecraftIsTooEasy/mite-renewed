package com.github.jeffyjamzhd.renewed.compat.emi.recipe;

import com.github.jeffyjamzhd.renewed.compat.emi.RenewedRecipeCategories;
import com.github.jeffyjamzhd.renewed.item.recipe.HandpanRecipe;
import com.github.jeffyjamzhd.renewed.registry.RenewedItems;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.TextWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.EnumChatFormatting;
import net.minecraft.ItemStack;
import net.minecraft.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import shims.java.net.minecraft.text.Text;
import shims.java.net.minecraft.util.Formatting;
import shims.java.net.minecraft.util.SyntheticIdentifier;

import java.util.Comparator;
import java.util.List;

public class HandpanEMIRecipe implements EmiRecipe {
    private EmiIngredient input;
    private List<EmiHandpanIngredient> outputs;
    private EmiIngredient mesh;
    private int damage;
    private int speed;

    public HandpanEMIRecipe(HandpanRecipe recipe) {
        this.input = EmiStack.of(recipe.getInput());
        this.outputs = recipe.getOutputs().stream()
                .map(output -> new EmiHandpanIngredient(EmiStack.of(output.getItem()), output.getChance()))
                .toList();
        this.mesh = EmiStack.of(new ItemStack(RenewedItems.handpan, 1, recipe.getSubtype()));
        this.damage = recipe.getDamage();
        this.speed = recipe.getSpeed();
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return RenewedRecipeCategories.HANDPAN;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return new SyntheticIdentifier(this);
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(EmiIngredient.of(this.input.getEmiStacks()), EmiIngredient.of(this.mesh.getEmiStacks()));
    }

    @Override
    public List<EmiStack> getOutputs() {
        return this.outputs.stream().map(emiHandpanIngredient -> emiHandpanIngredient.item).toList();
    }

    @Override
    public int getDisplayWidth() {
        return 120;
    }

    @Override
    public int getDisplayHeight() {
        return 64;
    }

    @Override
    public void addWidgets(WidgetHolder widgetHolder) {
        // Draw slots and items
        SlotWidget input = widgetHolder.addSlot(this.input, getDisplayWidth() / 2 - 18, 3);
        SlotWidget mesh = widgetHolder.addSlot(this.mesh, getDisplayWidth() / 2 , 3);
        List<EmiHandpanIngredient> renderOutputs = this.outputs.stream()
                .sorted(Comparator.comparingDouble( value -> ((EmiHandpanIngredient) value).chance).reversed())
                .toList();
        String formatting = EnumChatFormatting.DARK_GRAY.toString() + EnumChatFormatting.ITALIC.toString();

        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 2; y++) {
                int ind = x + (5 * y);
                if (ind < renderOutputs.size()) {
                    SlotWidget widget = widgetHolder.addSlot(
                            renderOutputs.get(ind).item,
                            getDisplayWidth() / 2 - 8 + (18 * (x - 2)),
                            25 + (18 * y));
                    widget.appendTooltip(Text.translatable(formatting + "Chance: %d%%".formatted((int) (renderOutputs.get(ind).chance * 100F))));
                    continue;
                }
                widgetHolder.addSlot(getDisplayWidth() / 2 - 8 + (18 * (x - 2)), 25 + (18 * y));
            }
        }

        mesh.appendTooltip(Text.translatable(formatting + "Takes %d damage after process".formatted(this.damage)));
        input.appendTooltip(Text.translatable(formatting + "Takes %.1fs to process".formatted(this.speedToSeconds())));
    }

    private float speedToSeconds() {
        return this.speed / 20F;
    }

    public class EmiHandpanIngredient {
        public EmiStack item;
        public float chance;

        public EmiHandpanIngredient(EmiStack item, float chance) {
            this.item = item;
            this.chance = chance;
        }
    }
}
