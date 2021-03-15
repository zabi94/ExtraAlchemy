package zabi.minecraft.extraalchemy.client.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import zabi.minecraft.extraalchemy.network.S2C_Channels;

public class ClientPacketRegistry {

	public static void init() {
		
		ClientPlayNetworking.registerGlobalReceiver(S2C_Channels.PLAY_CLICK_SOUND, (client, handler, buf,responseSender) -> {
			client.player.playSound(SoundEvents.UI_BUTTON_CLICK, SoundCategory.MASTER, 1, 1);
		});
		
	}

}
