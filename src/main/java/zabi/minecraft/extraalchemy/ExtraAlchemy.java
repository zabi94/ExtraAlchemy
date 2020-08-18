package zabi.minecraft.extraalchemy;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import zabi.minecraft.extraalchemy.config.ModConfig;
import zabi.minecraft.extraalchemy.crafting.CraftingRecipes;
import zabi.minecraft.extraalchemy.crafting.PotionRingRecipe;
import zabi.minecraft.extraalchemy.items.ModItems;
import zabi.minecraft.extraalchemy.network.ServerPacketRegistry;
import zabi.minecraft.extraalchemy.optional_compat.SizeEntityAttributesCompat;
import zabi.minecraft.extraalchemy.potion.ModPotionRegistry;
import zabi.minecraft.extraalchemy.recipes.BrewingRecipeRegistrar;
import zabi.minecraft.extraalchemy.statuseffect.ModEffectRegistry;
import zabi.minecraft.extraalchemy.utils.Log;

public class ExtraAlchemy implements ModInitializer {

	@Override
	public void onInitialize() {

		ModConfig.init();
		ModItems.registerItems();
		
		if (FabricLoader.getInstance().isModLoaded("sizeentityattributes")) {
			if (ModConfig.INSTANCE.forceDisableSEACompat) { 
				Log.i("Extra Alchemy module for Size Entity Attributes is disabled, but the mod is enabled");
			} else {
				Log.i("Extra Alchemy module for Size Entity Attributes is loading!");
				SizeEntityAttributesCompat.setup();
			}
		}
		
		ModEffectRegistry.registerAll();
		ModPotionRegistry.registerAll();
		PotionRingRecipe.generateDefaults();
		BrewingRecipeRegistrar.init();
		ServerPacketRegistry.init();
		CraftingRecipes.init();

	}

}
