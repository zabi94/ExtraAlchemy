package zabi.minecraft.extraalchemy.statuseffect.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import zabi.minecraft.extraalchemy.entitydata.EntityProperties;
import zabi.minecraft.extraalchemy.statuseffect.ModStatusEffect;
import zabi.minecraft.extraalchemy.utils.DimensionalPosition;

public class RecallStatusEffect extends ModStatusEffect {

	public RecallStatusEffect(StatusEffectCategory type, int color, boolean isInstant) {
		super(type, color, isInstant);
	}

	@Override
	public void onApplied(LivingEntity livingEntity, int amplifier) {
		EntityProperties ep = (EntityProperties) livingEntity;
		if (ep.getRecallPosition() == null) {
			ep.setRecallData(new DimensionalPosition(livingEntity));
		}
	}

	@Override
	public boolean canApplyUpdateEffect(int remainingTicks, int level) {
		return remainingTicks == 1;
	}

	@Override
	public void applyUpdateEffect(LivingEntity entity, int i) {
		if (!entity.getEntityWorld().isClient) {
			LivingEntity ent = entity; //Since the teleport method might clone the entity, this holds the most recent instance of the entity
			DimensionalPosition pos = ((EntityProperties) ent).getRecallPosition();
			try {
				if (pos != null) {
					entity.stopRiding();
					if (!pos.getWorldId().equals(entity.getEntityWorld().getRegistryKey().getValue())) {
						if (i > 0) {
//							ent = FabricDimensions.teleport(entity, (ServerWorld) pos.getWorld(entity.getServer()), (e, server_world, portDir, h, v) -> new PlaceAt(pos));
							return; //TODO remove this and restore teleportation capability once fabric dimension API is available again
						} else {
							ent.damage(entity.getEntityWorld().getDamageSources().magic(), 1f);
							return;
						}
					} else {
						ent.teleport(pos.getX(), pos.getY(), pos.getZ());
					}
				}
			} finally {
				((EntityProperties) ent).setRecallData(null);
			}
		}
	}

	@Override
	public void onEffectRemoved(LivingEntity livingEntity) {
		((EntityProperties) livingEntity).setRecallData(null);
	}

//	public static class PlaceAt extends BlockPattern.TeleportTarget {
//
//		public PlaceAt(DimensionalPosition pos) {
//			super(new Vec3d(pos.getX(), pos.getY(), pos.getZ()), Vec3d.ZERO, 0);
//		}
//		
//	}
	
}
