package zabi.minecraft.extraalchemy.utils.proxy;

import java.util.Optional;

import net.minecraft.client.MinecraftClient;
import net.minecraft.recipe.RecipeManager;
import zabi.minecraft.extraalchemy.utils.Log;

public class ClientProxy extends SidedProxy {
	
	@SuppressWarnings("resource")
	@Override
	public Optional<RecipeManager> getRecipeManager() {
		if (MinecraftClient.getInstance().world != null) {
			return Optional.of(MinecraftClient.getInstance().world.getRecipeManager());
		}
		return Optional.empty();
	}

	@Override
	public void registerProxy() {
		if (SidedProxy.getProxy() == null) {
			Log.i("Registering client proxy");
		} else {
			Log.i("Overwriting server proxy with client proxy");
		}
		
		PROXY = new ClientProxy();

	}

}
