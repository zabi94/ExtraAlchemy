package zabi.minecraft.extraalchemy.recipes.brew;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import zabi.minecraft.extraalchemy.potion.ModPotionHelper;

public class RecipeManager {
	
	private static final ItemStack gunpowder = new ItemStack(Items.GUNPOWDER);
	private static final ItemStack dragonBreath = new ItemStack(Items.DRAGON_BREATH);
	private static final ItemStack glowstone = new ItemStack(Items.GLOWSTONE_DUST);
	private static final ItemStack redstone = new ItemStack(Items.REDSTONE);
	
	public static void registerRecipe(PotionType in, PotionType out, ItemStack ingredient) {
		
		ItemStack splash = ModPotionHelper.transformToSplash(ModPotionHelper.getItemStackOfPotion(Items.SPLASH_POTION, out));
		ItemStack lingering = ModPotionHelper.transformToLingering(ModPotionHelper.getItemStackOfPotion(Items.LINGERING_POTION, out));
		
		BrewingRecipeRegistry.addRecipe(new BrewRecipe(in, ingredient, out));
		BrewingRecipeRegistry.addRecipe(new BrewRecipe(out, gunpowder, splash));
		BrewingRecipeRegistry.addRecipe(new BrewRecipe(splash, dragonBreath, lingering));
	}
	
	public static void registerRecipe(PotionType in, PotionType out, Item ingredient) {
		registerRecipe(in, out, new ItemStack(ingredient));
	}
	
	public static void registerRecipeWithVariant(PotionType in, ItemStack ingredient, PotionType normal, PotionType longer, PotionType stronger) {
		registerRecipe(in, normal, ingredient);
		registerRecipe(normal, longer, redstone);
		registerRecipe(normal, stronger, glowstone);
	}
}
