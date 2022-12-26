package zabi.minecraft.extraalchemy.items;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import zabi.minecraft.extraalchemy.utils.LibMod;

public class ModItems {

	public static EmptyVialItem EMPTY_VIAL = new EmptyVialItem();
	public static VialPotionItem POTION_VIAL = new VialPotionItem();
	public static PotionBagItem POTION_BAG = new PotionBagItem();
	public static EmptyRingItem EMPTY_RING = new EmptyRingItem();
	public static PotionRingItem POTION_RING = new PotionRingItem();
	
	public static void registerItems() {
		Registry.register(Registries.ITEM, LibMod.id("potion_bag"), POTION_BAG);
		Registry.register(Registries.ITEM, LibMod.id("empty_ring"), EMPTY_RING);
		Registry.register(Registries.ITEM, LibMod.id("empty_vial"), EMPTY_VIAL);
		Registry.register(Registries.ITEM, LibMod.id("potion_ring"), POTION_RING);
		Registry.register(Registries.ITEM, LibMod.id("breakable_potion"), POTION_VIAL);
	}
}
