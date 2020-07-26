package zabi.minecraft.extraalchemy.recipes;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import zabi.minecraft.extraalchemy.config.ModConfig;
import zabi.minecraft.extraalchemy.potion.ModPotion;
import zabi.minecraft.extraalchemy.potion.ModPotionRegistry;
import zabi.minecraft.extraalchemy.recipes.BrewingRecipeRegistrar.Registar;
import zabi.minecraft.extraalchemy.utils.DelayedConsumer;

public class BrewingRecipeRegistrar extends DelayedConsumer<Registar> {

	private static final BrewingRecipeRegistrar INSTANCE = new BrewingRecipeRegistrar();
	
	public static void init() {
		registerPotion(ModConfig.INSTANCE.potions.fuse, ModPotionRegistry.fuse, Items.FIREWORK_STAR, Potions.AWKWARD);
		registerPotion(ModConfig.INSTANCE.potions.crumbling, ModPotionRegistry.crumbling, Items.DIRT, Potions.AWKWARD);
		registerPotion(ModConfig.INSTANCE.potions.magnetism, ModPotionRegistry.magnetism, Items.IRON_INGOT, Potions.AWKWARD);
		registerPotion(ModConfig.INSTANCE.potions.photosynthesis, ModPotionRegistry.photosynthesis, Items.BEETROOT_SEEDS, Potions.AWKWARD);
		registerPotion(ModConfig.INSTANCE.potions.recall, ModPotionRegistry.recall, Items.ENDER_EYE, Potions.MUNDANE); //Use charged potion here
		registerPotion(ModConfig.INSTANCE.potions.sails, ModPotionRegistry.sails, Items.SALMON, Potions.AWKWARD);
		registerPotion(ModConfig.INSTANCE.potions.returning, ModPotionRegistry.returning, Items.PRISMARINE_SHARD, Potions.AWKWARD);
		registerPotion(ModConfig.INSTANCE.potions.learning, ModPotionRegistry.learning, Items.LAPIS_BLOCK, Potions.THICK);
		registerPotion(ModConfig.INSTANCE.potions.concentration, ModPotionRegistry.concentration, Items.EGG, Potions.AWKWARD);
		registerPotion(ModConfig.INSTANCE.potions.gravity, ModPotionRegistry.gravity, Items.NETHER_BRICK, Potions.THICK);
		registerPotion(ModConfig.INSTANCE.potions.combustion, ModPotionRegistry.combustion, Items.COAL_BLOCK, Potions.MUNDANE);
		registerPotion(ModConfig.INSTANCE.potions.pacifism, ModPotionRegistry.pacifism, Items.GOLDEN_APPLE, Potions.STRONG_HARMING);
	}
	
	private static void registerPotion(boolean active, ModPotion potion, Item ingredient, Potion base) {
		if (active) {
			INSTANCE.consumeWhenReady(reg -> {
				reg.register(base, ingredient, potion);
				if (potion.getEmpowered() != null) {
					reg.register(potion, Items.GLOWSTONE_DUST, potion.getEmpowered());
				}
				if (potion.getExtended() != null) {
					reg.register(potion, Items.REDSTONE, potion.getExtended());
				}
			});
		}
	}
	
	public static void onKeyReady(Registar r) {
		INSTANCE.keyReady(r);
	}
	
	@FunctionalInterface
	public static interface Registar {
		void register(Potion input, Item item, Potion output);
	}
}
