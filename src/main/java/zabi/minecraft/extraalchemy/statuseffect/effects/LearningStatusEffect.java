package zabi.minecraft.extraalchemy.statuseffect.effects;

import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.player.PlayerEntity;
import zabi.minecraft.extraalchemy.config.ModConfig;
import zabi.minecraft.extraalchemy.mixin.AccessorExperienceOrbEntity;
import zabi.minecraft.extraalchemy.statuseffect.ModStatusEffect;

public class LearningStatusEffect extends ModStatusEffect {

	public LearningStatusEffect(StatusEffectType type, int color, boolean isInstant) {
		super(type, color, isInstant);
	}

	@Override
	public boolean canApplyUpdateEffect(int remainingTicks, int level) {
		return true;
	}
	
	@Override
	public void applyUpdateEffect(LivingEntity entity, int i) {
		if (entity instanceof PlayerEntity) {
			PlayerEntity p = (PlayerEntity) entity;
			if (!p.world.isClient && !p.isSpectator()) {
				p.world.getEntitiesByClass(ExperienceOrbEntity.class, p.getBoundingBox().expand(2 + i * 2), null).forEach(orb -> {
					if (ModConfig.INSTANCE.learningIncreasesExpOrbValue) {
						((AccessorExperienceOrbEntity) orb).setAmount((int) (orb.getExperienceAmount() * (1f + (0.1f * i))));
					}
					orb.pickupDelay = 0;
					orb.onPlayerCollision(p);
					p.experiencePickUpDelay = 0;
				});
			}
		}
	}
}
