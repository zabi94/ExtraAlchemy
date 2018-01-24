package zabi.minecraft.extraalchemy.network.packets;


import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zabi.minecraft.extraalchemy.lib.Config;


public class PacketAskConfig implements IMessage {

	public PacketAskConfig() {} //Necessario
	

	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}

	public static class Handler implements IMessageHandler<PacketAskConfig, IMessage> {

		@Override
		public IMessage onMessage(PacketAskConfig message, MessageContext ctx) {
			return new PacketConfigStatus(Config.getConfigSignature());
		}

	}

}
