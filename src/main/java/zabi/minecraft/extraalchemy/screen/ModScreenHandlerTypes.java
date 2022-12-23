package zabi.minecraft.extraalchemy.screen;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import zabi.minecraft.extraalchemy.screen.potion_bag.PotionBagScreenHandler;
import zabi.minecraft.extraalchemy.utils.LibMod;

public class ModScreenHandlerTypes {
	public static ScreenHandlerType<PotionBagScreenHandler> POTION_BAG;
	
	public static void init() {
		POTION_BAG = new ScreenHandlerType<PotionBagScreenHandler>(PotionBagScreenHandler::new);
		Registry.register(Registries.SCREEN_HANDLER, LibMod.id("potion_bag"), POTION_BAG);
	}
	
}
