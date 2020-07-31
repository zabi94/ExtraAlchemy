package zabi.minecraft.extraalchemy.items;

import net.minecraft.util.registry.Registry;
import zabi.minecraft.extraalchemy.utils.LibMod;

public class ModItems {

	public static EmptyVialItem EMPTY_VIAL = new EmptyVialItem();
	public static VialPotionItem POTION_VIAL = new VialPotionItem();
	public static PotionBagItem POTION_BAG = new PotionBagItem();
	
	public static void registerItems() {
		Registry.register(Registry.ITEM, LibMod.id("empty_vial"), EMPTY_VIAL);
		Registry.register(Registry.ITEM, LibMod.id("breakable_potion"), POTION_VIAL);
		Registry.register(Registry.ITEM, LibMod.id("potion_bag"), POTION_BAG);
	}
}
