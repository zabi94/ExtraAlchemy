package zabi.minecraft.extraalchemy.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import zabi.minecraft.extraalchemy.lib.Reference;

public class TabExtraAlchemy extends CreativeTabs {

	private ItemStack ICON_STACK;
	
	public TabExtraAlchemy() {
		super(Reference.MID);
	}

	@Override
	public ItemStack createIcon() {
		ICON_STACK = new ItemStack(ModItems.potion_bag);
		return ICON_STACK;
	}
	
	@Override
	public ItemStack getIcon() {
		return ICON_STACK;
	}
	
}
