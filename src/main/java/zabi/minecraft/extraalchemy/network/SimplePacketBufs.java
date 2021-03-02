package zabi.minecraft.extraalchemy.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;

public class SimplePacketBufs {
	
	public static PacketByteBuf ofBool(boolean bool) {
		PacketByteBuf result = PacketByteBufs.create();
		result.writeBoolean(bool);
		return result;
	}

}
