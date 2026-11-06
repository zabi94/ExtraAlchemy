package dev.zabi94.extraalchemy.potion;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.zabi94.extraalchemy.mobEffect.ModEffectRegistry;
import dev.zabi94.extraalchemy.utils.LibMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.alchemy.Potion;

public class ModPotionRegistry {

	public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(LibMod.MOD_ID, Registries.POTION);

	public static RegistrySupplier<Potion> crumbling = ModPotion.ModPotionTimed.generateAll(POTIONS, "crumbling", ModEffectRegistry.crumbling, 20*60, 20*100, 20*40);
	public static RegistrySupplier<Potion> fuse = ModPotion.ModPotionTimed.generateAll(POTIONS, "fuse", ModEffectRegistry.fuse, 20*20, 20*10, 20*30);
	public static RegistrySupplier<Potion> magnetism = ModPotion.ModPotionTimed.generateAll(POTIONS, "magnetism", ModEffectRegistry.magnetism, 20*180, 20*240, 20*120);
	public static RegistrySupplier<Potion> photosynthesis = ModPotion.ModPotionTimed.generateAll(POTIONS, "photosynthesis", ModEffectRegistry.photosynthesis, 20*80, 20*100, 20*60);
	public static RegistrySupplier<Potion> recall = ModPotion.ModPotionTimed.generateAll(POTIONS, "recall", ModEffectRegistry.recall, 20*60, 20*80, 20*40);
	public static RegistrySupplier<Potion> sails = ModPotion.ModPotionTimed.generateAll(POTIONS, "sails", ModEffectRegistry.sails, 20*60*2, 20*60*4, 20*60);
	public static RegistrySupplier<Potion> learning = ModPotion.ModPotionTimed.generateAll(POTIONS, "learning", ModEffectRegistry.learning, 20*60*4, 20*60*6, 20*60*2);
	public static RegistrySupplier<Potion> gravity = ModPotion.ModPotionTimed.generateAll(POTIONS, "gravity", ModEffectRegistry.gravity, 20*60, 20*60*2, 20*30);
	public static RegistrySupplier<Potion> combustion = ModPotion.ModPotionTimed.generateAll(POTIONS, "combustion", ModEffectRegistry.combustion, 20*30, 20*50, 20*15);
	public static RegistrySupplier<Potion> pacifism = ModPotion.ModPotionTimed.generateAll(POTIONS, "pacifism", ModEffectRegistry.pacifism, 20*25, 20*40, 20*15);
	public static RegistrySupplier<Potion> piper = ModPotion.ModPotionTimed.generateWithLengthened(POTIONS, "piper", ModEffectRegistry.piper, 20*120, 20*240);
	public static RegistrySupplier<Potion> detection = ModPotion.ModPotionTimed.generateAll(POTIONS, "detection", ModEffectRegistry.detection, 20*60, 20*120, 20*30);
	
	public static RegistrySupplier<Potion> returning = POTIONS.register("returning", () -> new ModPotion.ModPotionInstant("returning", ModEffectRegistry.returning, 0));
	public static RegistrySupplier<Potion> concentration = POTIONS.register("concentration", () -> new ModPotion.ModPotionInstant("concentration", ModEffectRegistry.concentration, 0));

	public static void registerAll() {
		POTIONS.register();
	}

}
