package zabi.minecraft.extraalchemy.potion.potion;

import java.lang.reflect.Field;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import zabi.minecraft.extraalchemy.potion.PotionBase;

@SuppressWarnings("deprecation")
public class PotionSails extends PotionBase {
	
	private Field status = ReflectionHelper.findField(EntityBoat.class, "status", "field_184469_aF");

	public PotionSails(boolean isBadEffectIn, int liquidColorIn) {
		super(isBadEffectIn, liquidColorIn, "sails");
		this.setIconIndex(1, 2);
	}

	@Override
	public void performEffect(EntityLivingBase e, int amp) {
		super.performEffect(e, amp);
		try {
			if (e instanceof EntityPlayer && e.getRidingEntity() instanceof EntityBoat) {
				EntityBoat b = (EntityBoat) e.getRidingEntity();
				if (status.get(b) != EntityBoat.Status.ON_LAND) {
					float f=0;
					if (((EntityPlayer)e).moveForward>0) f = 0.05f + (0.05f * amp);
					b.motionX += (double)(MathHelper.sin(-b.rotationYaw * 0.017453292F) * f);
					b.motionZ += (double)(MathHelper.cos(b.rotationYaw * 0.017453292F) * f);
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}

}
