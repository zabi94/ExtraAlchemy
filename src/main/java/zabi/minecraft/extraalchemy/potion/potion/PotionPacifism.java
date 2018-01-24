package zabi.minecraft.extraalchemy.potion.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import zabi.minecraft.extraalchemy.lib.Log;
import zabi.minecraft.extraalchemy.potion.PotionBase;
import zabi.minecraft.extraalchemy.potion.PotionReference;

public class PotionPacifism extends PotionBase {

	public PotionPacifism(boolean isBadEffectIn, int liquidColorIn) {
		super(isBadEffectIn, liquidColorIn, "pacifism");
		this.setIconIndex(0, 0);
	}

	public static class PacifismHandler {
		
		public PacifismHandler() {
			Log.d("Registering Pacifism Handler");
		}
		
		@SubscribeEvent
		public void onEntityHit(LivingHurtEvent event) {
			if (event.getSource().getTrueSource()==null) return; //Non PvLiving damage is not protected
			PotionEffect hurtPacifism = event.getEntityLiving().getActivePotionEffect(PotionReference.INSTANCE.PACIFISM);
			if (hurtPacifism!=null) { //Se l'entità colpita HA pacifismo
				if (event.getSource().getTrueSource() instanceof EntityLivingBase) {
					PotionEffect slowMod = new PotionEffect(PotionTypes.SLOWNESS.getEffects().get(0).getPotion(), 200, hurtPacifism.getAmplifier()+1);
					((EntityLivingBase)event.getSource().getTrueSource()).addPotionEffect(slowMod);
				}
			}
			
		}
		
	}
}
