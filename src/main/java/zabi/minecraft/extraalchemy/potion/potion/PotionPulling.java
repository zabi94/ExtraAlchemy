package zabi.minecraft.extraalchemy.potion.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;
import zabi.minecraft.extraalchemy.potion.PotionBase;

public class PotionPulling extends PotionBase {

	public PotionPulling(boolean isBadEffectIn, int liquidColorIn) {
		super(isBadEffectIn, liquidColorIn, "pull");
		this.setIconIndex(6, 2);
	}
	
	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}
	
	@Override
	public void performEffect(EntityLivingBase elb, int amplifier) {
		if (elb.world.isRemote) return;
		elb.world.getEntitiesWithinAABB(EntityLivingBase.class, elb.getEntityBoundingBox().grow(2*(1+amplifier)), e -> (!e.equals(elb) && e.getDistance(elb)>1.5f))
			.stream().forEach(e -> pullIn(e, elb, amplifier));
	}

	private void pullIn(EntityLivingBase e, EntityLivingBase elb, int a) {
		Vec3d pull = new Vec3d(e.posX-elb.posX, 0, e.posZ-elb.posZ);
		pull = pull.scale(-0.01);
		pull = pull.scale(1-pull.lengthSquared());
		e.addVelocity(pull.x, pull.y, pull.z);
	}

}
