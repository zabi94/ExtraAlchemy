package dev.zabi94.extraalchemy.mobEffect;

import net.minecraft.world.entity.LivingEntity;

public interface ToggleableEffect {

	public boolean isActive(LivingEntity e);
	
}
