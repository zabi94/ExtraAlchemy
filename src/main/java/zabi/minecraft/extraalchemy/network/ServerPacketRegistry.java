package zabi.minecraft.extraalchemy.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.util.Hand;
// import zabi.minecraft.extraalchemy.compat.CuriosCompatBridge;
import zabi.minecraft.extraalchemy.entitydata.PlayerProperties;
import zabi.minecraft.extraalchemy.items.ModItems;
import zabi.minecraft.extraalchemy.items.PotionBagItem;

public class ServerPacketRegistry {

	public static void init() {
		ServerPlayNetworking.registerGlobalReceiver(C2S_Channels.MAGNETISM_ENABLE, (server, player, handler, buf, response) -> {
			boolean magnetismActive = buf.readBoolean();
			server.execute(() -> {
				((PlayerProperties) (Object) player).setMagnetismEnabled(magnetismActive);
				response.sendPacket(S2C_Channels.PLAY_CLICK_SOUND, PacketByteBufs.empty());
			});
		});
		
		ServerPlayNetworking.registerGlobalReceiver(C2S_Channels.CYCLE_BAG_MODES, (server, player, handler, buf, response) -> {
			boolean hand = buf.readBoolean();
			server.execute(() -> {
				PotionBagItem.toggleStatusForPlayer(player, hand?Hand.MAIN_HAND:Hand.OFF_HAND);
				ItemCooldownManager icm = player.getItemCooldownManager();
				icm.set(ModItems.POTION_BAG, 10);
				response.sendPacket(S2C_Channels.PLAY_CLICK_SOUND, PacketByteBufs.empty());
			});
		});
		
		// ServerPlayNetworking.registerGlobalReceiver(C2S_Channels.TOGGLE_CURIOS, (server, player, handler, buf, response) -> {
			
		// 	server.execute(() -> {
		// 		if (CuriosCompatBridge.toggleRings(player)) {
		// 			response.sendPacket(S2C_Channels.PLAY_CLICK_SOUND, PacketByteBufs.empty());
		// 		}
		// 	});
			
			
		// });
	}
	
}
