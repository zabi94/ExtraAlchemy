package zabi.minecraft.extraalchemy.integration;

import javax.annotation.Nonnull;

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
import zabi.minecraft.extraalchemy.potion.ModPotionHelper;

@JEIPlugin
public class JeiPlugin implements IModPlugin {
	
	public static boolean isJeiInstalled = false;
	
	private static String[] descriptionStickyList = new String[] {
			"sticky.description.title","sticky.description.description"
	};
	
	private static String[] descriptionSplitList = new String[] {
			"split.description.title","split.description.description"
	};

	public void register(@Nonnull IModRegistry registry) {
		Log.d("Configuring JEI integration");
		isJeiInstalled = true;
		
		registry.getJeiHelpers().getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModItems.supporter_medal));
		
		if (ModConfig.options.allowPotionCombining) {
			Log.d("Adding Sticky Potion description");
			PotionType.REGISTRY.forEach(pt -> addStickyDescription(pt, registry));
		}
		
		if (ModConfig.options.allowPotionSplitting) {
			Log.d("Adding Splitting Potion description");
			PotionType.REGISTRY.forEach(pt -> addSplitDescription(pt, registry));
		}
	}

	private void addSplitDescription(PotionType pt, IModRegistry registry) {
		if (pt==null || pt.hasInstantEffect() || pt.getEffects().isEmpty()) return;
		ItemStack s = new ItemStack(Items.POTIONITEM);
		PotionUtils.addPotionToItemStack(s, pt);
		registry.addIngredientInfo(s, ItemStack.class, descriptionSplitList);
		registry.addIngredientInfo(ModPotionHelper.transformToLingering(s), ItemStack.class, descriptionSplitList);
		registry.addIngredientInfo(ModPotionHelper.transformToSplash(s), ItemStack.class, descriptionSplitList);
	}

	private void addStickyDescription(PotionType pt, IModRegistry registry) {
		if (pt==null || pt.hasInstantEffect() || pt.getEffects().isEmpty()) return;
		ItemStack s = new ItemStack(Items.POTIONITEM);
		PotionUtils.addPotionToItemStack(s, pt);
		registry.addIngredientInfo(s, ItemStack.class, descriptionStickyList);
		registry.addIngredientInfo(ModPotionHelper.transformToArrow(s), ItemStack.class, descriptionStickyList);
		registry.addIngredientInfo(ModPotionHelper.transformToLingering(s), ItemStack.class, descriptionStickyList);
		registry.addIngredientInfo(ModPotionHelper.transformToSplash(s), ItemStack.class, descriptionStickyList);
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {}
	
	@Override
	public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {}

	@Override
	public void registerIngredients(IModIngredientRegistration registry) {}

}
