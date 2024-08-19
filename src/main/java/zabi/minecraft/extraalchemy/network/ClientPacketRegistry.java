package zabi.minecraft.extraalchemy.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class ClientPacketRegistry {

	public static void init() {
		ClientPlayNetworking.registerGlobalReceiver(ServerToClientPackets.PLAY_CLICK_SOUND.id(), (payload, context) -> {
			context.client().execute(() -> context.player().playSoundToPlayer(SoundEvents.UI_BUTTON_CLICK.value(), SoundCategory.MASTER, 1, 1));
		});
	}

}
