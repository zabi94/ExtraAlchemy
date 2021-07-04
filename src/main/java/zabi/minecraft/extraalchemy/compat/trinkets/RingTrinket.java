package zabi.minecraft.extraalchemy.compat.trinkets;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.Trinket;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import zabi.minecraft.extraalchemy.items.PotionRingItem;

public class RingTrinket implements Trinket {

	@Override
	public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
		if (!entity.world.isClient) {
			PotionRingItem.onTick(stack, entity);
		}
	}
	
	@Override
	public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
		//No op
	}
	
}
