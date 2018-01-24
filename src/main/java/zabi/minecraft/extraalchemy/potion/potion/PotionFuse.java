package zabi.minecraft.extraalchemy.potion.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import zabi.minecraft.extraalchemy.potion.PotionBase;

public class PotionFuse extends PotionBase {

	public PotionFuse(boolean isBadEffectIn, int liquidColorIn) {
		super(isBadEffectIn, liquidColorIn, "fuse");
		this.setIconIndex(1, 1);
	}
	
	@Override
	public void performEffect(EntityLivingBase e, int amplifier) {
		if (e instanceof EntityPlayer && ((EntityPlayer)e).isSpectator()) return;
		if (!e.getEntityWorld().isRemote) {
            boolean damageEnvironment = e.getEntityWorld().getGameRules().getBoolean("mobGriefing");
            float f = 1.2f+((float)amplifier)*0.6F;
            e.getEntityWorld().createExplosion(null, e.posX, e.posY+1, e.posZ, f, damageEnvironment);
        }
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return duration == 1;
	}

}
