package zabi.minecraft.extraalchemy.screen;

import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.registry.Registry;
import zabi.minecraft.extraalchemy.screen.potion_bag.PotionBagScreenHandler;
import zabi.minecraft.extraalchemy.utils.LibMod;

public class ModScreenHandlerTypes {
	public static ScreenHandlerType<PotionBagScreenHandler> POTION_BAG;
	
	public static void init() {
		POTION_BAG = new ScreenHandlerType<PotionBagScreenHandler>(PotionBagScreenHandler::new);
		Registry.register(Registry.SCREEN_HANDLER, LibMod.id("potion_bag"), POTION_BAG);
	}
	
}
