package zabi.minecraft.extraalchemy.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.InputUtil.Type;
import zabi.minecraft.extraalchemy.client.network.ToggleBagAutoSelection;
import zabi.minecraft.extraalchemy.client.network.ToggleMagnetismPacket;
import zabi.minecraft.extraalchemy.entitydata.PlayerProperties;
import zabi.minecraft.extraalchemy.items.ModItems;
import zabi.minecraft.extraalchemy.utils.LibMod;

public class KeybindDispatcher {

	public static final KeyBinding MAGNETISM_TOGGLE = new KeyBinding("key.extraalchemy.magnetism_toggle", Type.KEYSYM, InputUtil.fromName("key.keyboard.n").getKeyCode(), LibMod.MOD_NAME);
	public static final KeyBinding POTION_BAG_MODE = new KeyBinding("key.extraalchemy.potion_bag_mode", Type.KEYSYM, InputUtil.fromName("key.keyboard.k").getKeyCode(), LibMod.MOD_NAME);


	private static boolean wasMagnetismPressedLastTick = false; 

	public static void initKeybinds() {
		KeyBindingHelper.registerKeyBinding(MAGNETISM_TOGGLE);
		KeyBindingHelper.registerKeyBinding(POTION_BAG_MODE);
	}
	public static void registerListeners() {
		ClientTickEvents.END_CLIENT_TICK.register(evt -> {
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

			if (POTION_BAG_MODE.isPressed() && !evt.player.getItemCooldownManager().isCoolingDown(ModItems.POTION_BAG)) {
				boolean mainHand = evt.player.getMainHandStack().getItem() == ModItems.POTION_BAG;
				boolean offHand = evt.player.getOffHandStack().getItem() == ModItems.POTION_BAG;
				if (mainHand || offHand) {
					ClientSidePacketRegistry.INSTANCE.sendToServer(ToggleBagAutoSelection.ID, ToggleBagAutoSelection.anew(mainHand));
				}
			}
		});
	}
}
