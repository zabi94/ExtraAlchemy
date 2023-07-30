package zabi.minecraft.extraalchemy.statuseffect.effects;

import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import zabi.minecraft.extraalchemy.statuseffect.ModStatusEffect;

public class CombustionStatusEffect extends ModStatusEffect {

	public CombustionStatusEffect(StatusEffectCategory type, int color, boolean isInstant) {
		super(type, color, isInstant);
	}

	@Override
	protected boolean canApplyEffect(int remainingTicks, int level) {
		return remainingTicks % Math.max(5, 30/(level+1)) == 0;
	}

	@Override
	public void applyUpdateEffect(LivingEntity e, int level) {
		e.setFireTicks(20);
		World w = e.getEntityWorld();
		if (!w.isClient && w.getGameRules().getBoolean(GameRules.DO_FIRE_TICK)) {
			for (int i = 0; i < 10; i++) {
				Random rnd = e.getRandom();
				int x = (int) Math.round(e.getX()) - 5 + rnd.nextInt(12);
				int y = (int) Math.round(e.getY()) - 5 + rnd.nextInt(12);
				int z = (int) Math.round(e.getZ()) - 5 + rnd.nextInt(12);
				if (y < w.getBottomY()) y = w.getBottomY();
				if (y > w.getHeight()) {
					y = w.getHeight() - 1;
				}
				BlockPos pos = new BlockPos(x, y, z);
				if (w.getBlockState(pos).isReplaceable() && Blocks.FIRE.getDefaultState().canPlaceAt(w, pos)) {
					w.setBlockState(pos, FireBlock.getState(w, pos), 3);
					break;
				}
			}
		}
	}


}
