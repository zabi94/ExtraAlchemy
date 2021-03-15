package zabi.minecraft.extraalchemy.compat;

import net.minecraft.server.network.ServerPlayerEntity;

public class CuriosCompatBridge {
	
	public static void init() {
		CuriosRings.init();
	}

	public static boolean toggleRings(ServerPlayerEntity player) {
		return CuriosRings.toggleRings(player);
	}

}
