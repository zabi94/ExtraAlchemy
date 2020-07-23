package zabi.minecraft.extraalchemy.statuseffect.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.math.Vec3d;
import zabi.minecraft.extraalchemy.statuseffect.ModStatusEffect;

public class GravityStatusEffect extends ModStatusEffect {

	public GravityStatusEffect(StatusEffectType type, int color, boolean isInstant) {
		super(type, color, isInstant);
	}
	
	@Override
	protected boolean canApplyEffect(int remainingTicks, int level) {
		return true;
	}
	
	@Override
	public void applyUpdateEffect(LivingEntity entity, int level) {
		if (!entity.world.isClient && !entity.onGround) {
			double grav = -0.03 * (2*level + 1);
			entity.addVelocity(0, grav, 0);
			entity.move(MovementType.SELF, new Vec3d(0, grav, 0));
		}
	}
	
}
