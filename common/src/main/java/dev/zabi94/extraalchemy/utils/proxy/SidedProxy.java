package dev.zabi94.extraalchemy.utils.proxy;

import java.util.Optional;

import net.minecraft.world.item.crafting.RecipeAccess;


public abstract class SidedProxy {
	
	protected static SidedProxy PROXY;
	
	public abstract void registerProxy();
	
	public static SidedProxy getProxy() {
		return PROXY;
	}
	
	public abstract Optional<RecipeAccess> getRecipeManager();

}
