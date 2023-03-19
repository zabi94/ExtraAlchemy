package zabi.minecraft.extraalchemy.statuseffect.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import zabi.minecraft.extraalchemy.statuseffect.ModStatusEffect;

public class PhotosynthesisStatusEffect extends ModStatusEffect {

	public PhotosynthesisStatusEffect(StatusEffectCategory type, int color, boolean isInstant) {
		super(type, color, isInstant);
	}

	@Override
	protected boolean canApplyEffect(int remainingTicks, int level) {
		return remainingTicks % 60 == 0;
	}

	@Override
	public void applyUpdateEffect(LivingEntity entity, int level) {
		if (!entity.world.isClient && entity instanceof PlayerEntity player && isInDaylight(entity)) {
			player.getHungerManager().add(level, 0f);
		}
	}

	private static boolean isInDaylight(LivingEntity entity) {
		if (entity.world.isDay() && !entity.world.isRaining()) {

			BlockPos position = entity.getBlockPos();

			if (entity.world.isSkyVisible(position)) {
				return true;
			}
		}
		return false;
	}


}
