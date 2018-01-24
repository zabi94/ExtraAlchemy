package zabi.minecraft.extraalchemy.network.packets;


import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zabi.minecraft.extraalchemy.ExtraAlchemy;


public class PacketDispelSuccess implements IMessage {

	public PacketDispelSuccess() {} //Necessario
	

	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}

	public static class Handler implements IMessageHandler<PacketDispelSuccess, IMessage> {

		@Override
		public IMessage onMessage(PacketDispelSuccess message, MessageContext ctx) {
			ExtraAlchemy.proxy.playDispelSound();
			return null;
		}

	}

}
