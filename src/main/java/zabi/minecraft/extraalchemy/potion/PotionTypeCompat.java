package zabi.minecraft.extraalchemy.potion;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import zabi.minecraft.extraalchemy.lib.Reference;

public class PotionTypeCompat extends PotionType {
	
	String mod = "Unknown";
	
	public PotionTypeCompat(Potion potion, int duration, int amplifier, String effectName, String originMod) {
		super(effectName, new PotionEffect(potion, duration, amplifier));
		this.setRegistryName(new ResourceLocation(Reference.MID, effectName));
		mod = originMod;
	}
	
	public String getMod() {
		return mod;
	}
	
}
