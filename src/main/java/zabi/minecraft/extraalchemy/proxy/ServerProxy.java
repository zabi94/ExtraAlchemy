package zabi.minecraft.extraalchemy.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import zabi.minecraft.extraalchemy.ExtraAlchemy;
import zabi.minecraft.extraalchemy.lib.Log;
import zabi.minecraft.extraalchemy.network.packets.PacketAskConfig;

public class ServerProxy extends Proxy {

	@Override
	public void updatePlayerPotion(EntityPlayer e, PotionEffect fx) {
		((EntityPlayerMP)e).connection.sendPacket(new SPacketEntityEffect(e.getEntityId(), fx));
	}

	@Override
	public void registerEventHandler() {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onLogin(PlayerLoggedInEvent evt) {
		Log.i("Asking client for config status");
		ExtraAlchemy.network.sendTo(new PacketAskConfig(), (EntityPlayerMP) evt.player);
	}

	@Override
	public void playDispelSound() {}

}
