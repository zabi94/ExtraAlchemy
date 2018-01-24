package zabi.minecraft.extraalchemy.potion.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import zabi.minecraft.extraalchemy.potion.PotionBase;

public class PotionSinking extends PotionBase {

	public PotionSinking(boolean isBadEffectIn, int liquidColorIn) {
		super(isBadEffectIn, liquidColorIn, "sinking");
		this.setIconIndex(2, 0);
	}

	@Override
	public void performEffect(EntityLivingBase e, int amplifier) {
		super.performEffect(e, amplifier);
		if (e instanceof EntityPlayer && ((EntityPlayer)e).isSpectator()) return;
		if (!e.onGround && e.isInWater()) {
			e.addVelocity(0, -(0.01D+(amplifier*0.007D)), 0);
		}
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}

}
