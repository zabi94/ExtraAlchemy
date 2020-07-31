package zabi.minecraft.extraalchemy.client;

import net.fabricmc.fabric.api.client.screen.ContainerScreenFactory;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.minecraft.client.gui.screen.ingame.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.TranslatableText;
import zabi.minecraft.extraalchemy.screen.ModContainerTypes;
import zabi.minecraft.extraalchemy.screen.potion_bag.PotionBagContainer;
import zabi.minecraft.extraalchemy.screen.potion_bag.PotionBagScreen;

public class ModScreens {
	public static void init() {
		ScreenProviderRegistry.INSTANCE.registerFactory(ModContainerTypes.POTION_BAG, new ContainerScreenFactory<PotionBagContainer>() {
			@Override
			public ContainerScreen<PotionBagContainer> create(PotionBagContainer container) {
				return new PotionBagScreen(new TranslatableText("item.extraalchemy.potion_bag"), container, (PlayerInventory) container.getPlayerInventory());
			}
		});
	}
}
