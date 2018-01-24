package zabi.minecraft.extraalchemy.recipes.brew;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.brewing.PotionBrewEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import zabi.minecraft.extraalchemy.lib.Log;

public class BrewingStandBlocker {
	@SubscribeEvent
	public void onPotionBrewed(PotionBrewEvent.Pre evt) {
		evt.setCanceled(
				shouldBeBlocked(evt.getItem(0)) ||
				shouldBeBlocked(evt.getItem(1)) ||
				shouldBeBlocked(evt.getItem(2))
				);
	}
	
	
	private boolean shouldBeBlocked(ItemStack i) {
		if (i.isEmpty()) return false;
		if (i.getTagCompound()==null) return false;
		if (i.getTagCompound().hasKey("alteredPotion")) {
			Log.d("Blocking crafting recipe");
			return true;
		}
		return false;
	}
}
