package zabi.minecraft.extraalchemy.statuseffect;

import net.minecraft.entity.LivingEntity;

public interface ToggleableEffect {

	public boolean isActive(LivingEntity e);
	
}
