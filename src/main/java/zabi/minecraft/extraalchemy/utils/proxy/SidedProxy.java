package zabi.minecraft.extraalchemy.utils.proxy;

import java.util.Optional;

import net.minecraft.recipe.RecipeManager;

public abstract class SidedProxy {
	
	protected static SidedProxy PROXY;
	
	public abstract void registerProxy();
	
	public static SidedProxy getProxy() {
		return PROXY;
	}
	
	public abstract Optional<RecipeManager> getRecipeManager();

}
