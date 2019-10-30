package zabi.minecraft.extraalchemy.potion.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import zabi.minecraft.extraalchemy.potion.NoncontinuousEffect;
import zabi.minecraft.extraalchemy.potion.PotionBase;

public class PotionPhotosynthesis extends PotionBase implements NoncontinuousEffect {

	public PotionPhotosynthesis(boolean isBadEffectIn, int liquidColorIn) {
		super(isBadEffectIn, liquidColorIn, "photosynthesis");
		this.setIconIndex(0, 1);
	}

	@Override
	public void performEffect(EntityLivingBase e, int amp) {
		super.performEffect(e, amp);
		if (e instanceof EntityPlayer && ((EntityPlayer)e).isSpectator()) return;
		if (!e.getEntityWorld().isRemote && isEffectActive(e)) {
			if (e instanceof EntityPlayer) {
				EntityPlayer p = (EntityPlayer) e;
				p.getFoodStats().addStats(1, 0);
			} else {
				e.removeActivePotionEffect(this);
			}
		}
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return (duration % ( (amplifier>0) ? 40 : 80 ) )==1;
	}

	@Override
	public boolean isEffectActive(EntityLivingBase e) {
		return e.getEntityWorld().canBlockSeeSky(e.getPosition()) && e.getEntityWorld().isDaytime() && !e.getEntityWorld().isRaining();
	}

}
