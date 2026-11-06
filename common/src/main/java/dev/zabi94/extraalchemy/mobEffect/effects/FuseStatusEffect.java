package dev.zabi94.extraalchemy.mobEffect.effects;

import dev.zabi94.extraalchemy.mobEffect.ModStatusEffect;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class FuseStatusEffect extends ModStatusEffect {

	public FuseStatusEffect(MobEffectCategory type, int color, boolean isInstant) {
		super(type, color, isInstant);
	}

	@Override
	protected boolean canApplyEffect(int remainingTicks, int level) {
		return remainingTicks == 1;
	}

	@Override
	public boolean applyEffectTick(ServerLevel world, LivingEntity entity, int level) {
		if (!world.isClientSide()) {
			entity.level().explode(null, entity.getX(), entity.getY() + 1, entity.getZ(), 0.5f+level, false, Level.ExplosionInteraction.MOB);
		}
		return true;
	}

}
