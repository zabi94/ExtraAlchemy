package zabi.minecraft.extraalchemy.compat.pehkui;

public class PehkuiCompatBridge {

	public static int registerEffects() {
		return PehkuiPotions.registerEffects();
//		return 0;
	}
	
	public static int registerPotions() {
		return PehkuiPotions.registerPotions();
//		return 0;
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
