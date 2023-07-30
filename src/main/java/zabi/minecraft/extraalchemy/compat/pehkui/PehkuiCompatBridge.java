package zabi.minecraft.extraalchemy.compat.pehkui;

public class PehkuiCompatBridge {

	public static int registerEffects() {
		return PehkuiPotions.registerEffects();
	}
	
	public static int registerPotions() {
		return PehkuiPotions.registerPotions();
	}
	
	public static void registerRecipes() {
		PehkuiPotions.registerRecipes();
	}
	
	public static void init() {
		ModSizeModifiers.registerModifiers();
	}
	
	public static void preInit() {
		ScaleTypesAdapter.load();
	}
	
}
