package zabi.minecraft.extraalchemy.potion;

import net.minecraft.entity.EntityLivingBase;

public interface NoncontinuousEffect {
	boolean isEffectActive(EntityLivingBase entity);
}
