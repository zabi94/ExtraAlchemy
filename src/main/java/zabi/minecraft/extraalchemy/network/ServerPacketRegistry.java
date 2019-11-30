package zabi.minecraft.extraalchemy.network;

import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import zabi.minecraft.extraalchemy.client.network.ToggleMagnetismPacket;
import zabi.minecraft.extraalchemy.entitydata.PlayerProperties;

public class ServerPacketRegistry {

	public static void init() {
		ServerSidePacketRegistry.INSTANCE.register(ToggleMagnetismPacket.ID, (ctx, buf) -> {
			boolean magnetismActive = buf.readBoolean();
			ctx.getTaskQueue().execute(() -> {
				((PlayerProperties) (Object) ctx.getPlayer()).setMagnetismEnabled(magnetismActive);
			});
		});
	}
	
}
