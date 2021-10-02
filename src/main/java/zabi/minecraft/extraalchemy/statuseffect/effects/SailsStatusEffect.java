package zabi.minecraft.extraalchemy.statuseffect.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.math.MathHelper;
import zabi.minecraft.extraalchemy.mixin.access.AccessorBoatEntity;
import zabi.minecraft.extraalchemy.statuseffect.ModStatusEffect;

public class SailsStatusEffect extends ModStatusEffect {
	
	public SailsStatusEffect(StatusEffectCategory type, int color, boolean isInstant) {
		super(type, color, isInstant);
	}
	
	@Override
	public void applyUpdateEffect(LivingEntity e, int amp) {
		if (e.getVehicle() instanceof BoatEntity) {
			BoatEntity b = (BoatEntity) e.getVehicle();
			if (b.isLogicalSideForUpdatingMovement() && ((AccessorBoatEntity) b).extraalchemy_isPressingForward()) {
				float f = 0.05f + amp * 0.05f;
				float p = 0.017453292F;
				b.setVelocity(b.getVelocity().add(MathHelper.sin(-b.getYaw() * p) * f, 0,  MathHelper.cos(b.getYaw() * p) * f));
			}
		}
	}

	@Override
	public boolean canApplyUpdateEffect(int remainingTicks, int level) {
		return true;
	}
	
}
