package zabi.minecraft.extraalchemy.network.packets;


import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class PacketHighlightEntity implements IMessage {

	protected boolean apply = false;
	protected int entityID;
//	protected static ArrayList<Integer> affectedEntities = new ArrayList<Integer>();
	
	public PacketHighlightEntity() {} //Necessario
	
	public PacketHighlightEntity(boolean trueForApply, int entityID) {
		apply=trueForApply;
		this.entityID = entityID;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		apply=buf.readBoolean();
		entityID = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(apply);
		buf.writeInt(entityID);
	}

	public static class Handler implements IMessageHandler<PacketHighlightEntity, IMessage> {

		@Override
		@SideOnly(Side.CLIENT)
		public IMessage onMessage(PacketHighlightEntity message, MessageContext ctx) {
			EntityLivingBase ent = ((EntityLivingBase) Minecraft.getMinecraft().world.getEntityByID(message.entityID));
			ent.setGlowing(message.apply);
//			affectedEntities.add(ent.getEntityId());
			return null;
		}

	}

}
