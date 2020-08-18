package zabi.minecraft.extraalchemy.potion;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.util.registry.Registry;

public class ModPotion extends Potion {
	
	private ModPotion extended = null;
	private ModPotion empowered = null;
	private boolean registered = false;
	
	protected ModPotion(String name, StatusEffectInstance statusEffect) {
		super(name, statusEffect);
	}
	
	public ModPotion getExtended() {
		return extended;
	}
	
	public void setExtended(ModPotion extended) {
		this.extended = extended;
	}
	
	public ModPotion getEmpowered() {
		return empowered;
	}
	
	public void setEmpowered(ModPotion empowered) {
		this.empowered = empowered;
	}
	
	public int registerTree(String namespace, String base) {
		if (registered) return 0;
		Registry.register(Registry.POTION, namespace+":"+base, this);
		registered = true;
		int registeredTotal = 1;
		if (extended != null) {
			registeredTotal += extended.registerTree(namespace, base+"_long");
		}
		
		if (empowered != null) {
			registeredTotal += empowered.registerTree(namespace, base+"_strong");
		}
		
		return registeredTotal;
	}
	
	public static class ModPotionInstant extends ModPotion {
		
		public ModPotionInstant(String name, StatusEffect statusEffect, int amplifier) {
			super(name, new StatusEffectInstance(statusEffect, 0, amplifier, false, true));
		}

		@Override
		public boolean hasInstantEffect() {
			return true;
		}
		
		public static ModPotionInstant generateAll(String name, StatusEffect statusEffect) {
			ModPotionInstant base = new ModPotionInstant(name, statusEffect, 0);
			base.setEmpowered(new ModPotionInstant(name, statusEffect, 1));
			return base;
		}
		
	}
	
	public static class ModPotionTimed extends ModPotion {
		
		protected ModPotionTimed(String name, StatusEffect statusEffect, int length, int amplifier) {
			super(name, new StatusEffectInstance(statusEffect, length, amplifier, false, true));
		}

		@Override
		public boolean hasInstantEffect() {
			return false;
		}
		
		public static ModPotionTimed generateAll(String name, StatusEffect statusEffect, int lengthNormal, int lengthExtended, int lengthEmpowered) {
			ModPotionTimed base = new ModPotionTimed(name, statusEffect, lengthNormal, 0);
			base.setEmpowered(new ModPotionTimed(name, statusEffect, lengthEmpowered, 1));
			base.setExtended(new ModPotionTimed(name, statusEffect, lengthExtended, 0));
			return base;
		}
		
		public static ModPotionTimed generateWithLengthened(String name, StatusEffect statusEffect, int lengthNormal, int lengthExtended) {
			ModPotionTimed base = new ModPotionTimed(name, statusEffect, lengthNormal, 0);
			base.setEmpowered(null);
			base.setExtended(new ModPotionTimed(name, statusEffect, lengthExtended, 0));
			return base;
		}
		
	}

}
