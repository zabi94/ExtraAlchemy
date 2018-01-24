package zabi.minecraft.extraalchemy.potion.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import zabi.minecraft.extraalchemy.potion.PotionBase;

public class PotionGravity extends PotionBase {

	public PotionGravity(boolean isBadEffectIn, int liquidColorIn) {
		super(isBadEffectIn, liquidColorIn, "gravity");
		this.setIconIndex(7, 1);
	}

	@Override
	public void performEffect(EntityLivingBase e, int amplifier) {
		if (e instanceof EntityPlayer && ((EntityPlayer)e).isSpectator()) return;
		if (!e.getEntityWorld().isRemote && !e.isInWater() && !e.isRiding() && !e.onGround) {
			e.addVelocity(0, -0.07*amplifier, 0);
		}
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}
	
	

}
