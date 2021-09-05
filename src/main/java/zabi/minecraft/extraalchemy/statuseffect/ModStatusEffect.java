package zabi.minecraft.extraalchemy.statuseffect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import zabi.minecraft.extraalchemy.utils.Log;

public class ModStatusEffect extends StatusEffect {
	
	private boolean instant;
	private boolean isRegistered = false;
	
	public ModStatusEffect(StatusEffectCategory type, int color, boolean isInstant) {
		super(type, color);
		this.instant = isInstant;
	}
	
	@Override
	public boolean isInstant() {
		return instant;
	}
	
	@Override
	public boolean canApplyUpdateEffect(int remainingTicks, int level) {
		if (isInstant()) {
			return true;
		}
		return canApplyEffect(remainingTicks, level);
	}

	protected boolean canApplyEffect(int remainingTicks, int level) {
		if (!isInstant()) {
			Log.w("Non instant effects should override canApplyEffect!");
			Thread.dumpStack();
		}
		return false;
	}
	
	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		if (isInstant()) {
			applyInstantEffect(null, null, entity, amplifier, 1.0d);
		}
	}
	
	public ModStatusEffect onRegister() {
		isRegistered = true;
		return this;
	}
	
	public boolean isRegistered() {
		return isRegistered;
	}
	
}
