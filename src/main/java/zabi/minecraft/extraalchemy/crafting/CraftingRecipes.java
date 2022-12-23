package zabi.minecraft.extraalchemy.crafting;

import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import zabi.minecraft.extraalchemy.utils.LibMod;

public class CraftingRecipes {
	
	public static SpecialRecipeSerializer<PotionVialRecipe> FILL_VIAL_SERIALIZER;
	public static RecipeType<PotionVialRecipe> FILL_VIAL_RECIPE_TYPE;

	public static RecipeSerializer<PotionRingRecipe> RING_CRAFTING_SERIALIZER;
	public static RecipeType<PotionRingRecipe> RING_CRAFTING_TYPE;
	
	public static RecipeSerializer<AlternativePotionRingRecipe> ALT_RING_CRAFTING_SERIALIZER;
	public static RecipeType<AlternativePotionRingRecipe> ALT_RING_CRAFTING_TYPE;
	
	public static void init() {
		FILL_VIAL_SERIALIZER = Registry.register(Registries.RECIPE_SERIALIZER, LibMod.id("fill_potion_vial"), new SpecialRecipeSerializer<PotionVialRecipe>(PotionVialRecipe::new));
		FILL_VIAL_RECIPE_TYPE = Registry.register(Registries.RECIPE_TYPE, LibMod.id("fill_potion_vial"), new RecipeType<PotionVialRecipe>() {});

		RING_CRAFTING_SERIALIZER = Registry.register(Registries.RECIPE_SERIALIZER, LibMod.id("potion_ring"), new PotionRingRecipe.Serializer());
		RING_CRAFTING_TYPE = Registry.register(Registries.RECIPE_TYPE, LibMod.id("potion_ring"), new RecipeType<PotionRingRecipe>() {});

		ALT_RING_CRAFTING_SERIALIZER = Registry.register(Registries.RECIPE_SERIALIZER, LibMod.id("alt_potion_ring"), new AlternativePotionRingRecipe.Serializer());
		ALT_RING_CRAFTING_TYPE = Registry.register(Registries.RECIPE_TYPE, LibMod.id("alt_potion_ring"), new RecipeType<AlternativePotionRingRecipe>() {});
	}
	
}
