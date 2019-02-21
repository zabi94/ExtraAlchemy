package zabi.minecraft.extraalchemy.network;


import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import zabi.minecraft.extraalchemy.network.packets.PacketAskConfig;
import zabi.minecraft.extraalchemy.network.packets.PacketConfigStatus;
import zabi.minecraft.extraalchemy.network.packets.PacketDispelSuccess;
import zabi.minecraft.extraalchemy.network.packets.PacketToggleMagnet;


public class NetworkModRegistry {

	public static void registerMessages(SimpleNetworkWrapper net) {
		int id = 0;
		net.registerMessage(PacketAskConfig.Handler.class, PacketAskConfig.class, id++, Side.CLIENT);
		net.registerMessage(PacketDispelSuccess.Handler.class, PacketDispelSuccess.class, id++, Side.CLIENT);
		
		net.registerMessage(PacketConfigStatus.Handler.class, PacketConfigStatus.class, id++, Side.SERVER);
		net.registerMessage(PacketToggleMagnet.Handler.class, PacketToggleMagnet.class, id++, Side.SERVER);
	}
}
