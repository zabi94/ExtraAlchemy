package zabi.minecraft.extraalchemy.items;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import zabi.minecraft.extraalchemy.utils.LibMod;

public class ModItems {

	public static Item EMPTY_VIAL = new EmptyVialItem();
	public static Item POTION_VIAL = new VialPotionItem();
	
	public static void registerItems() {
		Registry.register(Registry.ITEM, new Identifier(LibMod.MOD_ID, "empty_vial"), EMPTY_VIAL);
		Registry.register(Registry.ITEM, new Identifier(LibMod.MOD_ID, "breakable_potion"), POTION_VIAL);
	}
}
