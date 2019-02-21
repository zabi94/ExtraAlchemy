package zabi.minecraft.extraalchemy.network.packets;


import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zabi.minecraft.extraalchemy.capability.MagnetismStatus;
import zabi.minecraft.extraalchemy.potion.PotionReference;


public class PacketToggleMagnet implements IMessage {

	
	public PacketToggleMagnet() {
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}

	public static class Handler implements IMessageHandler<PacketToggleMagnet, IMessage> {
		@Override
		public IMessage onMessage(PacketToggleMagnet message, MessageContext ctx) {
			if (ctx.getServerHandler().player.getActivePotionEffect(PotionReference.INSTANCE.MAGNETISM) != null) {
				ctx.getServerHandler().player.getCapability(MagnetismStatus.CAPABILITY, null).toggle();
			}
			return null;
		}
	}
}
