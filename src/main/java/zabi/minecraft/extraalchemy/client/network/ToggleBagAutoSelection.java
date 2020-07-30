package zabi.minecraft.extraalchemy.client.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import zabi.minecraft.extraalchemy.utils.LibMod;

public class ToggleBagAutoSelection extends PacketByteBuf {
	
	public static final Identifier ID = new Identifier(LibMod.MOD_ID, "packet_potion_bag_autoselect");
	
	private ToggleBagAutoSelection(ByteBuf byteBuf, boolean mainHand) {
		super(byteBuf);
		byteBuf.writeBoolean(mainHand);
	}
	
	public static ToggleBagAutoSelection anew(boolean mainHand) {
		return new ToggleBagAutoSelection(Unpooled.buffer(), mainHand);
	}

}
