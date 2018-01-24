package zabi.minecraft.extraalchemy.items;

import java.util.ArrayList;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.util.NonNullList;
import zabi.minecraft.extraalchemy.lib.Config;
import zabi.minecraft.extraalchemy.lib.Reference;
import zabi.minecraft.extraalchemy.potion.ModPotionHelper;
import zabi.minecraft.extraalchemy.potion.PotionTypeBase;

public class TabExtraAlchemy extends CreativeTabs {

	private static ItemStack ICON_STACK;
	
	public TabExtraAlchemy() {
		super(Reference.MID);
		ICON_STACK = new ItemStack(ModItems.potion_bag);
	}

	@Override
	public ItemStack getIconItemStack() {
		return ICON_STACK;
	}
	
	@Override
	public void displayAllRelevantItems(NonNullList<ItemStack> list) {
		ArrayList<ItemStack> added_list = new ArrayList<ItemStack>();
		try {
			for (PotionType t:PotionType.REGISTRY) {
				if (t instanceof PotionTypeBase) {
					ItemStack is = ModPotionHelper.getItemStackOfPotion(Items.POTIONITEM, t);
					added_list.add(is);
					list.add(is);
				}
			}
			for (ItemStack s:added_list) {
				list.add(ModPotionHelper.transformToSplash(s));
			}
			for (ItemStack s:added_list) {
				list.add(ModPotionHelper.transformToLingering(s));
			}
			for (ItemStack s:added_list) {
				list.add(ModPotionHelper.transformToArrow(s));
			}
			if (Config.enable_potion_bag.getBoolean()) list.add(new ItemStack(ModItems.potion_bag));
			if (Config.breakingPotions.getBoolean()) list.add(new ItemStack(ModItems.vial_break));
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.displayAllRelevantItems(list);
	}


	@Override
	public ItemStack getTabIconItem() {
		return ICON_STACK;
	}
	

}
