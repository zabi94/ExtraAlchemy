package dev.zabi94.extraalchemy.mobEffect.effects;

import dev.zabi94.extraalchemy.mobEffect.ModStatusEffect;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;

public class GravityStatusEffect extends ModStatusEffect {

	public GravityStatusEffect(MobEffectCategory type, int color, boolean isInstant) {
		super(type, color, isInstant);
	}
	
	@Override
	protected boolean canApplyEffect(int remainingTicks, int level) {
		return true;
	}
	
	@Override
	public boolean applyEffectTick(ServerLevel world, LivingEntity entity, int level) {
		if (!world.isClientSide() && !entity.onGround()) {
			entity.move(MoverType.SELF, new Vec3(0, -0.1 * (level + 1), 0));
		}
		
		return true;
	}
	
}
