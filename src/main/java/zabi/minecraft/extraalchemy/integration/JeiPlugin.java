package zabi.minecraft.extraalchemy.integration;

import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import zabi.minecraft.extraalchemy.ModConfig;
import zabi.minecraft.extraalchemy.items.ModItems;
import zabi.minecraft.extraalchemy.lib.Log;

@JEIPlugin
public class JeiPlugin implements IModPlugin {
	
	private static final String slime = "recipe.sticky.description";
	private static final String split_d = "recipe.split.drink.description";
	private static final String split_v = "recipe.split.vial.description";

	public void register(@Nonnull IModRegistry registry) {
		Log.d("Configuring JEI integration");
		
		registry.getJeiHelpers().getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModItems.supporter_medal));
		
		ArrayList<ItemStack> potion = getAllPotionsInsideItemstack(new ItemStack(Items.POTIONITEM));
		ArrayList<ItemStack> potion_s = getAllPotionsInsideItemstack(new ItemStack(Items.SPLASH_POTION));
		
		
		registry.addIngredientInfo(potion, ItemStack.class, split_d, "\n\n", slime);
		
		if (ModConfig.options.breakingPotions) {
			registry.addIngredientInfo(potion_s, ItemStack.class, split_v);
			ArrayList<ItemStack> potion_v = getAllPotionsInsideItemstack(new ItemStack(ModItems.breakable_potion));
			registry.addIngredientInfo(potion_v, ItemStack.class, split_v, "\n\n", slime);
		}
		
	}

	private ArrayList<ItemStack> getAllPotionsInsideItemstack(ItemStack is) {
		return Lists.newArrayList(
				PotionType.REGISTRY.getKeys().parallelStream()
				.map(k -> PotionType.REGISTRY.getObject(k))
				.map(pt -> PotionUtils.addPotionToItemStack(is.copy(), pt))
				.collect(Collectors.toList())
			);
	}
	
	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {}
	
	@Override
	public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {}

	@Override
	public void registerIngredients(IModIngredientRegistration registry) {}

}
