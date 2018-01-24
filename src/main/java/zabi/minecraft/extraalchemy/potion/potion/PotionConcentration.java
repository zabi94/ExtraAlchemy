package zabi.minecraft.extraalchemy.potion.potion;

import java.util.ArrayList;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import zabi.minecraft.extraalchemy.potion.PotionInstant;

public class PotionConcentration extends PotionInstant {

	public PotionConcentration(boolean isBadEffectIn, int liquidColorIn) {
		super(isBadEffectIn, liquidColorIn, "concentration");
	}

	@Override
	public void applyInstantEffect(EntityLivingBase e, int amp) {
		ArrayList<PotionEffect> effects = new ArrayList<PotionEffect>();
		effects.addAll(e.getActivePotionEffects());
		NBTTagCompound tag=null;
		if (e.getEntityData().hasKey("recall")) tag = e.getEntityData().getCompoundTag("recall");
		effects.stream().filter(pf -> pf.getPotion()!=this).forEach(pe -> {
			e.removePotionEffect(pe.getPotion());
			e.addPotionEffect(new PotionEffect(pe.getPotion(), pe.getDuration(), pe.getAmplifier(), pe.getIsAmbient(), false));
		});
		if (tag!=null) e.getEntityData().setTag("recall", tag);
	}
}
