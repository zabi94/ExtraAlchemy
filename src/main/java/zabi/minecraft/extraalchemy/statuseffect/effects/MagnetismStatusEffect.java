package zabi.minecraft.extraalchemy.statuseffect.effects;

import com.google.common.base.Predicates;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import zabi.minecraft.extraalchemy.entitydata.PlayerProperties;
import zabi.minecraft.extraalchemy.statuseffect.ModStatusEffect;
import zabi.minecraft.extraalchemy.statuseffect.ToggleableEffect;

public class MagnetismStatusEffect extends ModStatusEffect implements ToggleableEffect {

	public MagnetismStatusEffect(StatusEffectCategory type, int color, boolean isInstant) {
		super(type, color, isInstant);
	}

	@Override
	public void applyUpdateEffect(LivingEntity entity, int level) {
		if (!entity.getEntityWorld().isClient) {
			if (entity instanceof PlayerEntity player) {
				if (PlayerProperties.of(player).isMagnetismEnabled()) {
					entity.getEntityWorld().getEntitiesByClass(ItemEntity.class, entity.getBoundingBox().expand((level + 1) * 5), Predicates.alwaysTrue())
					.stream()
					.map(e -> (ItemEntity) e)
					.filter(e -> e.isAlive() && !e.isRemoved())
					.filter(e -> e.cannotPickup() == entity.isSneaking())
					.forEach(e -> e.onPlayerCollision((PlayerEntity) entity));
				}
			} else {
				entity.removeStatusEffectInternal(this);
			}
		}
	}

	@Override
	protected boolean canApplyEffect(int remainingTicks, int level) {
		return true;
	}

	@Override
	public boolean isActive(LivingEntity e) {
		if (e instanceof PlayerEntity p) {
			return PlayerProperties.of(p).isMagnetismEnabled();
		}
		return false;
	}
	
}
