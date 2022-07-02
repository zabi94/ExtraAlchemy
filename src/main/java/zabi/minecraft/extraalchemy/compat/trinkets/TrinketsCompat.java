package zabi.minecraft.extraalchemy.compat.trinkets;

import com.google.common.collect.Lists;

import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.player.PlayerEntity;
import zabi.minecraft.extraalchemy.ExtraAlchemy;
import zabi.minecraft.extraalchemy.items.ModItems;
import zabi.minecraft.extraalchemy.items.PotionRingItem;

public class TrinketsCompat {
	
	public static void init() {
		ExtraAlchemy.setRingModsInstalled();
		TrinketsApi.registerTrinket(ModItems.POTION_RING, new RingTrinket());
	}

	public static boolean toggleRings(PlayerEntity player) {
		var trinketList = TrinketsApi.getTrinketComponent(player).map(tc -> tc.getEquipped(ModItems.POTION_RING)).orElseGet(Lists::newArrayList);
		for (var pair: trinketList) {
			PotionRingItem.toggleRingStack(pair.getRight());
		}
		return !trinketList.isEmpty();
	}

}
