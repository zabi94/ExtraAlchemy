package zabi.minecraft.extraalchemy.recipes;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry.Builder;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import zabi.minecraft.extraalchemy.compat.pehkui.PehkuiCompatBridge;
import zabi.minecraft.extraalchemy.config.ModConfig;
import zabi.minecraft.extraalchemy.potion.ModPotion;
import zabi.minecraft.extraalchemy.potion.ModPotionRegistry;

public class BrewingRecipes {

	public static void init(Builder builder) {
		registerPotion(builder, ModConfig.INSTANCE.potions.fuse, ModPotionRegistry.fuse, Items.FIREWORK_STAR, Potions.AWKWARD);
		registerPotion(builder, ModConfig.INSTANCE.potions.crumbling, ModPotionRegistry.crumbling, Items.DIRT, Potions.AWKWARD);
		registerPotion(builder, ModConfig.INSTANCE.potions.magnetism, ModPotionRegistry.magnetism, Items.IRON_INGOT, Potions.AWKWARD);
		registerPotion(builder, ModConfig.INSTANCE.potions.photosynthesis, ModPotionRegistry.photosynthesis, Items.BEETROOT_SEEDS, Potions.AWKWARD);
		registerPotion(builder, ModConfig.INSTANCE.potions.recall, ModPotionRegistry.recall, Items.ENDER_EYE, Potions.MUNDANE); //Use charged potion here
		registerPotion(builder, ModConfig.INSTANCE.potions.sails, ModPotionRegistry.sails, Items.SALMON, Potions.AWKWARD);
		registerPotion(builder, ModConfig.INSTANCE.potions.returning, ModPotionRegistry.returning, Items.PRISMARINE_SHARD, Potions.AWKWARD);
		registerPotion(builder, ModConfig.INSTANCE.potions.learning, ModPotionRegistry.learning, Items.LAPIS_BLOCK, Potions.THICK);
		registerPotion(builder, ModConfig.INSTANCE.potions.concentration, ModPotionRegistry.concentration, Items.EGG, Potions.AWKWARD);
		registerPotion(builder, ModConfig.INSTANCE.potions.gravity, ModPotionRegistry.gravity, Items.NETHER_BRICK, Potions.THICK);
		registerPotion(builder, ModConfig.INSTANCE.potions.combustion, ModPotionRegistry.combustion, Items.COAL_BLOCK, Potions.MUNDANE);
		registerPotion(builder, ModConfig.INSTANCE.potions.pacifism, ModPotionRegistry.pacifism, Items.GOLDEN_APPLE, Potions.STRONG_HARMING);
		registerPotion(builder, ModConfig.INSTANCE.potions.detection, ModPotionRegistry.detection, Items.KELP, Potions.MUNDANE); 
		registerPotion(builder, ModConfig.INSTANCE.potions.piper, ModPotionRegistry.piper, Items.WHEAT, Potions.AWKWARD);
		
		if (FabricLoader.getInstance().isModLoaded("pehkui")) {
			PehkuiCompatBridge.registerRecipes(builder);
		}
		
	}
	
	public static void registerPotion(Builder builder, boolean active, ModPotion potion, Item ingredient, RegistryEntry<Potion> base) {
		RegistryEntry<Potion> potionKey = Registries.POTION.getEntry(potion);
		if (active) {
			builder.registerPotionRecipe(base, ingredient, potionKey);
			if (potion.getEmpowered() != null) {
				builder.registerPotionRecipe(potionKey, Ingredient.ofItems(Items.GLOWSTONE_DUST), Registries.POTION.getEntry(potion.getEmpowered()));
			}
			if (potion.getExtended() != null) {
				builder.registerPotionRecipe(potionKey, Ingredient.ofItems(Items.REDSTONE), Registries.POTION.getEntry(potion.getExtended()));
			}
		}
	}
	
}
