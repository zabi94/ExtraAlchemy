package zabi.minecraft.extraalchemy.client;

import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.InputUtil.Type;
import net.minecraft.util.Identifier;
import zabi.minecraft.extraalchemy.client.network.ToggleMagnetismPacket;
import zabi.minecraft.extraalchemy.entitydata.PlayerProperties;
import zabi.minecraft.extraalchemy.utils.LibMod;

public class KeybindDispatcher {
	
	public static final FabricKeyBinding MAGNETISM_TOGGLE = FabricKeyBinding.Builder.create(new Identifier(LibMod.MOD_ID, "magnetism_toggle"), Type.KEYSYM, InputUtil.fromName("key.keyboard.n").getKeyCode(), LibMod.MOD_NAME).build();
	
	private static boolean wasMagnetismPressedLastTick = false; 
	
	public static void initKeybinds() {
		KeyBindingRegistry.INSTANCE.addCategory(LibMod.MOD_NAME);
		KeyBindingRegistry.INSTANCE.register(MAGNETISM_TOGGLE);
	}
	
	public static void registerListeners() {
		ClientTickCallback.EVENT.register(evt -> {
			if (MAGNETISM_TOGGLE.isPressed()) {
				if (!wasMagnetismPressedLastTick) {
					@SuppressWarnings("resource")
					PlayerProperties pp = (PlayerProperties) (Object) MinecraftClient.getInstance().player;
					boolean newMagnetismStatus = !pp.isMagnetismEnabled();
					pp.setMagnetismEnabled(newMagnetismStatus);
					ClientSidePacketRegistry.INSTANCE.sendToServer(ToggleMagnetismPacket.ID, ToggleMagnetismPacket.anew(newMagnetismStatus));
				}
				wasMagnetismPressedLastTick = true;
			} else {
				wasMagnetismPressedLastTick = false;
			}
		});
	}
}
