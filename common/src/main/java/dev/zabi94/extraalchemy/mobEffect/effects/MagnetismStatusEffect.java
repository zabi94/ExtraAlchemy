package dev.zabi94.extraalchemy.mobEffect.effects;

import com.google.common.base.Predicates;

import dev.zabi94.extraalchemy.entitydata.PlayerProperties;
import dev.zabi94.extraalchemy.mobEffect.ModStatusEffect;
import dev.zabi94.extraalchemy.mobEffect.ToggleableEffect;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;

public class MagnetismStatusEffect extends ModStatusEffect implements ToggleableEffect {

	public MagnetismStatusEffect(MobEffectCategory type, int color, boolean isInstant) {
		super(type, color, isInstant);
	}

	@Override
	public boolean applyEffectTick(ServerLevel w, LivingEntity entity, int level) {
		if (!(entity instanceof Player player)) return false;
		if (w.isClientSide() || !PlayerProperties.of(player).isMagnetismEnabled()) return true;
		
		entity.level().getEntitiesOfClass(ItemEntity.class, entity.getBoundingBox().inflate((level + 1) * 5), Predicates.alwaysTrue())
			.stream()
			.map(e -> (ItemEntity) e)
			.filter(e -> e.isAlive() && !e.isRemoved())
			.filter(e -> e.hasPickUpDelay() == entity.isCrouching())
			.forEach(e -> e.playerTouch(player));
		return true;
	}

	@Override
	protected boolean canApplyEffect(int remainingTicks, int level) {
		return true;
	}

	@Override
	public boolean isActive(LivingEntity e) {
		if (e instanceof Player p) {
			return PlayerProperties.of(p).isMagnetismEnabled();
		}
		return false;
	}

}
