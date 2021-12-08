package zabi.minecraft.extraalchemy;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import zabi.minecraft.extraalchemy.compat.inventorio.InventorioCompatBridge;
import zabi.minecraft.extraalchemy.compat.pehkui.PehkuiCompatBridge;
import zabi.minecraft.extraalchemy.compat.trinkets.TrinketsCompatBridge;
import zabi.minecraft.extraalchemy.config.ModConfig;
import zabi.minecraft.extraalchemy.crafting.CraftingRecipes;
import zabi.minecraft.extraalchemy.items.ModItems;
import zabi.minecraft.extraalchemy.network.ServerPacketRegistry;
import zabi.minecraft.extraalchemy.potion.ModPotionRegistry;
import zabi.minecraft.extraalchemy.recipes.BrewingRecipeRegistrar;
import zabi.minecraft.extraalchemy.screen.ModScreenHandlerTypes;
import zabi.minecraft.extraalchemy.statuseffect.ModEffectRegistry;

public class ExtraAlchemy implements ModInitializer {
	
	private static boolean ringModsInstalled = false;

	@Override
	public void onInitialize() {
		ModConfig.init();
		if (FabricLoader.getInstance().isModLoaded("pehkui")) {
			PehkuiCompatBridge.preInit();
		}
		ModItems.registerItems();
		ModEffectRegistry.registerAll();
		ModPotionRegistry.registerAll();
//		PotionRingRecipe.generateDefaults();
		BrewingRecipeRegistrar.init();
		ServerPacketRegistry.init();
		CraftingRecipes.init();
		ModScreenHandlerTypes.init();
		if (FabricLoader.getInstance().isModLoaded("pehkui")) {
			PehkuiCompatBridge.init();
		}
		if (FabricLoader.getInstance().isModLoaded("trinkets")) {
			TrinketsCompatBridge.init();
		}
		if (FabricLoader.getInstance().isModLoaded("inventorio")) {
			InventorioCompatBridge.init();
		}
	}

	public static boolean areRingModsInstalled() {
		return ringModsInstalled;
	}

	public static void setRingModsInstalled() {
		ringModsInstalled = true;
	}

}
