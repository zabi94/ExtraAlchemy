package zabi.minecraft.extraalchemy.potion.potion;

import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import zabi.minecraft.extraalchemy.potion.PotionBase;
import zabi.minecraft.extraalchemy.potion.PotionReference;

public class PotionPain extends PotionBase {

	public PotionPain(boolean isBadEffectIn, int liquidColorIn) {
		super(isBadEffectIn, liquidColorIn, "pain");
		this.setIconIndex(2, 2);
	}
	
	public static class PotionPainHandler {
		
		@SubscribeEvent
		public void onHurt(LivingHurtEvent evt) {
			PotionEffect pe = evt.getEntityLiving().getActivePotionEffect(PotionReference.INSTANCE.PAIN);
			if (pe != null) {
				evt.setAmount(evt.getAmount() + (evt.getAmount() * 0.2f * (pe.getAmplifier() + 1)));
			}
		}
		
	}

}
