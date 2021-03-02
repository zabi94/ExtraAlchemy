package zabi.minecraft.extraalchemy.client.input;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.InputUtil.Type;
import zabi.minecraft.extraalchemy.entitydata.PlayerProperties;
import zabi.minecraft.extraalchemy.items.ModItems;
import zabi.minecraft.extraalchemy.network.C2S_Channels;
import zabi.minecraft.extraalchemy.network.SimplePacketBufs;
import zabi.minecraft.extraalchemy.utils.LibMod;

public class KeybindDispatcher {

	public static final KeyBinding MAGNETISM_TOGGLE = new KeyBinding("key.extraalchemy.magnetism_toggle", Type.KEYSYM, InputUtil.fromTranslationKey("key.keyboard.n").getCode(), LibMod.MOD_NAME);
	public static final KeyBinding POTION_BAG_MODE = new KeyBinding("key.extraalchemy.potion_bag_mode", Type.KEYSYM, InputUtil.fromTranslationKey("key.keyboard.k").getCode(), LibMod.MOD_NAME);

	private static boolean wasMagnetismPressedLastTick = false; 

	public static void initKeybinds() {
		KeyBindingHelper.registerKeyBinding(MAGNETISM_TOGGLE);
		KeyBindingHelper.registerKeyBinding(POTION_BAG_MODE);
	}

	@SuppressWarnings("resource")
	public static void registerListeners() {
		ClientTickEvents.END_CLIENT_TICK.register(evt -> {
			if (MAGNETISM_TOGGLE.isPressed()) {
				if (!wasMagnetismPressedLastTick) {
					PlayerProperties pp = (PlayerProperties) (Object) MinecraftClient.getInstance().player;
					boolean newMagnetismStatus = !pp.isMagnetismEnabled();
					pp.setMagnetismEnabled(newMagnetismStatus);
					ClientPlayNetworking.send(C2S_Channels.MAGNETISM_ENABLE, SimplePacketBufs.ofBool(newMagnetismStatus));
				}
				wasMagnetismPressedLastTick = true;
			} else {
				wasMagnetismPressedLastTick = false;
			}

			if (POTION_BAG_MODE.isPressed() && !evt.player.getItemCooldownManager().isCoolingDown(ModItems.POTION_BAG)) {
				boolean mainHand = evt.player.getMainHandStack().getItem() == ModItems.POTION_BAG;
				boolean offHand = evt.player.getOffHandStack().getItem() == ModItems.POTION_BAG;
				if (mainHand || offHand) {
					ClientPlayNetworking.send(C2S_Channels.CYCLE_BAG_MODES, SimplePacketBufs.ofBool(mainHand));
				}
			}
		});
	}
}
