package zabi.minecraft.extraalchemy.client.screen;

import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.gui.screen.ingame.HandledScreens.Provider;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import zabi.minecraft.extraalchemy.screen.ModScreenHandlerTypes;
import zabi.minecraft.extraalchemy.screen.potion_bag.PotionBagScreen;
import zabi.minecraft.extraalchemy.screen.potion_bag.PotionBagScreenHandler;

public class ModScreens {
	
	public static void init() {
		HandledScreens.register(ModScreenHandlerTypes.POTION_BAG, new Provider<PotionBagScreenHandler, PotionBagScreen>() {

			@Override
			public PotionBagScreen create(PotionBagScreenHandler handler, PlayerInventory inventory, Text title) {
				return new PotionBagScreen(title, handler, inventory);
			}
		});
	}

}
