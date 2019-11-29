package zabi.minecraft.extraalchemy.client;

import net.fabricmc.api.ClientModInitializer;
import zabi.minecraft.extraalchemy.client.network.ClientPacketRegistry;

public class ClientDispatcher implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ClientPacketRegistry.init();
		KeybindDispatcher.initKeybinds();
		KeybindDispatcher.registerListeners();
	}

}
