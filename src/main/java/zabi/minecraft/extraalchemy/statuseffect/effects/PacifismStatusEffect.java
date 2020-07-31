package zabi.minecraft.extraalchemy.statuseffect.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectType;
import zabi.minecraft.extraalchemy.statuseffect.ModStatusEffect;

public class PacifismStatusEffect extends ModStatusEffect {

	public PacifismStatusEffect(StatusEffectType type, int color, boolean isInstant) {
		super(type, color, isInstant);
	}
	
	@Override
	protected boolean canApplyEffect(int remainingTicks, int level) {
		return false;
	}
	
	@Override
	public void applyUpdateEffect(LivingEntity entity, int level) {
		//No-op
	}
	
	//Logic handled in onDamaged - EntityDamaged mixin

}
