package zabi.minecraft.extraalchemy.client;

import net.fabricmc.api.ClientModInitializer;
import zabi.minecraft.extraalchemy.client.input.KeybindDispatcher;
import zabi.minecraft.extraalchemy.client.network.ClientPacketRegistry;
import zabi.minecraft.extraalchemy.client.rendering.ColorsRegistration;
import zabi.minecraft.extraalchemy.client.screen.ModScreens;

public class ExtraAlchemyClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ClientPacketRegistry.init();
		KeybindDispatcher.initKeybinds();
		KeybindDispatcher.registerListeners();
		ColorsRegistration.init();
		ModScreens.init();
	}

}
