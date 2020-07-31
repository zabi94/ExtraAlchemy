package zabi.minecraft.extraalchemy.client;

import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry.Factory;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import zabi.minecraft.extraalchemy.screen.ModScreenHandlerTypes;
import zabi.minecraft.extraalchemy.screen.potion_bag.PotionBagScreen;
import zabi.minecraft.extraalchemy.screen.potion_bag.PotionBagScreenHandler;

public class ModScreens {
	
	public static void init() {
		ScreenRegistry.register(ModScreenHandlerTypes.POTION_BAG, new Factory<PotionBagScreenHandler, PotionBagScreen> () {
			@Override
			public PotionBagScreen create(PotionBagScreenHandler handler, PlayerInventory inventory, Text title) {
				return new PotionBagScreen(title, handler, inventory);
			}
		});
	}

}
