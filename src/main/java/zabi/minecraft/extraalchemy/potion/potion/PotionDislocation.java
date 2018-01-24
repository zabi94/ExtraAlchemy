package zabi.minecraft.extraalchemy.potion.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import zabi.minecraft.extraalchemy.potion.PotionBase;

public class PotionDislocation extends PotionBase {

	public PotionDislocation(boolean isBadEffectIn, int liquidColorIn) {
		super(isBadEffectIn, liquidColorIn, "dislocation");
		this.setIconIndex(2, 1);
	}

	@Override
	public void performEffect(EntityLivingBase e, int potLevel) {
		super.performEffect(e, potLevel);
		if (e instanceof EntityPlayer && ((EntityPlayer)e).isSpectator()) return;
        double x = e.posX + (e.getRNG().nextDouble() - 0.5D) * 16.0D;
        double y = clamp(e.posY + (double)(e.getRNG().nextInt(16) - 8), 0.0D, (double)(e.getEntityWorld().getActualHeight() - 1));
        double z = e.posZ + (e.getRNG().nextDouble() - 0.5D) * 16.0D;
		teleportTo(x, y, z, e);
	}

	private double clamp(double num, double min, double max) {
	        return num < min ? min : (num > max ? max : num);
	}

	@Override
	public boolean isInstant() {
		return false;
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		int factor = 20 - (5*amplifier);
		if (factor<=0) return false;
		return duration % factor == 1;
	}

	
	private static void teleportTo(double x, double y, double z, EntityLivingBase e) {
        net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(e, x, y, z, 0);
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return;
        boolean flag = e.attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ());
        if (flag) {
            e.getEntityWorld().playSound((EntityPlayer)null, e.prevPosX, e.prevPosY, e.prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, e.getSoundCategory(), 1.0F, 1.0F);
            e.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
        }
    }
}
