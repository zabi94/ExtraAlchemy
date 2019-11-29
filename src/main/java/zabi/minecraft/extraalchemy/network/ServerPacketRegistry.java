package zabi.minecraft.extraalchemy.network;

import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import zabi.minecraft.extraalchemy.client.network.ToggleMagnetismPacket;
import zabi.minecraft.extraalchemy.entitydata.PlayerProperties;
import zabi.minecraft.extraalchemy.utils.Log;

public class ServerPacketRegistry {

	public static void init() {
		ServerSidePacketRegistry.INSTANCE.register(ToggleMagnetismPacket.ID, (ctx, buf) -> {
			boolean magnetismActive = buf.readBoolean();
			ctx.getTaskQueue().execute(() -> {
				((PlayerProperties) (Object) ctx.getPlayer()).setMagnetismEnabled(magnetismActive);
				Log.d("Server: magnetism toggled for "+ ctx.getPlayer().toString());
			});
		});
	}
	
}
