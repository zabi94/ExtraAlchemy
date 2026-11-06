package dev.zabi94.extraalchemy.mobEffect;

import dev.zabi94.extraalchemy.utils.Log;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class ModStatusEffect extends MobEffect {
	
	private boolean instant;
	private boolean isRegistered = false;
	
	public ModStatusEffect(MobEffectCategory type, int color, boolean isInstant) {
		super(type, color);
		this.instant = isInstant;
	}
	
	@Override
	public boolean isInstantaneous() {
		return instant;
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int remainingTicks, int level) {
		if (isInstantaneous()) {
			return true;
		}
		return canApplyEffect(remainingTicks, level);
	}
	

	protected boolean canApplyEffect(int remainingTicks, int level) {
		if (!isInstantaneous()) {
			Log.w("Non instant effects should override canApplyEffect!");
			Thread.dumpStack();
		}
		return false;
	}
	
	@Override
	public boolean applyEffectTick(ServerLevel world, LivingEntity entity, int amplifier) {
		
		if (isInstantaneous()) {
			applyInstantaneousEffect(world, null, null, entity, amplifier, 1.0d);
			return false;
		}
		
		return super.applyEffectTick(world, entity, amplifier);
	}



	public ModStatusEffect onRegister() {
		isRegistered = true;
		return this;
	}
	
	public boolean isRegistered() {
		return isRegistered;
	}
	
	public void onEffectRemoved(LivingEntity entity) {
		
	}
	
}
