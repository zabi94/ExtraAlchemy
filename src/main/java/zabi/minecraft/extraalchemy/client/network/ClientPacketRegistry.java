package zabi.minecraft.extraalchemy.client.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import zabi.minecraft.extraalchemy.network.S2C_Channels;

public class ClientPacketRegistry {

	public static void init() {
		
		ClientPlayNetworking.registerGlobalReceiver(S2C_Channels.PLAY_CLICK_SOUND, (client, handler, buf,responseSender) -> {
			client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f));
		});
		
	}

}
