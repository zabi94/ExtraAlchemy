package zabi.minecraft.extraalchemy.potion.potion;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import zabi.minecraft.extraalchemy.potion.PotionBase;

public class PotionCrumbling extends PotionBase {
	
	public static final ArrayList<Block> whitelist = new ArrayList<Block>();

	public PotionCrumbling(boolean isBadEffectIn, int liquidColorIn) {
		super(isBadEffectIn, liquidColorIn, "crumbling");
		whitelist.add(Blocks.DIRT);
		whitelist.add(Blocks.COBBLESTONE);
		whitelist.add(Blocks.STONE);
		whitelist.add(Blocks.SAND);
		whitelist.add(Blocks.SANDSTONE);
		whitelist.add(Blocks.GRASS);
		whitelist.add(Blocks.NETHERRACK);
		whitelist.add(Blocks.GRAVEL);
		this.setIconIndex(4, 0);
	}

	@Override
	public void performEffect(EntityLivingBase e, int amp) {
		super.performEffect(e, amp);
		if (e instanceof EntityPlayer && ((EntityPlayer)e).isSpectator()) return;
		if (!e.getEntityWorld().isRemote) {
			
			int nextRnd = e.getRNG().nextInt(9);
			
			int dx = (nextRnd/3)-1;
			int dy = (nextRnd%3)-1;
			Block bl = e.getEntityWorld().getBlockState(e.getPosition().add(dx, -1, dy)).getBlock();
			int tries = 0;
			while (bl.equals(Blocks.AIR) && tries<9) {
				tries++;
				nextRnd = (nextRnd+1)%9;
				dx = (nextRnd/3)-1;
				dy = (nextRnd%3)-1;
				bl = e.getEntityWorld().getBlockState(e.getPosition().add(dx, -1, dy)).getBlock();
			}
			
			final Block blf = bl;
			
			if (whitelist.parallelStream().anyMatch(b -> b.equals(blf))) {
				e.getEntityWorld().destroyBlock(e.getPosition().add(dx, -1, dy), true);
			}
		}
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		if (amplifier>4) amplifier = 4;
		return duration % (10-2*amplifier) == 0;
	}

}
