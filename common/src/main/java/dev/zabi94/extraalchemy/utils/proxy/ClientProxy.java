package dev.zabi94.extraalchemy.utils.proxy;

import java.util.Optional;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.crafting.RecipeAccess;
import dev.zabi94.extraalchemy.utils.Log;

public class ClientProxy extends SidedProxy {
	
	@Override
	public Optional<RecipeAccess> getRecipeManager() {
		if (Minecraft.getInstance().level != null) {
			return Optional.of(Minecraft.getInstance().level.recipeAccess());
			
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
