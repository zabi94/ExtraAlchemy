package zabi.minecraft.extraalchemy.statuseffect.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.math.Vec3d;
import zabi.minecraft.extraalchemy.statuseffect.ModStatusEffect;

public class GravityStatusEffect extends ModStatusEffect {

	public GravityStatusEffect(StatusEffectCategory type, int color, boolean isInstant) {
		super(type, color, isInstant);
	}
	
	@Override
	protected boolean canApplyEffect(int remainingTicks, int level) {
		return true;
	}
	
	@Override
	public void applyUpdateEffect(LivingEntity entity, int level) {
		if (!entity.getEntityWorld().isClient && !entity.isOnGround()) {
			entity.move(MovementType.SELF, new Vec3d(0, -0.1 * (level + 1), 0));
		}
	}
	
}
