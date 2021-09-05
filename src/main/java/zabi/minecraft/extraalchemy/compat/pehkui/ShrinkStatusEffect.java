package zabi.minecraft.extraalchemy.compat.pehkui;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffectCategory;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;
import zabi.minecraft.extraalchemy.statuseffect.ModStatusEffect;

public class ShrinkStatusEffect extends ModStatusEffect {

	public ShrinkStatusEffect(StatusEffectCategory type, int color, boolean isInstant) {
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
		if (!entity.world.isClient) {
			ScaleData data = ModSizeModifiers.SHRINKING.getScaleData(entity);
			data.setScale(getScale(amplifier));
			ScaleType.BASE.getScaleData(entity).markForSync(true);
			data.markForSync(true);
		}
	}
	
	@Override
	public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		super.onRemoved(entity, attributes, amplifier);
		if (!entity.world.isClient) {
			ScaleData data = ModSizeModifiers.SHRINKING.getScaleData(entity);
			data.resetScale(true);
			ScaleType.BASE.getScaleData(entity).markForSync(true);
			data.markForSync(true);
		}
	}
	
	private static float getScale(int amplifier) {
		return 1f/(2f * (float)(amplifier + 1));
	}

}
