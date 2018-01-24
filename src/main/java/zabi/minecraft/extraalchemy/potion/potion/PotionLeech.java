package zabi.minecraft.extraalchemy.potion.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import zabi.minecraft.extraalchemy.potion.PotionBase;
import zabi.minecraft.extraalchemy.potion.PotionReference;

public class PotionLeech extends PotionBase {

	public PotionLeech(boolean isBadEffectIn, int liquidColorIn) {
		super(isBadEffectIn, liquidColorIn, "leech");
		this.setIconIndex(0, 2);
	}
	
	public static class PotionLeechHandler {
		
		@SubscribeEvent
		public void onDamageDealt(LivingHurtEvent evt) {
			if (evt.getSource().getTrueSource() instanceof EntityLivingBase) {
				EntityLivingBase elb = (EntityLivingBase) evt.getSource().getTrueSource();
				if (elb.isPotionActive(PotionReference.INSTANCE.LEECH)) {
					if (!evt.getEntityLiving().getEntityWorld().isRemote) {
						elb.heal(1f + 0.5f*elb.getActivePotionEffect(PotionReference.INSTANCE.LEECH).getAmplifier());
					}
				}
			}
		}
	}
}
