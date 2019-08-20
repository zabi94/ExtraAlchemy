package zabi.minecraft.extraalchemy.potion.potion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import zabi.minecraft.extraalchemy.entity.AIFollowPiper;
import zabi.minecraft.extraalchemy.potion.PotionBase;

public class PotionPiper extends PotionBase {
	
	public PotionPiper(boolean isBadEffectIn, int liquidColorIn) {
		super(isBadEffectIn, liquidColorIn, "piper");
		this.setIconIndex(1, 0);
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return duration%20==0;
	}

	@Override
	public void performEffect(EntityLivingBase e, int p_76394_2_) {
		super.performEffect(e, p_76394_2_);
		applyAI(e);
	}

	@Override
	public void affectEntity(Entity source, Entity indirectSource, EntityLivingBase e, int amplifier, double health) {
		super.affectEntity(source, indirectSource, e, amplifier, health);
		applyAI(e);
	}
	
	private void applyAI(EntityLivingBase e) {
		if (e instanceof EntityPlayer && ((EntityPlayer)e).isSpectator()) return;
		if (e instanceof EntityPlayer && !e.getEntityWorld().isRemote) {
			e.getEntityWorld().getEntitiesWithinAABB(EntityAnimal.class, e.getEntityBoundingBox().expand(10, 2, 10)).parallelStream().forEach(a -> {
				if (a.tasks.taskEntries.stream().noneMatch(ett -> ett.action instanceof AIFollowPiper)) a.tasks.addTask(3, new AIFollowPiper(a));
			});
		}
	}

}
