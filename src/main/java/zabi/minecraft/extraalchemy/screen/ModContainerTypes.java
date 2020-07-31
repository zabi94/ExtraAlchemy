package zabi.minecraft.extraalchemy.screen;

import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import zabi.minecraft.extraalchemy.screen.potion_bag.PotionBagContainer;
import zabi.minecraft.extraalchemy.utils.LibMod;

public class ModContainerTypes {
	
	public static final Identifier POTION_BAG = LibMod.id("potion_bag");
	
	public static void init() {
		ContainerProviderRegistry.INSTANCE.registerFactory(POTION_BAG, (syncId, factoryId, player, buffer) -> {
			Hand hand = buffer.readBoolean() ? Hand.MAIN_HAND:Hand.OFF_HAND;
			return new PotionBagContainer(syncId, player.inventory, player, hand);
		});
	}
}
