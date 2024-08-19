package zabi.minecraft.extraalchemy.entitydata;

import net.minecraft.entity.LivingEntity;

public interface EntityProperties {

	public void markEffectsDirty();
	
	public static EntityProperties of(LivingEntity entity) {
		return (EntityProperties) entity;
	}
	
}
