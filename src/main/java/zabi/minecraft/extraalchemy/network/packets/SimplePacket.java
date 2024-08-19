package zabi.minecraft.extraalchemy.network.packets;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.CustomPayload.Id;
import net.minecraft.util.Identifier;

public class SimplePacket  {
	
	private final Id<SimplePacket.PacketPayload> SIMPLE_PACKET_ID; 
	
	public SimplePacket(Identifier id) {
		this.SIMPLE_PACKET_ID = new CustomPayload.Id<SimplePacket.PacketPayload>(id);
	}

	public static void encode(RegistryByteBuf buf, SimplePacket.PacketPayload dest) {
		//No-op
	}
	
	public SimplePacket.PacketPayload decode(RegistryByteBuf buf, Identifier key) {
		return this.new PacketPayload();
	}
	
	public Id<SimplePacket.PacketPayload> id() {
		return SIMPLE_PACKET_ID;
	}
	
	public PacketCodec<RegistryByteBuf, SimplePacket.PacketPayload> getCodec() {
		return PacketCodec.<RegistryByteBuf, SimplePacket.PacketPayload>ofStatic(SimplePacket::encode, buf -> decode(buf, SIMPLE_PACKET_ID.id()));
	}
	
	public PacketPayload payload() {
		return this.new PacketPayload();
	}
	
	public class PacketPayload implements CustomPayload {

		@Override
		public Id<? extends CustomPayload> getId() {
			return SIMPLE_PACKET_ID;
		}
		
	}
	
}