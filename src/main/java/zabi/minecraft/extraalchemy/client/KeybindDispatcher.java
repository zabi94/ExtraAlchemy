package zabi.minecraft.extraalchemy.client;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.InputUtil.Type;
import zabi.minecraft.extraalchemy.client.network.ToggleMagnetismPacket;
import zabi.minecraft.extraalchemy.entitydata.PlayerProperties;
import zabi.minecraft.extraalchemy.utils.LibMod;

public class KeybindDispatcher {
	
	public static final KeyBinding MAGNETISM_TOGGLE = new KeyBinding("key.extraalchemy.magnetism_toggle", Type.KEYSYM, InputUtil.fromName("key.keyboard.n").getKeyCode(), LibMod.MOD_NAME);
	
	private static boolean wasMagnetismPressedLastTick = false; 
	
	public static void initKeybinds() {
		KeyBindingHelper.registerKeyBinding(MAGNETISM_TOGGLE);
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
