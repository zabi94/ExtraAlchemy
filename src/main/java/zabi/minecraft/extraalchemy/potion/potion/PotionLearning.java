package zabi.minecraft.extraalchemy.potion.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import zabi.minecraft.extraalchemy.potion.PotionBase;

public class PotionLearning extends PotionBase {

	public PotionLearning(boolean isBadEffectIn, int liquidColorIn) {
		super(isBadEffectIn, liquidColorIn, "learning");
		this.setIconIndex(6, 1);
	}

	@Override
	public void performEffect(EntityLivingBase e, int amp) {
		super.performEffect(e, amp);
		if (e instanceof EntityPlayer) {
			EntityPlayer p = (EntityPlayer) e;
			if (!p.isSpectator()) {
				int rd = 2+2*amp;
				p.getEntityWorld().getEntitiesWithinAABB(EntityXPOrb.class, p.getEntityBoundingBox().expand(rd, rd, rd)).stream().forEach(xp -> {
					xp.delayBeforeCanPickup=0;
					xp.xpValue*=1.1;
					xp.onCollideWithPlayer(p);
					p.xpCooldown = 0;});
			}
		}
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}

}
