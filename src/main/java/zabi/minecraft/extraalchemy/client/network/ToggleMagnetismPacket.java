package zabi.minecraft.extraalchemy.client.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import zabi.minecraft.extraalchemy.utils.LibMod;

public class ToggleMagnetismPacket extends PacketByteBuf {
	
	public static final Identifier ID = new Identifier(LibMod.MOD_ID, "packet_magnetism");

	private ToggleMagnetismPacket(ByteBuf byteBuf, boolean magnetism) {
		super(byteBuf);
		byteBuf.writeBoolean(magnetism);
	}
	
	public static ToggleMagnetismPacket anew(boolean magnetismStatus) {
		return new ToggleMagnetismPacket(Unpooled.buffer(), magnetismStatus);
	}

}
