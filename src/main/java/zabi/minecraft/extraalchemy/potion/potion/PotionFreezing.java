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
		AxisAlignedBB bbx = e.getEntityBoundingBox();
		for (int x = (int) Math.floor(bbx.minX); x < (int) Math.ceil(bbx.maxX); x++) {
			for (int y = (int) Math.floor(bbx.minY); y < (int) Math.ceil(bbx.maxY); y++) {
				for (int z = (int) Math.floor(bbx.minZ); z < (int) Math.ceil(bbx.maxZ); z++) {
					BlockPos bp = new BlockPos(x, y, z);
					if (e.getEntityWorld().isAirBlock(bp)) {
						e.getEntityWorld().setBlockState(bp, BlockList.ENCASING_ICE.getDefaultState());
					}
				}
			}
		}
		
	}
}
