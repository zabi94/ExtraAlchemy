package zabi.minecraft.extraalchemy.utils.proxy;

import java.util.Optional;

import net.minecraft.recipe.RecipeManager;
import net.minecraft.server.MinecraftServer;
import zabi.minecraft.extraalchemy.utils.Log;

public class ServerProxy extends SidedProxy {
	
	private final MinecraftServer server;
	
	public ServerProxy(MinecraftServer server) {
		this.server = server;
	}

	@Override
	public Optional<RecipeManager> getRecipeManager() {
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
