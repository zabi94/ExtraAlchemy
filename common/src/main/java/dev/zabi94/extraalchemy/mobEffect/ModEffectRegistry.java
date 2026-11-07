package dev.zabi94.extraalchemy.mobEffect;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.zabi94.extraalchemy.mobEffect.effects.CombustionStatusEffect;
import dev.zabi94.extraalchemy.mobEffect.effects.ConcentrationStatusEffect;
import dev.zabi94.extraalchemy.mobEffect.effects.CrumblingStatusEffect;
import dev.zabi94.extraalchemy.mobEffect.effects.EmptyStatusEffect;
import dev.zabi94.extraalchemy.mobEffect.effects.FuseStatusEffect;
import dev.zabi94.extraalchemy.mobEffect.effects.GravityStatusEffect;
import dev.zabi94.extraalchemy.mobEffect.effects.LearningStatusEffect;
import dev.zabi94.extraalchemy.mobEffect.effects.MagnetismStatusEffect;
import dev.zabi94.extraalchemy.mobEffect.effects.PhotosynthesisStatusEffect;
import dev.zabi94.extraalchemy.mobEffect.effects.RecallStatusEffect;
import dev.zabi94.extraalchemy.mobEffect.effects.ReturnStatusEffect;
import dev.zabi94.extraalchemy.mobEffect.effects.SailsStatusEffect;
import dev.zabi94.extraalchemy.utils.LibMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class ModEffectRegistry {
	
	public static final int MAGNETISM_COLOR = 0xb8b8b8;

	public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(LibMod.MOD_ID, Registries.MOB_EFFECT);

	public static final RegistrySupplier<MobEffect> magnetism = EFFECTS.register("magnetism", () -> new MagnetismStatusEffect(MobEffectCategory.BENEFICIAL, MAGNETISM_COLOR, false).setRegistered());
	public static final RegistrySupplier<MobEffect> photosynthesis = EFFECTS.register("photosynthesis", () -> new PhotosynthesisStatusEffect(MobEffectCategory.BENEFICIAL, 0x3cbd19, false).setRegistered());
	public static final RegistrySupplier<MobEffect> crumbling = EFFECTS.register("crumbling", () -> new CrumblingStatusEffect(MobEffectCategory.NEUTRAL, 0x794044, false).setRegistered());
	public static final RegistrySupplier<MobEffect> fuse = EFFECTS.register("fuse", () -> new FuseStatusEffect(MobEffectCategory.HARMFUL, 0xcc3333, false).setRegistered());
	public static final RegistrySupplier<MobEffect> recall = EFFECTS.register("recall", () -> new RecallStatusEffect(MobEffectCategory.BENEFICIAL, 0xFFF200, false).setRegistered());
	public static final RegistrySupplier<MobEffect> sails = EFFECTS.register("sails", () -> new SailsStatusEffect(MobEffectCategory.BENEFICIAL, 0x9BD8FF, false).setRegistered());
	public static final RegistrySupplier<MobEffect> learning = EFFECTS.register("learning", () -> new LearningStatusEffect(MobEffectCategory.BENEFICIAL, 0xD5FF39, false).setRegistered());
	public static final RegistrySupplier<MobEffect> gravity = EFFECTS.register("gravity", () -> new GravityStatusEffect(MobEffectCategory.NEUTRAL, 0x815D09, false).setRegistered());
	public static final RegistrySupplier<MobEffect> combustion = EFFECTS.register("combustion", () -> new CombustionStatusEffect(MobEffectCategory.HARMFUL, 0xF96501, false).setRegistered());
	public static final RegistrySupplier<MobEffect> pacifism = EFFECTS.register("pacifism", () -> new EmptyStatusEffect(MobEffectCategory.BENEFICIAL, 0xFEFFE7, false).setRegistered());
	public static final RegistrySupplier<MobEffect> piper = EFFECTS.register("piper", () -> new EmptyStatusEffect(MobEffectCategory.BENEFICIAL, 0xFFDF9F, false).setRegistered());
	public static final RegistrySupplier<MobEffect> detection = EFFECTS.register("detection", () -> new EmptyStatusEffect(MobEffectCategory.BENEFICIAL, 0x5297D6, false).setRegistered());

	//Instant
	public static final RegistrySupplier<MobEffect> returning = EFFECTS.register("returning", () -> new ReturnStatusEffect(MobEffectCategory.BENEFICIAL, 0xD05585, true).setRegistered());
	public static final RegistrySupplier<MobEffect> concentration = EFFECTS.register("concentration", () -> new ConcentrationStatusEffect(MobEffectCategory.BENEFICIAL, 0xC8C5E5, true).setRegistered());
	
	//Dummy effects
	public static RegistrySupplier<MobEffect> magnetism_disabled = EFFECTS.register("magnetism_disabled", () -> new ModStatusEffect(MobEffectCategory.BENEFICIAL, MAGNETISM_COLOR, false));

	public static void registerAll() {
		EFFECTS.register();
	}
	
}
