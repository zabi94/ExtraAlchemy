package zabi.minecraft.extraalchemy.client.tooltip;

import net.minecraft.client.item.TooltipData;
import net.minecraft.item.ItemStack;

public class PotionTooltipData implements TooltipData {
	
	private final ItemStack stack;
	
	public PotionTooltipData(ItemStack stack) {
		this.stack = stack;
	}
	
	public ItemStack getStack() {
		return stack;
	}

}
