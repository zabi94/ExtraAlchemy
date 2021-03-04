package zabi.minecraft.extraalchemy.compat.pehkui;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffectType;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;
import zabi.minecraft.extraalchemy.entitydata.EntityProperties;
import zabi.minecraft.extraalchemy.statuseffect.ModStatusEffect;

public class GrowthStatusEffect extends ModStatusEffect {

	public GrowthStatusEffect(StatusEffectType type, int color, boolean isInstant) {
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
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		super.onApplied(entity, attributes, amplifier);
		ScaleData data = ScaleType.BASE.getScaleData(entity);
		EntityProperties props = EntityProperties.of(entity);
		if (props.getLastGrowthAmplifier() >= 0) {
			data.setTargetScale(data.getTargetScale()/getScale(props.getLastGrowthAmplifier()));
		}
		entity.removeStatusEffect(PehkuiPotions.shrinking);
		data.setTargetScale(data.getTargetScale()*getScale(amplifier));
		props.setLastGrowthAmplifier(amplifier);
	}
	
	@Override
	public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		super.onRemoved(entity, attributes, amplifier);
		ScaleData data = ScaleType.BASE.getScaleData(entity);
		data.setTargetScale(data.getTargetScale()/getScale(amplifier));
		EntityProperties.of(entity).setLastGrowthAmplifier(-1);
	}
	
	private static float getScale(int amplifier) {
		return amplifier + 2;
	}

}
