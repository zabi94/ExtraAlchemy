package zabi.minecraft.extraalchemy.statuseffect.effects;

import java.util.ArrayList;

import com.google.common.collect.Lists;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.math.BlockPos;
import zabi.minecraft.extraalchemy.statuseffect.ModStatusEffect;

public class CrumblingStatusEffect extends ModStatusEffect {

	public CrumblingStatusEffect(StatusEffectType type, int color, boolean isInstant) {
		super(type, color, isInstant);
	}

	@Override
	protected boolean canApplyEffect(int remainingTicks, int level) {
		return remainingTicks % 7 == 0;
	}

	@Override
	public void applyUpdateEffect(LivingEntity entity, int level) {
		if (!entity.world.isClient) {
			ArrayList<BlockPos> blocks = Lists.newArrayList();
			int radius = level + 1;
			for (int dx = -radius; dx <= radius; dx++) {
				for (int dz = -radius; dz <= radius; dz++) {
					BlockPos pos = entity.getBlockPos().add(dx, -1, dz);
					BlockState state = entity.world.getBlockState(pos);
					if (!state.isAir()) {
						float hardness = state.getHardness(entity.world, pos);
						if (hardness >= 0 && hardness <= level + 1) {
							blocks.add(pos);
						}
					}
				}
			}
			if (blocks.size() > 0) {
				BlockPos r = blocks.get(entity.getRandom().nextInt(blocks.size()));
				entity.world.breakBlock(r, true);
			}
		}
	}

}
