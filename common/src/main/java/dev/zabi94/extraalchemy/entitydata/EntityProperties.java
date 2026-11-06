package dev.zabi94.extraalchemy.entitydata;

import net.minecraft.world.entity.LivingEntity;

public interface EntityProperties {

	public void markEffectsDirty();
	
	public static EntityProperties of(LivingEntity entity) {
		return (EntityProperties) entity;
	}
	
}
