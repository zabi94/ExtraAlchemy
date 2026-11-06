package dev.zabi94.extraalchemy.mobEffect.effects;

import java.util.ArrayList;

import com.google.common.collect.Lists;

import dev.zabi94.extraalchemy.mobEffect.ModStatusEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CrumblingStatusEffect extends ModStatusEffect {

	public CrumblingStatusEffect(MobEffectCategory type, int color, boolean isInstant) {
		super(type, color, isInstant);
	}

	@Override
	protected boolean canApplyEffect(int remainingTicks, int level) {
		return remainingTicks % 7 == 0;
	}

	@Override
	public boolean applyEffectTick(ServerLevel world, LivingEntity entity, int level) {
		
		if (world.isClientSide()) return true;
		
		ArrayList<BlockPos> blocks = Lists.newArrayList();
		int radius = level + 1;
		for (int dx = -radius; dx <= radius; dx++) {
			for (int dz = -radius; dz <= radius; dz++) {
				BlockPos pos = entity.blockPosition().offset(dx, -1, dz);
				BlockState state = entity.level().getBlockState(pos);
				if (!state.isAir()) {
					float hardness = state.getDestroySpeed(entity.level(), pos);
					if (hardness >= 0 && hardness <= level + 1) {
						blocks.add(pos);
					}
				}
			}
		}
		
		if (blocks.size() > 0) {
			BlockPos r = blocks.get(entity.getRandom().nextInt(blocks.size()));
			entity.level().destroyBlock(r, true);
		}
		
		return true;
	}

}
