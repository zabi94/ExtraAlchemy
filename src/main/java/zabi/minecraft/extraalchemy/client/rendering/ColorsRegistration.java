package zabi.minecraft.extraalchemy.client.rendering;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import zabi.minecraft.extraalchemy.items.ModComponents;
import zabi.minecraft.extraalchemy.items.ModItems;
import zabi.minecraft.extraalchemy.items.PotionBagItem;
import zabi.minecraft.extraalchemy.items.PotionRingItem.PotionRingData;
import zabi.minecraft.extraalchemy.utils.Log;

public class ColorsRegistration {

	public static void init() {
		ColorProviderRegistry.ITEM.register(ColorsRegistration::potionColor , ModItems.POTION_VIAL);
		ColorProviderRegistry.ITEM.register(PotionBagItem::getColor, ModItems.POTION_BAG);
		ColorProviderRegistry.ITEM.register(ColorsRegistration::ringColor, ModItems.POTION_RING);
	}
	
	private static int potionColor(ItemStack stack, int tintIndex) {
		if (tintIndex != 0) return -1;
		PotionContentsComponent pcc = stack.get(DataComponentTypes.POTION_CONTENTS); 
		if (pcc != null) return pcc.getColor();
		return -1;
	}
	
	private static int ringColor(ItemStack stack, int tintIndex) {
		if (tintIndex != 0) return -1;
		PotionRingData prd = stack.getOrDefault(ModComponents.POTION_RING_DATA, PotionRingData.EMPTY);
		if (prd.effect().isEmpty()) {
			Log.w("Ring has empty effect!");
			return -1;
		}
		return 0xee000000 | prd.effect().get().getColor();
		
	}
	
}
