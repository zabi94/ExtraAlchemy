package zabi.minecraft.extraalchemy.crafting;

import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.registry.Registry;
import zabi.minecraft.extraalchemy.utils.LibMod;

public class CraftingRecipes {
	
	public static SpecialRecipeSerializer<PotionVialRecipe> FILL_VIAL_SERIALIZER;
	public static RecipeType<PotionVialRecipe> FILL_VIAL_RECIPE_TYPE;
	
	public static void init() {
		FILL_VIAL_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER, LibMod.id("fill_potion_vial"), new SpecialRecipeSerializer<PotionVialRecipe>(PotionVialRecipe::new));
		FILL_VIAL_RECIPE_TYPE = Registry.register(Registry.RECIPE_TYPE, LibMod.id("fill_potion_vial"), new RecipeType<PotionVialRecipe>() {});
	}
	
}
