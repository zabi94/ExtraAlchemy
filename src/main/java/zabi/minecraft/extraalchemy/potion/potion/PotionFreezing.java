package zabi.minecraft.extraalchemy.potion.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import zabi.minecraft.extraalchemy.blocks.BlockList;
import zabi.minecraft.extraalchemy.potion.PotionInstant;

public class PotionFreezing extends PotionInstant {
	
	public PotionFreezing(boolean isBadEffectIn, int liquidColorIn) {
		super(isBadEffectIn, liquidColorIn, "freezing");
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}

	@Override
	public void applyInstantEffect(EntityLivingBase e, int amp) {
		if (e instanceof EntityPlayer && ((EntityPlayer)e).isSpectator()) return;
		AxisAlignedBB bbx = e.getEntityBoundingBox().grow(1.3);//TODO test
		for (int x = (int)bbx.minX; x < (int)bbx.maxX + 1; x++) {
			for (int y = (int)bbx.minY; y < (int)bbx.maxY + 1; y++) {
				for (int z = (int)bbx.minZ; z < (int)bbx.maxZ + 1; z++) {
					BlockPos bp = new BlockPos(x, y, z);
					if (e.getEntityWorld().isAirBlock(bp) && e.getDistanceSq(bp.down())<5) {
						e.getEntityWorld().setBlockState(bp, BlockList.ENCASING_ICE.getDefaultState());
					}
				}
			}
		}
		
	}
}
