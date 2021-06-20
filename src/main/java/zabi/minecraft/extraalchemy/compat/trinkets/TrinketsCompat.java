package zabi.minecraft.extraalchemy.compat.trinkets;

import dev.emi.trinkets.api.TrinketsApi;
import zabi.minecraft.extraalchemy.ExtraAlchemy;
import zabi.minecraft.extraalchemy.items.ModItems;

public class TrinketsCompat {
	
	public static void init() {
		ExtraAlchemy.setRingModsInstalled();
		TrinketsApi.registerTrinket(ModItems.POTION_RING, new RingTrinket());
	}

}
