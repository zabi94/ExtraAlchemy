package zabi.minecraft.extraalchemy.recipes.brew;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.common.brewing.BrewingRecipe;
import zabi.minecraft.extraalchemy.potion.ModPotionHelper;

public class BrewRecipe extends BrewingRecipe {

	public BrewRecipe(PotionType input, ItemStack ingredient, PotionType output, Item typeIn, Item typeOut) {
		super(ModPotionHelper.getItemStackOfPotion(typeIn, input), ingredient, ModPotionHelper.getItemStackOfPotion(typeOut, output));
	}
	
	public BrewRecipe(PotionType input, ItemStack ingredient, PotionType output) {
		super(ModPotionHelper.getItemStackOfPotion(Items.POTIONITEM, input), ingredient, ModPotionHelper.getItemStackOfPotion(Items.POTIONITEM, output));
	}

	public BrewRecipe(PotionType input, ItemStack ingredient, ItemStack output) {
		super(ModPotionHelper.getItemStackOfPotion(Items.POTIONITEM, input), ingredient, output);
	}

	public BrewRecipe(ItemStack input, ItemStack ingredient, ItemStack output) {
		super(input, ingredient, output);
	}
	
	@Override
	public boolean isInput(ItemStack stack) {
		return !stack.isEmpty() && stack.getItem().equals(this.getInput().getItem()) && PotionUtils.getPotionFromItem(stack).equals(PotionUtils.getPotionFromItem(getInput()));
	}

}
