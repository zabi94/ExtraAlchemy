package dev.zabi94.extraalchemy.mobEffect.effects;

import com.google.common.base.Predicates;

import dev.zabi94.extraalchemy.config.ModConfig;
import dev.zabi94.extraalchemy.entitydata.PlayerProperties;
import dev.zabi94.extraalchemy.mixin.AccessorExperienceOrbEntity;
import dev.zabi94.extraalchemy.mobEffect.ModStatusEffect;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class LearningStatusEffect extends ModStatusEffect {

	public LearningStatusEffect(MobEffectCategory type, int color, boolean isInstant) {
		super(type, color, isInstant);
	}

	@Override
	public boolean canApplyEffect(int remainingTicks, int level) {
		return true;
	}

	@Override
	public boolean applyEffectTick(ServerLevel world, LivingEntity entity, int i) {
		if (!(entity instanceof Player p)) return true;
		
		if (world.isClientSide() || p.isSpectator()) return true;
		
		p.level().getEntitiesOfClass(ExperienceOrb.class, p.getBoundingBox().inflate(2 + i * 2), Predicates.alwaysTrue()).forEach(orb -> {
			if (ModConfig.INSTANCE.learningIncreasesExpOrbValue) {
				
				PlayerProperties pp = PlayerProperties.of(p);
				float increasedXp = orb.getValue() * (1f + (0.1f * i));
				int assignableXp = pp.calculateXPDue(increasedXp);
				
				((AccessorExperienceOrbEntity) orb).extraalchemy_setAmount(assignableXp);
			}
			p.takeXpDelay = 0;
			orb.playerTouch(p);
		});
		return true;
	}
}
