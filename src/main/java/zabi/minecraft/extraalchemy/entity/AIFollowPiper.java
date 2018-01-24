package zabi.minecraft.extraalchemy.entity;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigateGround;
import zabi.minecraft.extraalchemy.potion.PotionReference;

public class AIFollowPiper extends EntityAIBase {
	
	private EntityPlayer piper;
	private EntityCreature animal;
	
	public AIFollowPiper(EntityCreature temptedEntityIn) {
		this.animal = temptedEntityIn;
        this.setMutexBits(3);
        if (!(temptedEntityIn.getNavigator() instanceof PathNavigateGround)) {
            throw new IllegalArgumentException("Unsupported mob type for TemptGoal");
        }
	}

	
	protected boolean isTempting(@Nullable EntityPlayer p)  {
        return p.getActivePotionEffect(PotionReference.INSTANCE.PIPER)!=null;
    }
	
	@Override
	public boolean shouldExecute() {
		this.piper = this.animal.getEntityWorld().getClosestPlayerToEntity(this.animal, 10.0D);
		return this.piper == null ? false : this.isTempting(piper);
	}
	
	@Override
	public void resetTask() {
        this.animal.getNavigator().clearPath();
		piper = null;
	}
	
	public boolean continueExecuting() {
        return this.shouldExecute();
    }
	
	public void updateTask()  {
        this.animal.getLookHelper().setLookPositionWithEntity(this.piper, (float)(this.animal.getHorizontalFaceSpeed() + 20), (float)this.animal.getVerticalFaceSpeed());
        if (this.animal.getDistanceSq(this.piper) < 7D) {
            this.animal.getNavigator().clearPath();
        } else {
            this.animal.getNavigator().tryMoveToEntityLiving(this.piper, 1+this.piper.getActivePotionEffect(PotionReference.INSTANCE.PIPER).getAmplifier()*0.6);
        }
    }
	
}
