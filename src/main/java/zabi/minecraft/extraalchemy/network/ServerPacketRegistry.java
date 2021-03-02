package zabi.minecraft.extraalchemy.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.util.Hand;
import zabi.minecraft.extraalchemy.client.network.ToggleBagAutoSelection;
import zabi.minecraft.extraalchemy.client.network.ToggleMagnetismPacket;
import zabi.minecraft.extraalchemy.entitydata.PlayerProperties;
import zabi.minecraft.extraalchemy.items.ModItems;
import zabi.minecraft.extraalchemy.items.PotionBagItem;

public class ServerPacketRegistry {

	public static void init() {
		ServerPlayNetworking.registerGlobalReceiver(ToggleMagnetismPacket.ID, (server, player, handler, buf, response) -> {
			boolean magnetismActive = buf.readBoolean();
			server.execute(() -> {
				((PlayerProperties) (Object) player).setMagnetismEnabled(magnetismActive);
			});
		});
		
		ServerPlayNetworking.registerGlobalReceiver(ToggleBagAutoSelection.ID, (server, player, handler, buf, response) -> {
			boolean hand = buf.readBoolean();
			server.execute(() -> {
				PotionBagItem.toggleStatusForPlayer(player, hand?Hand.MAIN_HAND:Hand.OFF_HAND);
				ItemCooldownManager icm = player.getItemCooldownManager();
				icm.set(ModItems.POTION_BAG, 10);
			});
		});
	}
	
}
