package dev.zabi94.extraalchemy.mobEffect.effects;

import dev.zabi94.extraalchemy.mobEffect.ModStatusEffect;
import dev.zabi94.extraalchemy.utils.DimensionalPosition;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;

public class RecallStatusEffect extends ModStatusEffect {

	public RecallStatusEffect(MobEffectCategory type, int color, boolean isInstant) {
		super(type, color, isInstant);
	}

	@Override
	public void onEffectAdded(LivingEntity livingEntity, int amplifier) {
		DimensionalPosition.setRecallPosition(livingEntity, new DimensionalPosition(livingEntity));
	}

	@Override
	public boolean canApplyEffect(int remainingTicks, int level) {
		return remainingTicks == 1;
	}

	@Override
	public boolean applyEffectTick(ServerLevel w, LivingEntity entity, int amplifier) {
		
		if (w.isClientSide()) return false;
		
		LivingEntity ent = entity; //Since the teleport method might clone the entity, this holds the most recent instance of the entity
		DimensionalPosition pos = DimensionalPosition.getRecallPosition(ent);
		try {
			if (pos != null) {
				entity.stopRiding();
				if (!pos.getWorldId().equals(entity.level().dimension().identifier())) {
					if (amplifier > 0) {
						ServerLevel destinationWorld = (ServerLevel) pos.getWorld(w.getServer());
						TeleportTransition destination = new TeleportTransition(destinationWorld, new Vec3(pos.getX(), pos.getY(), pos.getZ()), Vec3.ZERO, ent.getYRot(), ent.getXRot(), TeleportTransition.DO_NOTHING);
						ent = (LivingEntity) entity.teleport(destination);
					} else {
						ent.hurtClient(entity.level().damageSources().magic());
						if (ent instanceof Player player) {
							player.sendOverlayMessage(Component.translatable("message.extraalchemy.recall_damage"));
						}
					}
				} else {
					ent.teleportTo(pos.getX(), pos.getY(), pos.getZ());
				}
			}
		} finally {
			DimensionalPosition.unsetRecallPosition(ent);
		}
		return false;
	}

	@Override
	public void onEffectRemoved(LivingEntity ent) {
		DimensionalPosition.unsetRecallPosition(ent);
	}
	
}
