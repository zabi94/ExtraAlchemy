package zabi.minecraft.extraalchemy.statuseffect.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import zabi.minecraft.extraalchemy.statuseffect.ModStatusEffect;

public class EmptyStatusEffect extends ModStatusEffect {
	
	public EmptyStatusEffect(StatusEffectCategory type, int color, boolean isInstant) {
		super(type, color, isInstant);
	}
	
	@Override
	public void applyUpdateEffect(LivingEntity e, int amp) {
		//Logic handled in the TemptGoalMixin class
	}

	@Override
	public boolean canApplyUpdateEffect(int remainingTicks, int level) {
		return false;
	}
	
}
