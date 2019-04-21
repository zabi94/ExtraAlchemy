package zabi.minecraft.extraalchemy.statuseffect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;

public class ModStatusEffect extends StatusEffect {
	
	private boolean instant;
	private boolean isRegistered = false;
	
	public ModStatusEffect(StatusEffectType type, int color, boolean isInstant) {
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
			return false;
		}
		return canApplyEffect(remainingTicks, level);
	}

	protected boolean canApplyEffect(int remainingTicks, int level) {
		if (!isInstant()) {
			throw new IllegalStateException("Non instant effects should override this method!");
		}
		return false;
	}
	
	public ModStatusEffect onRegister() {
		isRegistered = true;
		return this;
	}
	
	public boolean isRegistered() {
		return isRegistered;
	}
	
}
