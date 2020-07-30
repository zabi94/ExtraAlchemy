package zabi.minecraft.extraalchemy.client;

import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import zabi.minecraft.extraalchemy.screen.ModScreenHandlerTypes;
import zabi.minecraft.extraalchemy.screen.potion_bag.PotionBagScreen;

public class ModScreens {
	
	public static void init() {
		ScreenRegistry.register(ModScreenHandlerTypes.POTION_BAG, (sh, pinv, titolo) -> new PotionBagScreen(titolo, sh, pinv));
	}

}
