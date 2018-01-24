package zabi.minecraft.extraalchemy.potion.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.BlockPos;
import zabi.minecraft.extraalchemy.potion.PotionInstant;

public class PotionReturn extends PotionInstant {

	public PotionReturn(boolean isBadEffectIn, int liquidColorIn) {
		super(isBadEffectIn, liquidColorIn, "return");
	}
	
	@Override
	public void applyInstantEffect(EntityLivingBase e, int amp) {
		if (!e.getEntityWorld().isRemote && e instanceof EntityPlayer) {

			if (((EntityPlayer)e).getBedLocation()!=null) {
				BlockPos l= EntityPlayer.getBedSpawnLocation(e.getEntityWorld(), ((EntityPlayer)e).getBedLocation(), false);
				if (l!=null) {
					e.setPositionAndUpdate(l.getX(), l.getY()+0.5, l.getZ());
				}
			}
			e.getEntityWorld().playSound(null, e.prevPosX, e.prevPosY, e.prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, e.getSoundCategory(), 1.0F, 1.0F);
            e.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
		}
	}
}
