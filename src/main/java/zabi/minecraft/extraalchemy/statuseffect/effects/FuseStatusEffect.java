package zabi.minecraft.extraalchemy.statuseffect.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.world.explosion.Explosion.DestructionType;
import zabi.minecraft.extraalchemy.statuseffect.ModStatusEffect;

public class FuseStatusEffect extends ModStatusEffect {

	public FuseStatusEffect(StatusEffectCategory type, int color, boolean isInstant) {
		super(type, color, isInstant);
	}

	@Override
	protected boolean canApplyEffect(int remainingTicks, int level) {
		return remainingTicks == 1;
	}

	@Override
	public void applyUpdateEffect(LivingEntity entity, int level) {
		if (!entity.world.isClient) {
			entity.world.createExplosion(null, entity.getX(), entity.getY() + 1, entity.getZ(), 0.5f+level, false, DestructionType.BREAK);
		}
	}

}
