package zabi.minecraft.extraalchemy.compat.pehkui;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import virtuoel.pehkui.api.ScaleData;
import zabi.minecraft.extraalchemy.statuseffect.ModStatusEffect;

public class GrowthStatusEffect extends ModStatusEffect {

	public GrowthStatusEffect(StatusEffectCategory type, int color, boolean isInstant) {
		super(type, color, isInstant);
	}

	@Override
	protected boolean canApplyEffect(int remainingTicks, int level) {
		return false;
	}

	@Override
	public void applyUpdateEffect(LivingEntity entity, int level) {
	}

	@Override
	public void onApplied(LivingEntity entity, int amplifier) {
		super.onApplied(entity, amplifier);
		if (!entity.getEntityWorld().isClient) {
			ScaleData data = ModSizeModifiers.GROWING.getScaleData(entity);
			data.setScale(getScale(amplifier));
			ScaleTypesAdapter.BASE.getScaleData(entity).markForSync(true);
			data.markForSync(true);	
		}
	}
	
	@Override
	public void onEffectRemoved(LivingEntity entity) {
		if (!entity.getEntityWorld().isClient) {
			ScaleData data = ModSizeModifiers.GROWING.getScaleData(entity);
			data.resetScale(true);
			ScaleTypesAdapter.BASE.getScaleData(entity).markForSync(true);
			data.markForSync(true);
		}
	}
	
	private static float getScale(int amplifier) {
		return amplifier + 2;
	}

}
