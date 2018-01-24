package zabi.minecraft.extraalchemy.network.packets;


import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zabi.minecraft.extraalchemy.ExtraAlchemy;
import zabi.minecraft.extraalchemy.potion.potion.PotionMagnetism;


public class PacketMagnetStatus implements IMessage {

	boolean active;
	
	public PacketMagnetStatus() {} //Necessario
	
	public PacketMagnetStatus(boolean s) {
		active = s;
	} 
	
	@Override
	public void fromBytes(ByteBuf buf) {
		active = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(active);
	}

	public static class Handler implements IMessageHandler<PacketMagnetStatus, IMessage> {

		@Override
		public IMessage onMessage(PacketMagnetStatus message, MessageContext ctx) {
			PotionMagnetism.setMagnetStatus(ExtraAlchemy.proxy.getSP(), message.active);
			return null;
		}

	}

}
