package zabi.minecraft.extraalchemy.network.packets;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.CustomPayload.Id;
import net.minecraft.util.Identifier;

public class TogglePacket {
	
	private final Id<TogglePacket.PacketPayload> TOGGLE_PACKET_ID; 
	
	public TogglePacket(Identifier id) {
		this.TOGGLE_PACKET_ID = new CustomPayload.Id<TogglePacket.PacketPayload>(id);
	}

	public static void encode(RegistryByteBuf buf, TogglePacket.PacketPayload dest) {
		buf.writeBoolean(dest.getState());
	}
	
	public TogglePacket.PacketPayload decode(RegistryByteBuf buf, Identifier key) {
		return this.new PacketPayload(buf.readBoolean());
	}
	
	public Id<TogglePacket.PacketPayload> id() {
		return TOGGLE_PACKET_ID;
	}
	
	public PacketCodec<RegistryByteBuf, TogglePacket.PacketPayload> getCodec() {
		return PacketCodec.<RegistryByteBuf, TogglePacket.PacketPayload>ofStatic(TogglePacket::encode, buf -> decode(buf, TOGGLE_PACKET_ID.id()));
	}
	
	public PacketPayload payload(boolean state) {
		return this.new PacketPayload(state);
	}
	
	public class PacketPayload implements CustomPayload {

		private final boolean state;
		
		public PacketPayload(boolean state) {
			this.state = state;
		}

		@Override
		public Id<? extends CustomPayload> getId() {
			return TOGGLE_PACKET_ID;
		}
		
		public boolean getState() {
			return state;
		}
	}
	
}