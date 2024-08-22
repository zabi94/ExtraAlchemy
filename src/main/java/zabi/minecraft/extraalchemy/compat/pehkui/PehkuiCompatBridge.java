package zabi.minecraft.extraalchemy.compat.pehkui;

import net.minecraft.recipe.BrewingRecipeRegistry.Builder;

public class PehkuiCompatBridge {

	public static int registerEffects() {
		return PehkuiPotions.registerEffects();
	}
	
	public static int registerPotions() {
		return PehkuiPotions.registerPotions();
	}
	
	public static void registerRecipes(Builder builder) {
		PehkuiPotions.registerRecipes(builder);
	}
	
	public static void init() {
		ModSizeModifiers.registerModifiers();
	}
	
}
