package zabi.minecraft.extraalchemy.client;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import zabi.minecraft.extraalchemy.ExtraAlchemy;
import zabi.minecraft.extraalchemy.lib.Reference;
import zabi.minecraft.extraalchemy.network.packets.PacketToggleMagnet;
import zabi.minecraft.extraalchemy.potion.PotionReference;

public class ModKeybinds {
	
	private KeyBinding suppress_magnet;
	
	public ModKeybinds() {
		suppress_magnet = new KeyBinding("Toggle Magnetism Suppressor", Keyboard.KEY_N, Reference.NAME);
		ClientRegistry.registerKeyBinding(suppress_magnet);
	}
	
	@SubscribeEvent
	public void buttonPressed(KeyInputEvent evt) {
		if (suppress_magnet.isPressed()) {
			if (Minecraft.getMinecraft().player.getActivePotionEffect(PotionReference.INSTANCE.MAGNETISM) != null) {
				ExtraAlchemy.network.sendToServer(new PacketToggleMagnet());
			}
		}
	}
}
