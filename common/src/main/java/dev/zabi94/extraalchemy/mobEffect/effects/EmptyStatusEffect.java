package dev.zabi94.extraalchemy.mobEffect.effects;

import dev.zabi94.extraalchemy.mobEffect.ModStatusEffect;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class EmptyStatusEffect extends ModStatusEffect {
	
	public EmptyStatusEffect(MobEffectCategory type, int color, boolean isInstant) {
		super(type, color, isInstant);
	}
	
	@Override
	public boolean applyEffectTick(ServerLevel world, LivingEntity e, int amp) {
		//Logic handled in the TemptGoalMixin class
		return true;
	}

	@Override
	public boolean canApplyEffect(int remainingTicks, int level) {
		return false;
	}

}
