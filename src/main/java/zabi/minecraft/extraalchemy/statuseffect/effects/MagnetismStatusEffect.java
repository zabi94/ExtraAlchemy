package zabi.minecraft.extraalchemy.statuseffect.effects;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.player.PlayerEntity;
import zabi.minecraft.extraalchemy.entitydata.PlayerProperties;
import zabi.minecraft.extraalchemy.statuseffect.ModStatusEffect;

public class MagnetismStatusEffect extends ModStatusEffect {

	public MagnetismStatusEffect(StatusEffectType type, int color, boolean isInstant) {
		super(type, color, isInstant);
	}

	@Override
	public void applyUpdateEffect(LivingEntity entity, int level) {
		if (!entity.world.isClient) {
			if (entity instanceof PlayerEntity) {
				if (((PlayerProperties) (Object) entity).isMagnetismEnabled()) {
					entity.world.getEntitiesByClass(ItemEntity.class, entity.getBoundingBox().expand((level + 1) * 3), null)
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
	
}
