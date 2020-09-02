package zabi.minecraft.extraalchemy.screen;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import zabi.minecraft.extraalchemy.screen.potion_bag.PotionBagScreenHandler;
import zabi.minecraft.extraalchemy.utils.LibMod;

public class ModScreenHandlerTypes {
	public static ScreenHandlerType<PotionBagScreenHandler> POTION_BAG;
	
	public static void init() {
		POTION_BAG = ScreenHandlerRegistry.registerSimple(LibMod.id("potion_bag"), PotionBagScreenHandler::new);
	}
	
}
