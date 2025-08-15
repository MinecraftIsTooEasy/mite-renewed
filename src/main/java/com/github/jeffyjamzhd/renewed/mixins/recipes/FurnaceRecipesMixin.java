package com.github.jeffyjamzhd.renewed.mixins.recipes;

import com.github.jeffyjamzhd.renewed.api.IFurnaceRecipes;
import com.github.jeffyjamzhd.renewed.api.recipe.FurnaceEntry;
import net.minecraft.FurnaceRecipes;
import net.minecraft.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mixin(FurnaceRecipes.class)
public class FurnaceRecipesMixin implements IFurnaceRecipes {
    @Unique private List<FurnaceEntry> mr$smeltingListComplex = new ArrayList<>();
    @Unique private Consumer<FurnaceEntry> mr$register =
            furnaceEntry -> mr$smeltingListComplex.add(furnaceEntry);

    @Override
    public List<FurnaceEntry> mr$getComplexEntries() {
        return mr$smeltingListComplex;
    }

    @Override
    public void mr$addSmeltingComplexEntry(ItemStack input, ItemStack output) {
        mr$register.accept(new FurnaceEntry(input, output));
    }

    @Override
    public FurnaceEntry mr$getComplexEntry(ItemStack input, boolean ignoreInputCount) {
        for (FurnaceEntry entry : mr$smeltingListComplex) {
            if (entry.input().itemID == input.itemID) {
                ItemStack target = entry.input();
                boolean sameSubtype = input.getItemSubtype() == target.getItemSubtype();
                boolean sameCount = ignoreInputCount || input.stackSize >= target.stackSize;

                if (sameSubtype && sameCount)
                    return entry;
            }
        }
        return null;
    }

    @Inject(method = "getSmeltingResult", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
    public void complexRecipeCheck1(
            ItemStack input, int heat, CallbackInfoReturnable<ItemStack> cir
    ) {
        FurnaceEntry entry = mr$getComplexEntry(input, false);
        if (entry != null)
            cir.setReturnValue(entry.output().copy());
    }

    @Inject(method = "getSmeltingResult", at = @At(value = "RETURN", ordinal = 2), cancellable = true)
    public void complexRecipeCheck2(
            ItemStack input, int heat, CallbackInfoReturnable<ItemStack> cir
            ) {
        FurnaceEntry entry = mr$getComplexEntry(input, false);
        if (entry != null)
            cir.setReturnValue(entry.output().copy());
    }

    @Inject(method = "doesSmeltingRecipeExistFor", at = @At("RETURN"), cancellable = true)
    public void doesComplexRecipeExist(ItemStack input, CallbackInfoReturnable<Boolean> cir) {
        FurnaceEntry entry = mr$getComplexEntry(input, true);
        if (entry != null)
            cir.setReturnValue(true);
    }

}
