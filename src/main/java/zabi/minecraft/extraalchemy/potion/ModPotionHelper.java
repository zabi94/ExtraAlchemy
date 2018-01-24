package zabi.minecraft.extraalchemy.potion;

import javax.annotation.Nonnull;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;

public class ModPotionHelper {

	private static ItemStack splashEmpty = new ItemStack(Items.SPLASH_POTION);
	private static ItemStack lingeringEmpty = new ItemStack(Items.LINGERING_POTION);
	private static ItemStack arrowEmpty = new ItemStack(Items.TIPPED_ARROW);
	
	static {
		ModPotionHelper.splashEmpty.setTagCompound(new NBTTagCompound());
		ModPotionHelper.lingeringEmpty.setTagCompound(new NBTTagCompound());
		ModPotionHelper.arrowEmpty.setTagCompound(new NBTTagCompound());
	}

	@Nonnull
	public static ItemStack transformToSplash(@Nonnull ItemStack stack) {
		ItemStack res = splashEmpty.copy();
		res.getTagCompound().setTag("Potion", stack.getTagCompound().getTag("Potion"));
		return res;
	}

	@Nonnull
	public static ItemStack transformToLingering(@Nonnull ItemStack stack) {
		ItemStack res = lingeringEmpty.copy();
		res.getTagCompound().setTag("Potion", stack.getTagCompound().getTag("Potion"));
		return res;
	}

	@Nonnull
	public static ItemStack transformToArrow(@Nonnull ItemStack stack) {
		ItemStack res = arrowEmpty.copy();
		res.getTagCompound().setTag("Potion", stack.getTagCompound().getTag("Potion"));
		return res;
	}

	public static ItemStack getItemStackOfPotion(Item it, PotionType pt) {
		ItemStack res = new ItemStack(it);
		res.setTagCompound(new NBTTagCompound());
		PotionUtils.addPotionToItemStack(res, pt);
		return res;
	}

}
