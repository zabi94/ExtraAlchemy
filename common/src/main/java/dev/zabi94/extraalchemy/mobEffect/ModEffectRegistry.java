package dev.zabi94.extraalchemy.mobEffect;

import java.lang.reflect.Field;

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
import dev.zabi94.extraalchemy.utils.Log;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class ModEffectRegistry {
	
	public static final int MAGNETISM_COLOR = 0xb8b8b8;

	public static ModStatusEffect magnetism = new MagnetismStatusEffect(MobEffectCategory.BENEFICIAL, MAGNETISM_COLOR, false);
	public static ModStatusEffect photosynthesis = new PhotosynthesisStatusEffect(MobEffectCategory.BENEFICIAL, 0x3cbd19, false);
	public static ModStatusEffect crumbling = new CrumblingStatusEffect(MobEffectCategory.NEUTRAL, 0x794044, false);
	public static ModStatusEffect fuse = new FuseStatusEffect(MobEffectCategory.HARMFUL, 0xcc3333, false);
	public static ModStatusEffect recall = new RecallStatusEffect(MobEffectCategory.BENEFICIAL, 0xFFF200, false);
	public static ModStatusEffect sails = new SailsStatusEffect(MobEffectCategory.BENEFICIAL, 0x9BD8FF, false);
	public static ModStatusEffect learning = new LearningStatusEffect(MobEffectCategory.BENEFICIAL, 0xD5FF39, false);
	public static ModStatusEffect gravity = new GravityStatusEffect(MobEffectCategory.NEUTRAL, 0x815D09, false);
	public static ModStatusEffect combustion = new CombustionStatusEffect(MobEffectCategory.HARMFUL, 0xF96501, false);
	public static ModStatusEffect pacifism = new EmptyStatusEffect(MobEffectCategory.BENEFICIAL, 0xFEFFE7, false);
	public static ModStatusEffect piper = new EmptyStatusEffect(MobEffectCategory.BENEFICIAL, 0xFFDF9F, false);
	public static ModStatusEffect detection = new EmptyStatusEffect(MobEffectCategory.BENEFICIAL, 0x5297D6, false);

	//Instant
	public static ModStatusEffect returning = new ReturnStatusEffect(MobEffectCategory.BENEFICIAL, 0xD05585, true);
	public static ModStatusEffect concentration = new ConcentrationStatusEffect(MobEffectCategory.BENEFICIAL, 0xC8C5E5, true);
	
	
	public static void registerAll() {

		try {
			int registered = 0;
			for (Field field:ModEffectRegistry.class.getDeclaredFields()) {
				if (ModStatusEffect.class.isAssignableFrom(field.getType())) {
					Identifier id = LibMod.id(field.getName());
					Registry.register(BuiltInRegistries.MOB_EFFECT, ResourceKey.create(Registries.MOB_EFFECT, id), ((ModStatusEffect) field.get(null)).onRegister());
					Log.d("Registered potion "+id);
					registered++;
				}
			}
			
			Log.i("Registered %d status effects", registered);
			Utils.register();
		} catch (Exception e) {
			Log.printAndPropagate(e);
		}

	}
	
	public static class Utils {
		
		public static MobEffect magnetism_disabled = null;
		
		public static void register() {
			magnetism_disabled = Registry.register(BuiltInRegistries.MOB_EFFECT, ResourceKey.create(Registries.MOB_EFFECT, LibMod.id("magnetism_disabled")), new ModStatusEffect(MobEffectCategory.BENEFICIAL, MAGNETISM_COLOR, magnetism.isInstantaneous()).onRegister());
			Log.i("Registered dummy effects");
		}
	}

}
