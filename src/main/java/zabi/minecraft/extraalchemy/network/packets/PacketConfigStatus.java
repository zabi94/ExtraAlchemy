package zabi.minecraft.extraalchemy.network.packets;


import io.netty.buffer.ByteBuf;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zabi.minecraft.extraalchemy.lib.Config;
import zabi.minecraft.extraalchemy.lib.Log;


public class PacketConfigStatus implements IMessage {

	protected long status = 0;
	
	public PacketConfigStatus() {
	}
	
	public PacketConfigStatus(long stat) {
		status = stat;
		Log.d("Sending client config status: "+status);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		status = buf.readLong();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(status);
	}

	public static class Handler implements IMessageHandler<PacketConfigStatus, IMessage> {
		@Override
		public IMessage onMessage(PacketConfigStatus message, MessageContext ctx) {
			if (Config.getConfigSignature()!=message.status) {
				Log.w("Configuration mismatch for "+ctx.getServerHandler().player.getGameProfile().getName());
				String reasons = Config.getDifferences(message.status);
				ctx.getServerHandler().player.connection.onDisconnect(new TextComponentString("Your Extra Alchemy configuration isn't compatible with the server, you need to change the following config(s):\n\n"+reasons+"\n\nand restart your game"));
			}
			Log.d("Valid configuration found ("+message.status+")");
			return null;
		}

	}

}
