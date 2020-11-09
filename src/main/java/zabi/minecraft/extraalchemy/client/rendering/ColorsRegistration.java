package zabi.minecraft.extraalchemy.client.rendering;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;
import zabi.minecraft.extraalchemy.items.ModItems;

public class ColorsRegistration {

	public static void init() {
		ColorProviderRegistry.ITEM.register(ColorsRegistration::potionColor , ModItems.POTION_VIAL, ModItems.POTION_RING);
		ColorProviderRegistry.ITEM.register(ColorsRegistration::bagColor, ModItems.POTION_BAG);
	}
	
	private static int potionColor(ItemStack stack, int tintIndex) {
		return tintIndex == 0 ? PotionUtil.getColor(PotionUtil.getPotionEffects(stack)) : -1;
	}
	
	private static int bagColor(ItemStack stack, int index) {
		if (index > 0) return -1;
		return ((DyeableItem) stack.getItem()).getColor(stack);
	}
	
}
