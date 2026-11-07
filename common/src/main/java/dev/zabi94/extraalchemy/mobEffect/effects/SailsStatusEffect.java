package dev.zabi94.extraalchemy.mobEffect.effects;

import dev.zabi94.extraalchemy.mixin.AccessorBoatEntity;
import dev.zabi94.extraalchemy.mobEffect.ModStatusEffect;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.vehicle.boat.Boat;

public class SailsStatusEffect extends ModStatusEffect {
	
	public SailsStatusEffect(MobEffectCategory type, int color, boolean isInstant) {
		super(type, color, isInstant);
	}
	
	@Override
	public boolean applyEffectTick(ServerLevel w, LivingEntity e, int amp) {
		
		if (!(e.getVehicle() instanceof Boat b)) return true;

		if (b.isLocalInstanceAuthoritative() && ((AccessorBoatEntity) b).extraalchemy_isPressingForward()) {
			float f = 0.05f + amp * 0.05f;
			float p = 0.017453292F;
			b.setDeltaMovement(b.getDeltaMovement().add(Mth.sin(-b.getYRot() * p) * f, 0,  Mth.cos(b.getYRot() * p) * f));
		}

		return true;
	}

	@Override
	public boolean canApplyEffect(int remainingTicks, int level) {
		return true;
	}
	
}
