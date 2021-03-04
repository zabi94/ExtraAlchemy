package zabi.minecraft.extraalchemy.entitydata;

import net.minecraft.entity.LivingEntity;
import zabi.minecraft.extraalchemy.utils.DimensionalPosition;

public interface EntityProperties {
	
	public DimensionalPosition getRecallPosition();
	
	public void setRecallData(DimensionalPosition pos);

	public void markEffectsDirty();
	
	public int getLastGrowthAmplifier();
	
	public void setLastGrowthAmplifier(int mult);
	
	public int getLastShrinkAmplifier();
	
	public void setLastShrinkAmplifier(int mult);
	
	public static EntityProperties of(LivingEntity entity) {
		return (EntityProperties) entity;
	}
	
}
