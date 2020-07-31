package zabi.minecraft.extraalchemy;

import net.fabricmc.api.ModInitializer;
import zabi.minecraft.extraalchemy.config.ModConfig;
import zabi.minecraft.extraalchemy.crafting.CraftingRecipes;
import zabi.minecraft.extraalchemy.items.ModItems;
import zabi.minecraft.extraalchemy.network.ServerPacketRegistry;
import zabi.minecraft.extraalchemy.potion.ModPotionRegistry;
import zabi.minecraft.extraalchemy.recipes.BrewingRecipeRegistrar;
import zabi.minecraft.extraalchemy.statuseffect.ModEffectRegistry;

public class ExtraAlchemy implements ModInitializer {

	@Override
	public void onInitialize() {
		ModConfig.init();
		ModItems.registerItems();
		ModEffectRegistry.registerAll();
		ModPotionRegistry.registerAll();
		BrewingRecipeRegistrar.init();
		ServerPacketRegistry.init();
		CraftingRecipes.init();
	}

}
