package zabi.minecraft.extraalchemy.crafting;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.world.World;
import zabi.minecraft.extraalchemy.config.ModConfig;
import zabi.minecraft.extraalchemy.items.ModItems;
import zabi.minecraft.extraalchemy.utils.PotionUtilities;

public class PotionVialRecipe extends SpecialCraftingRecipe {
	
	public PotionVialRecipe(CraftingRecipeCategory category) {
		super(CraftingRecipeCategory.MISC);
	}
	
	@Override
	public boolean fits(int width, int height) {
		return width > 1 || height > 1;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return CraftingRecipes.FILL_VIAL_SERIALIZER;
	}

	@Override
	public boolean matches(CraftingRecipeInput input, World world) {
		if (!ModConfig.INSTANCE.enableVials) {
			return false;
		}
		
		boolean splash = false;
		boolean vial = false;
		
		for (int i = 0; i < input.getSize(); i++) {
			Item s = input.getStackInSlot(i).getItem();
			if (s.equals(Items.SPLASH_POTION)) {
				if (splash) {
					return false;
				} else {
					splash = true;
				}
			} else if (s.equals(ModItems.EMPTY_VIAL)) {
				if (vial) {
					return false;
				} else {
					vial = true;
				}
			} else if (!s.equals(Items.AIR)) {
				return false;
			}
		}
		return vial && splash;
	}

	@Override
	public ItemStack craft(CraftingRecipeInput input, WrapperLookup lookup) {
		for (int i = 0; i < input.getSize(); i++) {
			ItemStack is = input.getStackInSlot(i);
			if (is.getItem().equals(Items.SPLASH_POTION)) {
				ItemStack result = new ItemStack(ModItems.POTION_VIAL);
				PotionUtilities.cloneEffectsToStack(is, result);
				return result;
			}
		}
		return ItemStack.EMPTY;
	}
	
}
