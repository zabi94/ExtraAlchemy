package zabi.minecraft.extraalchemy.potion.potion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import zabi.minecraft.extraalchemy.lib.Log;
import zabi.minecraft.extraalchemy.lib.Utils;
import zabi.minecraft.extraalchemy.potion.PotionBase;
import zabi.minecraft.extraalchemy.potion.PotionReference;

public class PotionRecall extends PotionBase {
	


	public PotionRecall(boolean isBadEffectIn, int liquidColorIn) {
		super(isBadEffectIn, liquidColorIn, "recall");
		this.setIconIndex(3, 0);
	}

	@Override
	public void performEffect(EntityLivingBase e, int modifier) {
		if (e instanceof EntityPlayer && ((EntityPlayer)e).isSpectator()) return;
		if (!e.getEntityWorld().isRemote) try {
			
			Log.d("Applying recall to "+e);
			Log.d(e.getEntityData());
			
			NBTTagCompound pos = e.getEntityData().getCompoundTag("recall");
			if (e.dimension!=pos.getInteger("dim")) {
				if(modifier==0) e.attackEntityFrom(DamageSource.GENERIC, 4F);
				else {
					if (e instanceof EntityPlayer) {
						Utils.teleportThroughDimensions((EntityPlayer) e, pos.getInteger("dim"), pos.getDouble("posX"), pos.getDouble("posY"), pos.getDouble("posZ"));
					} else {
						e.dismountRidingEntity();
						e.changeDimension(pos.getInteger("dim"));
						e.setPositionAndUpdate(pos.getDouble("posX"), pos.getDouble("posY"), pos.getDouble("posZ"));
					}
			}
			} else {
				e.dismountRidingEntity();
				e.setPositionAndUpdate(pos.getDouble("posX"), pos.getDouble("posY"), pos.getDouble("posZ"));
			}
			
		} catch (Exception er) {
			Log.e(er.getMessage());
		} finally {
			try {
				e.getEntityData().removeTag("recall");
			} catch (Exception ex2) {}
		}
		e.getEntityData().removeTag("recall");
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return duration==1;
	}

	@Override
	public void applyAttributesModifiersToEntity(EntityLivingBase e, AbstractAttributeMap attributeMapIn, int amplifier) {
		super.applyAttributesModifiersToEntity(e, attributeMapIn, amplifier);
		savePosition(e);
	}
	
	@Override
	public void affectEntity(Entity source, Entity indirectSource, EntityLivingBase e, int amplifier, double health) {
		super.affectEntity(source, indirectSource, e, amplifier, health);
		if (e instanceof EntityPlayer && ((EntityPlayer)e).isSpectator()) return;
		savePosition(e);
	}

	private void savePosition(EntityLivingBase e) {
		if (e instanceof EntityPlayer && ((EntityPlayer)e).isSpectator()) return;
		if (!e.getEntityData().hasKey("recall") || !e.isPotionActive(PotionReference.INSTANCE.RECALL)) {
			NBTTagCompound position = new NBTTagCompound();
			position.setDouble("posX", e.posX);
			position.setDouble("posY", e.posY);
			position.setDouble("posZ", e.posZ);
			position.setInteger("dim", e.dimension);
			e.getEntityData().setTag("recall", position);
		}
	}

	@Override
	public void removeAttributesModifiersFromEntity(EntityLivingBase e, AbstractAttributeMap attributeMapIn, int amplifier) {
		super.removeAttributesModifiersFromEntity(e, attributeMapIn, amplifier);
		if (e instanceof EntityPlayer && ((EntityPlayer)e).isSpectator()) return;
		e.getEntityData().removeTag("recall");
	}

}
