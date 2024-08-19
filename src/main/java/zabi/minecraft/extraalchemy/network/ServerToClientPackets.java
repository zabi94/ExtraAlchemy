package zabi.minecraft.extraalchemy.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import zabi.minecraft.extraalchemy.network.packets.SimplePacket;
import zabi.minecraft.extraalchemy.utils.LibMod;

public class ServerToClientPackets {
	
	
	public static final SimplePacket PLAY_CLICK_SOUND = new SimplePacket(LibMod.id("play_click"));

	public static void register() {
		PayloadTypeRegistry.playS2C().register(PLAY_CLICK_SOUND.id(), PLAY_CLICK_SOUND.getCodec());
	}
	
}
