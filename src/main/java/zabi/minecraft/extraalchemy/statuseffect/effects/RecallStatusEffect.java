package zabi.minecraft.extraalchemy.statuseffect.effects;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import zabi.minecraft.extraalchemy.entitydata.ModEntityData;
import zabi.minecraft.extraalchemy.statuseffect.ModStatusEffect;
import zabi.minecraft.extraalchemy.utils.DimensionalPosition;

public class RecallStatusEffect extends ModStatusEffect {

	public RecallStatusEffect(StatusEffectCategory type, int color, boolean isInstant) {
		super(type, color, isInstant);
	}

	@Override
	public void onApplied(LivingEntity livingEntity, int amplifier) {
		livingEntity.setAttached(ModEntityData.RECALL_POSITION, new DimensionalPosition(livingEntity));
	}

	@Override
	public boolean canApplyUpdateEffect(int remainingTicks, int level) {
		return remainingTicks == 1;
	}

	@Override
	public boolean applyUpdateEffect(LivingEntity entity, int i) {
		if (!entity.getEntityWorld().isClient) {
			LivingEntity ent = entity; //Since the teleport method might clone the entity, this holds the most recent instance of the entity
			DimensionalPosition pos = ent.getAttachedOrElse(ModEntityData.RECALL_POSITION, null);
			try {
				if (pos != null) {
					entity.stopRiding();
					if (!pos.getWorldId().equals(entity.getEntityWorld().getRegistryKey().getValue())) {
						if (i > 0) {
							ServerWorld destinationWorld = (ServerWorld) pos.getWorld(entity.getServer());
							ent = FabricDimensions.teleport(entity, destinationWorld, new TeleportTarget(new Vec3d(pos.getX(), pos.getY(), pos.getZ()), Vec3d.ZERO, ent.getYaw(), ent.getPitch()));	
						} else {
							ent.damage(entity.getEntityWorld().getDamageSources().magic(), 1f);
							if (ent instanceof PlayerEntity player) {
								player.sendMessage(Text.translatable("message.extraalchemy.recall_damage"), true);
							}
						}
					} else {
						ent.teleport(pos.getX(), pos.getY(), pos.getZ());
					}
				}
			} finally {
				ent.removeAttached(ModEntityData.RECALL_POSITION);
			}
		}
		return false;
	}

	@Override
	public void onEffectRemoved(LivingEntity ent) {
		ent.removeAttached(ModEntityData.RECALL_POSITION);
	}

//	public static class PlaceAt extends BlockPattern.TeleportTarget {
//
//		public PlaceAt(DimensionalPosition pos) {
//			super(new Vec3d(pos.getX(), pos.getY(), pos.getZ()), Vec3d.ZERO, 0);
//		}
//		
//	}
	
}
