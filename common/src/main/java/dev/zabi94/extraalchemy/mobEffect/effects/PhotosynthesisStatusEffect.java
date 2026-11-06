package dev.zabi94.extraalchemy.mobEffect.effects;

import dev.zabi94.extraalchemy.mobEffect.ModStatusEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class PhotosynthesisStatusEffect extends ModStatusEffect {

	public PhotosynthesisStatusEffect(MobEffectCategory type, int color, boolean isInstant) {
		super(type, color, isInstant);
	}

	@Override
	protected boolean canApplyEffect(int remainingTicks, int level) {
		return remainingTicks % 60 == 0;
	}

	@Override
	public boolean applyEffectTick(ServerLevel w, LivingEntity entity, int level) {
		if (!w.isClientSide() && entity instanceof Player player && isInDaylight(entity)) {
			player.getFoodData().eat(level, 0f);
		}
		
		return true;
	}

	private static boolean isInDaylight(LivingEntity entity) {
		if (entity.level().isBrightOutside() && !entity.level().isRaining()) {

			BlockPos position = entity.blockPosition();

			if (entity.level().canSeeSky(position)) {
				return true;
			}
		}
		return false;
	}


}
