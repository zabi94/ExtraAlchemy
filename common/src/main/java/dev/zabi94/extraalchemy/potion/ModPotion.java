package dev.zabi94.extraalchemy.potion;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.zabi94.extraalchemy.mobEffect.ModStatusEffect;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;

public class ModPotion extends Potion {
	
	private RegistrySupplier<Potion> extended = null;
	private RegistrySupplier<Potion> empowered = null;
	
	protected ModPotion(String name, MobEffectInstance statusEffect) {
		super(name, statusEffect);
	}
	
	public ModPotion getExtended() {
		return (ModPotion) extended.get();
	}
	
	public ModPotion setExtended(RegistrySupplier<Potion> extended) {
		this.extended = extended;
		return this;
	}
	
	public ModPotion getEmpowered() {
		return (ModPotion) empowered.get();
	}
	
	public ModPotion setEmpowered(RegistrySupplier<Potion> empowered) {
		this.empowered = empowered;
		return this;
	}
	
	public static class ModPotionInstant extends ModPotion {
		
		public ModPotionInstant(String name, RegistrySupplier<ModStatusEffect> statusEffect, int amplifier) {
			super(name, new MobEffectInstance(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(statusEffect.get()), 0, amplifier, false, true));
		}

		@Override
		public boolean hasInstantEffects() {
			return true;
		}
		
		public static RegistrySupplier<Potion> generateAll(DeferredRegister<Potion> registry, String name, RegistrySupplier<ModStatusEffect> statusEffect) {
			RegistrySupplier<Potion> empowered = registry.register(name+"_strong", () -> new ModPotionInstant(name, statusEffect, 1));
			return registry.register(name, () -> new ModPotionInstant(name, statusEffect, 0).setEmpowered(empowered));
		}
		
	}
	
	public static class ModPotionTimed extends ModPotion {
		
		protected ModPotionTimed(String name, RegistrySupplier<ModStatusEffect> statusEffect, int length, int amplifier) {
			super(name, new MobEffectInstance(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(statusEffect.get()), length, amplifier, false, true));
		}

		@Override
		public boolean hasInstantEffects() {
			return false;
		}
		
		public static RegistrySupplier<Potion> generateAll(DeferredRegister<Potion> registry, String name, RegistrySupplier<ModStatusEffect> statusEffect, int lengthNormal, int lengthExtended, int lengthEmpowered) {
			RegistrySupplier<Potion> empowered = registry.register(name+"_strong", () -> new ModPotionTimed(name, statusEffect, lengthEmpowered, 1));
			RegistrySupplier<Potion> extended = registry.register(name+"_long", () -> new ModPotionTimed(name, statusEffect, lengthExtended, 0));
			return registry.register(name, () -> new ModPotionTimed(name, statusEffect, lengthNormal, 0).setEmpowered(empowered).setExtended(extended));
		}
		
		public static RegistrySupplier<Potion> generateWithLengthened(DeferredRegister<Potion> registry, String name, RegistrySupplier<ModStatusEffect> statusEffect, int lengthNormal, int lengthExtended) {
			RegistrySupplier<Potion> extended = registry.register(name+"_long", () -> new ModPotionTimed(name, statusEffect, lengthExtended, 0));
			return registry.register(name, () -> new ModPotionTimed(name, statusEffect, lengthNormal, 0).setExtended(extended));
		}
		
	}

}
