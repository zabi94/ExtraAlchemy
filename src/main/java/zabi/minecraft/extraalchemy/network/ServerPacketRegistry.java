package zabi.minecraft.extraalchemy.network;

import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.util.Hand;
import zabi.minecraft.extraalchemy.client.network.ToggleBagAutoSelection;
import zabi.minecraft.extraalchemy.client.network.ToggleMagnetismPacket;
import zabi.minecraft.extraalchemy.entitydata.PlayerProperties;
import zabi.minecraft.extraalchemy.items.ModItems;
import zabi.minecraft.extraalchemy.items.PotionBagItem;

public class ServerPacketRegistry {

	public static void init() {
		ServerSidePacketRegistry.INSTANCE.register(ToggleMagnetismPacket.ID, (ctx, buf) -> {
			boolean magnetismActive = buf.readBoolean();
			ctx.getTaskQueue().execute(() -> {
				((PlayerProperties) (Object) ctx.getPlayer()).setMagnetismEnabled(magnetismActive);
			});
		});
		
		ServerSidePacketRegistry.INSTANCE.register(ToggleBagAutoSelection.ID, (ctx, buf) -> {
			boolean hand = buf.readBoolean();
			ctx.getTaskQueue().execute(() -> {
				PotionBagItem.toggleStatusForPlayer(ctx.getPlayer(), hand?Hand.MAIN_HAND:Hand.OFF_HAND);
				ItemCooldownManager icm = ctx.getPlayer().getItemCooldownManager();
				icm.set(ModItems.POTION_BAG, 10);
			});
		});
	}
	
}
