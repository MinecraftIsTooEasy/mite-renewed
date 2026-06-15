package com.github.jeffyjamzhd.renewed.compat.emi.recipe;

import com.github.jeffyjamzhd.renewed.item.recipe.ShapelessToolRecipe;
import dev.emi.emi.EmiPort;
import dev.emi.emi.EmiUtil;
import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.recipe.EmiPatternCraftingRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.GeneratedSlotWidget;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import dev.emi.emi.recipe.EmiShapelessRecipe;
import dev.emi.emi.screen.tooltip.EmiSecondaryOutputComponent;
import moddedmite.emi.MITEPlugin;
import net.minecraft.Item;
import net.minecraft.ItemStack;
import net.minecraft.Material;
import net.minecraft.Minecraft;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import shims.java.com.unascribed.retroemi.ItemStacks;
import shims.java.com.unascribed.retroemi.RetroEMI;
import shims.java.net.minecraft.client.gui.tooltip.TooltipComponent;
import shims.java.net.minecraft.text.MutableText;
import shims.java.net.minecraft.text.Text;
import shims.java.net.minecraft.util.Formatting;
import shims.java.net.minecraft.util.SyntheticIdentifier;

import java.util.*;

public class EmiShapelessToolRecipe<T extends Item> extends EmiCraftingRecipe {
    public static final HashMap<Class<Item>, ArrayList<Item>> CLASS_TO_ITEMS = new HashMap<>();
    private final int unique;
    private final Material craftLevel;
    private final float craftDifficulty;
    private final HashSet<Class<? extends Item>> exclude;
    private final Class<T> toolClass;

    public EmiShapelessToolRecipe(ShapelessToolRecipe<T> recipe) {
        super(
                Arrays.stream(recipe.getComponents()).map(RetroEMI::wildcardIngredient).toList(),
                EmiStack.of(recipe.getRecipeOutput()),
                new SyntheticIdentifier(recipe, "/tool"),
                true,
                null,
                recipe.getUnmodifiedDifficulty()
        );

        this.unique = EmiUtil.RANDOM.nextInt();
        this.craftDifficulty = recipe.getUnmodifiedDifficulty();
        this.craftLevel = recipe.getMaterialToCheckToolBenchHardnessAgainst();
        this.toolClass = recipe.toolClass;
        this.exclude = recipe.excludedClasses;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        ArrayList<EmiIngredient> ingredients = new ArrayList<>();
        ingredients.add(EmiIngredient.of(getCatalysts()));
        ingredients.addAll(input);
        return ingredients;
    }

    @Override
    public List<EmiIngredient> getCatalysts() {
        return getItemsFiltered().stream().map(t -> RetroEMI.wildcardIngredient(new ItemStack(t))).toList();
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        List<TooltipComponent> components = new ArrayList();
        int crafting_period = Minecraft.getMinecraft().thePlayer.getCraftingPeriod(this.craftDifficulty);
        String duration = DurationFormatUtils.formatDuration((long)crafting_period * 50L, "m:ss", true);
        components.add(TooltipComponent.of(EmiPort.translatable("emi.craft_time.items", new Object[]{duration})));
        Material level = this.getCraftLevel();
        if (level != null && level != Material.air) {
            if (level == Material.wood) {
                level = Material.flint;
            }

            MutableText text = EmiPort.literal(level.getLocalizedName());
            String translated = EmiPort.translatable("emi.craft_level.items", new Object[]{text}).asString();
            String[] set = WordUtils.wrap(translated, 24).split("\\r?\\n");
            components.addAll(Arrays.stream(set).map((sx) -> Formatting.GOLD + sx).map(EmiPort::literal).map(TooltipComponent::of).toList());
        } else {
            components.add(TooltipComponent.of(Text.translatable("emi.craft_level.none.items")));
        }

        widgets.addFillingArrow(60, 18, crafting_period * 50).tooltip(components);
        widgets.addTexture(EmiTexture.SHAPELESS, 97, 0);

        for (int i = 0; i < 9; ++i) {
            if (i == 0) {
                widgets.addGeneratedSlot(random -> EmiStack.of(pickRandomToolForDisplay(random)), this.unique, i % 3 * 18, i / 3 * 18);
                continue;
            }

            int s = i - 1;
            if (s < this.input.size()) {
                widgets.addSlot(this.input.get(s), i % 3 * 18, i / 3 * 18);
            } else {
                widgets.addSlot(EmiStack.of(ItemStacks.EMPTY), i % 3 * 18, i / 3 * 18);
            }
        }

        widgets.addSlot(this.output, 92, 14).large(true).recipeContext(this);
    }

    @Override
    public Material getCraftLevel() {
        return this.craftLevel;
    }

    private T pickRandomToolForDisplay(Random random) {
        List<T> items = getItemsFiltered();
        return items.get(random.nextInt(items.size()));
    }

    private List<T> getItemsFiltered() {
        List<T> items = getItems();
        for (Class<? extends Item> clazz : exclude) {
            items = items.stream().filter(t -> !clazz.isInstance(t)).toList();
        }
        return items;
    }

    @SuppressWarnings("unchecked")
    private List<T> getItems() {
        if (CLASS_TO_ITEMS.containsKey(toolClass)) {
            return (List<T>) CLASS_TO_ITEMS.get(toolClass);
        }

        ArrayList<T> items = new ArrayList<>();
        for (Item item : Item.itemsList) {
            if (toolClass.isInstance(item)) items.add((T) item);
        }
        CLASS_TO_ITEMS.put((Class<Item>) toolClass, (ArrayList<Item>) items);
        return items;
    }
}
