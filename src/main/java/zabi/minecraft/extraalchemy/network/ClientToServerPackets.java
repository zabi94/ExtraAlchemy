package zabi.minecraft.extraalchemy.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import zabi.minecraft.extraalchemy.network.packets.SimplePacket;
import zabi.minecraft.extraalchemy.network.packets.TogglePacket;
import zabi.minecraft.extraalchemy.utils.LibMod;

public class ClientToServerPackets {
	
	public static final TogglePacket MAGNETISM_ENABLE = new TogglePacket(LibMod.id("magnetism_enable"));
	public static final TogglePacket CYCLE_BAG_MODES = new TogglePacket(LibMod.id("cycle_bag_mode"));
	public static final SimplePacket TOGGLE_RINGS_IN_EXTRA_INVENTORIES = new SimplePacket(LibMod.id("toggle_curios"));

	public static void register() {
		PayloadTypeRegistry.playC2S().register(MAGNETISM_ENABLE.id(), MAGNETISM_ENABLE.getCodec());
		PayloadTypeRegistry.playC2S().register(CYCLE_BAG_MODES.id(), CYCLE_BAG_MODES.getCodec());
		PayloadTypeRegistry.playC2S().register(TOGGLE_RINGS_IN_EXTRA_INVENTORIES.id(), TOGGLE_RINGS_IN_EXTRA_INVENTORIES.getCodec());
	}
}
