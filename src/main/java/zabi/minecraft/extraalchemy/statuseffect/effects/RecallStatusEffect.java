package zabi.minecraft.extraalchemy.statuseffect.effects;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AbstractEntityAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.world.dimension.DimensionType;
import zabi.minecraft.extraalchemy.entitydata.EntityProperties;
import zabi.minecraft.extraalchemy.statuseffect.ModStatusEffect;
import zabi.minecraft.extraalchemy.utils.DimensionalPosition;

public class RecallStatusEffect extends ModStatusEffect {

	public RecallStatusEffect(StatusEffectType type, int color, boolean isInstant) {
		super(type, color, isInstant);
	}

	@Override
	public void onApplied(LivingEntity livingEntity, AbstractEntityAttributeContainer abstractEntityAttributeContainer, int i) {
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
		if (!entity.world.isClient) {
			LivingEntity ent = entity; //Since the teleport method might clone the entity, this holds the most recent instance of the entity
			DimensionalPosition pos = ((EntityProperties) ent).getRecallPosition();
			try {
				if (pos != null) {
					entity.stopRiding();
					if (pos.getDim() != entity.dimension.getRawId()) {
						if (i > 0) {
							ent = FabricDimensions.teleport(entity, DimensionType.byRawId(pos.getDim()), (e, server_world, portDir, h, v) -> null);
						} else {
							ent.damage(DamageSource.MAGIC, 1f);
							return;
						}
					}
					ent.teleport(pos.getX(), pos.getY(), pos.getZ());
				}
			} finally {
				((EntityProperties) ent).setRecallData(null);
			}
		}
	}

	@Override
	public void onRemoved(LivingEntity livingEntity, AbstractEntityAttributeContainer abstractEntityAttributeContainer, int i) {
		((EntityProperties) livingEntity).setRecallData(null);
	}

}
