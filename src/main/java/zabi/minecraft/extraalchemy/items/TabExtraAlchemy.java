package zabi.minecraft.extraalchemy.items;

import java.util.ArrayList;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
import zabi.minecraft.extraalchemy.ModConfig;
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
		if (ModConfig.options.enable_potion_bag) list.add(new ItemStack(ModItems.potion_bag));
		if (ModConfig.options.breakingPotions) list.add(new ItemStack(ModItems.vial_break));
		try {
			ArrayList<ItemStack> normal = new ArrayList<ItemStack>();
			ArrayList<ItemStack> splash = new ArrayList<ItemStack>();
			ArrayList<ItemStack> linger = new ArrayList<ItemStack>();
			ArrayList<ItemStack> arrow = new ArrayList<ItemStack>();
			ArrayList<ItemStack> vial = new ArrayList<ItemStack>();
			
			for (PotionType t:PotionType.REGISTRY) {
				if (t instanceof PotionTypeBase) {
					normal.add(PotionUtils.addPotionToItemStack(ModPotionHelper.normalEmpty.copy(), t));
					splash.add(PotionUtils.addPotionToItemStack(ModPotionHelper.splashEmpty.copy(), t));
					linger.add(PotionUtils.addPotionToItemStack(ModPotionHelper.lingeringEmpty.copy(), t));
					arrow.add(PotionUtils.addPotionToItemStack(ModPotionHelper.arrowEmpty.copy(), t));
					if (ModConfig.options.breakingPotions) {
						ItemStack stack = PotionUtils.addPotionToItemStack(ModPotionHelper.vialEmpty.copy(), t);
						PotionUtils.appendEffects(stack, t.getEffects());
						vial.add(stack);
					}
				}
			}
			
			list.addAll(normal);
			list.addAll(vial);
			list.addAll(splash);
			list.addAll(linger);
			list.addAll(arrow);
			
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
