package zabi.minecraft.extraalchemy.potion.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;
import zabi.minecraft.extraalchemy.potion.PotionBase;

public class PotionPushing extends PotionBase {

	public PotionPushing(boolean isBadEffectIn, int liquidColorIn) {
		super(isBadEffectIn, liquidColorIn, "push");
		this.setIconIndex(5, 2);
	}
	
	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}
	
	@Override
	public void performEffect(EntityLivingBase elb, int amplifier) {
		if (elb.world.isRemote) return;
		elb.world.getEntitiesWithinAABB(EntityLivingBase.class, elb.getEntityBoundingBox().grow(2*(1+amplifier)), e -> !e.equals(elb))
			.stream().forEach(e -> pushAway(e, elb, amplifier));
	}

	private void pushAway(EntityLivingBase e, EntityLivingBase elb, int a) {
		Vec3d push = new Vec3d(e.posX-elb.posX, 0, e.posZ-elb.posZ);
		push = push.scale(0.2*(1d/push.lengthSquared()));
		e.addVelocity(push.x, push.y, push.z);
	}

}
