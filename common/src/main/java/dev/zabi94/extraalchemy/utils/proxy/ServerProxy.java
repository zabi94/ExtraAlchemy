package dev.zabi94.extraalchemy.utils.proxy;

import java.util.Optional;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.RecipeAccess;
import dev.zabi94.extraalchemy.utils.Log;

public class ServerProxy extends SidedProxy {
	
	private final MinecraftServer server;
	
	public ServerProxy(MinecraftServer server) {
		this.server = server;
	}

	@Override
	public Optional<RecipeAccess> getRecipeManager() {
		return Optional.of(server.getRecipeManager());
	}

	@Override
	public void registerProxy() {
		if (PROXY == null) {
			Log.i("Registering server proxy");
			PROXY = this;
		} else {
			Log.i("Proxy already registered, skipping");
		}
	}

}
