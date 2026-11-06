package dev.zabi94.extraalchemy.mobEffect.effects;

import dev.zabi94.extraalchemy.mobEffect.ModStatusEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.gamerules.GameRules;

public class CombustionStatusEffect extends ModStatusEffect {

	public CombustionStatusEffect(MobEffectCategory type, int color, boolean isInstant) {
		super(type, color, isInstant);
	}

	@Override
	protected boolean canApplyEffect(int remainingTicks, int level) {
		return remainingTicks % Math.max(5, 30/(level+1)) == 0;
	}

	@Override
	public boolean applyEffectTick(ServerLevel w, LivingEntity e, int amplifier) {
		if (w.isClientSide() || w.getGameRules().get(GameRules.FIRE_SPREAD_RADIUS_AROUND_PLAYER) == 0) return true;

		for (int i = 0; i < 10; i++) {
			RandomSource rnd = e.getRandom();
			int x = (int) Math.round(e.getX()) - 5 + rnd.nextInt(12);
			int y = (int) Math.round(e.getY()) - 5 + rnd.nextInt(12);
			int z = (int) Math.round(e.getZ()) - 5 + rnd.nextInt(12);
			if (y < w.getMinY()) y = w.getMinY();
			if (y > w.getHeight()) {
				y = w.getHeight() - 1;
			}
			BlockPos pos = new BlockPos(x, y, z);
			if (w.getBlockState(pos).canBeReplaced() && Blocks.FIRE.defaultBlockState().canSurvive(w, pos)) {
				w.setBlock(pos, FireBlock.getState(w, pos), 3);
				break;
			}
		}

		return true;
	}


}
