package zabi.minecraft.extraalchemy.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import zabi.minecraft.extraalchemy.compat.trinkets.TrinketsCompatBridge;
// import zabi.minecraft.extraalchemy.compat.CuriosCompatBridge;
import zabi.minecraft.extraalchemy.entitydata.PlayerProperties;
import zabi.minecraft.extraalchemy.items.ModItems;
import zabi.minecraft.extraalchemy.items.PotionBagItem;

public class ServerPacketRegistry {

	public static void init() {
		ServerPlayNetworking.registerGlobalReceiver(ClientToServerPackets.MAGNETISM_ENABLE.id(), (p,c) -> {
			boolean magnetismActive = p.getState();
			c.player().getServerWorld().getServer().execute(() -> {
				PlayerProperties.of(c.player()).setMagnetismEnabled(magnetismActive);
	 			c.responseSender().sendPacket(ServerToClientPackets.PLAY_CLICK_SOUND.payload());
			});
		});
		
		ServerPlayNetworking.registerGlobalReceiver(ClientToServerPackets.CYCLE_BAG_MODES.id(), (p,c) -> {
			boolean hand = p.getState();
			c.player().getServerWorld().getServer().execute(() -> {
				PotionBagItem.toggleStatusForPlayer(c.player(), hand?Hand.MAIN_HAND:Hand.OFF_HAND);
				ItemCooldownManager icm = c.player().getItemCooldownManager();
				icm.set(ModItems.POTION_BAG, 10);
	 			c.responseSender().sendPacket(ServerToClientPackets.PLAY_CLICK_SOUND.payload());
			});
		});
		
		 ServerPlayNetworking.registerGlobalReceiver(ClientToServerPackets.TOGGLE_RINGS_IN_EXTRA_INVENTORIES.id(), (p,c) -> {
		 	c.player().getServerWorld().getServer().execute(() -> {
		 		if (toggleRings(c.player())) {
		 			c.responseSender().sendPacket(ServerToClientPackets.PLAY_CLICK_SOUND.payload());
		 		}
		 	});
		 });
	}
	
	private static boolean toggleRings(PlayerEntity player) {
		boolean foundAny = false;
		foundAny |= TrinketsCompatBridge.toggleRings(player);
		// foundAny |= OtherInventoryModCompatBridge.toggleRings(player)
		return foundAny; 
	}
	
}
