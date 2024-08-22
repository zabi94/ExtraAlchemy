package zabi.minecraft.extraalchemy;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.fabricmc.loader.api.FabricLoader;
import zabi.minecraft.extraalchemy.compat.inventorio.InventorioCompatBridge;
import zabi.minecraft.extraalchemy.compat.pehkui.PehkuiCompatBridge;
import zabi.minecraft.extraalchemy.compat.trinkets.TrinketsCompatBridge;
import zabi.minecraft.extraalchemy.config.ModConfig;
import zabi.minecraft.extraalchemy.crafting.CraftingRecipes;
import zabi.minecraft.extraalchemy.entitydata.ModEntityData;
import zabi.minecraft.extraalchemy.items.ItemSettings;
import zabi.minecraft.extraalchemy.items.ModComponents;
import zabi.minecraft.extraalchemy.items.ModItems;
import zabi.minecraft.extraalchemy.network.ClientToServerPackets;
import zabi.minecraft.extraalchemy.network.ServerPacketRegistry;
import zabi.minecraft.extraalchemy.network.ServerToClientPackets;
import zabi.minecraft.extraalchemy.potion.ModPotionRegistry;
import zabi.minecraft.extraalchemy.recipes.BrewingRecipes;
import zabi.minecraft.extraalchemy.screen.ModScreenHandlerTypes;
import zabi.minecraft.extraalchemy.statuseffect.ModEffectRegistry;
import zabi.minecraft.extraalchemy.utils.proxy.ServerProxy;

public class ExtraAlchemy implements ModInitializer {
	
	private static boolean ringModsInstalled = false;

	@Override
	public void onInitialize() {
		ModConfig.init();
		ModComponents.register();
		ModItems.registerItems();
		ModEffectRegistry.registerAll();
		ModPotionRegistry.registerAll();
		FabricBrewingRecipeRegistryBuilder.BUILD.register(BrewingRecipes::init);
		ServerToClientPackets.register();
		ClientToServerPackets.register();
		ServerPacketRegistry.init();
		CraftingRecipes.init();
		ModEntityData.init();
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
		ServerLifecycleEvents.SERVER_STARTING.register(server -> new ServerProxy(server).registerProxy());
		ItemSettings.init();
		
	}

	public static boolean areRingModsInstalled() {
		return ringModsInstalled;
	}

	public static void setRingModsInstalled() {
		ringModsInstalled = true;
	}
	
}
