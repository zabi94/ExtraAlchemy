package zabi.minecraft.extraalchemy.statuseffect;

import java.lang.reflect.Field;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import zabi.minecraft.extraalchemy.compat.pehkui.PehkuiCompatBridge;
import zabi.minecraft.extraalchemy.statuseffect.effects.CombustionStatusEffect;
import zabi.minecraft.extraalchemy.statuseffect.effects.ConcentrationStatusEffect;
import zabi.minecraft.extraalchemy.statuseffect.effects.CrumblingStatusEffect;
import zabi.minecraft.extraalchemy.statuseffect.effects.EmptyStatusEffect;
import zabi.minecraft.extraalchemy.statuseffect.effects.FuseStatusEffect;
import zabi.minecraft.extraalchemy.statuseffect.effects.GravityStatusEffect;
import zabi.minecraft.extraalchemy.statuseffect.effects.LearningStatusEffect;
import zabi.minecraft.extraalchemy.statuseffect.effects.MagnetismStatusEffect;
import zabi.minecraft.extraalchemy.statuseffect.effects.PhotosynthesisStatusEffect;
import zabi.minecraft.extraalchemy.statuseffect.effects.RecallStatusEffect;
import zabi.minecraft.extraalchemy.statuseffect.effects.ReturnStatusEffect;
import zabi.minecraft.extraalchemy.statuseffect.effects.SailsStatusEffect;
import zabi.minecraft.extraalchemy.utils.LibMod;
import zabi.minecraft.extraalchemy.utils.Log;

public class ModEffectRegistry {
	
	public static final int MAGNETISM_COLOR = 0xb8b8b8;

	public static ModStatusEffect magnetism = new MagnetismStatusEffect(StatusEffectCategory.BENEFICIAL, MAGNETISM_COLOR, false);
	public static ModStatusEffect photosynthesis = new PhotosynthesisStatusEffect(StatusEffectCategory.BENEFICIAL, 0x3cbd19, false);
	public static ModStatusEffect crumbling = new CrumblingStatusEffect(StatusEffectCategory.NEUTRAL, 0x794044, false);
	public static ModStatusEffect fuse = new FuseStatusEffect(StatusEffectCategory.HARMFUL, 0xcc3333, false);
	public static ModStatusEffect recall = new RecallStatusEffect(StatusEffectCategory.BENEFICIAL, 0xFFF200, false);
	public static ModStatusEffect sails = new SailsStatusEffect(StatusEffectCategory.BENEFICIAL, 0x9BD8FF, false);
	public static ModStatusEffect learning = new LearningStatusEffect(StatusEffectCategory.BENEFICIAL, 0xD5FF39, false);
	public static ModStatusEffect gravity = new GravityStatusEffect(StatusEffectCategory.NEUTRAL, 0x815D09, false);
	public static ModStatusEffect combustion = new CombustionStatusEffect(StatusEffectCategory.HARMFUL, 0xF96501, false);
	public static ModStatusEffect pacifism = new EmptyStatusEffect(StatusEffectCategory.BENEFICIAL, 0xFEFFE7, false);
	public static ModStatusEffect piper = new EmptyStatusEffect(StatusEffectCategory.BENEFICIAL, 0xFFDF9F, false);
	public static ModStatusEffect detection = new EmptyStatusEffect(StatusEffectCategory.BENEFICIAL, 0x5297D6, false);

	//Instant
	public static ModStatusEffect returning = new ReturnStatusEffect(StatusEffectCategory.BENEFICIAL, 0xD05585, true);
	public static ModStatusEffect concentration = new ConcentrationStatusEffect(StatusEffectCategory.BENEFICIAL, 0xC8C5E5, true);
	
	
	public static void registerAll() {

		try {
			int registered = 0;
			for (Field field:ModEffectRegistry.class.getDeclaredFields()) {
				if (ModStatusEffect.class.isAssignableFrom(field.getType())) {
					Identifier id = new Identifier(LibMod.MOD_ID, field.getName());
					Registry.register(Registries.STATUS_EFFECT, id, ((ModStatusEffect) field.get(null)).onRegister());
					Log.d("Registered potion "+id);
					registered++;
				}
			}
			
			if (FabricLoader.getInstance().isModLoaded("pehkui")) {
				registered += PehkuiCompatBridge.registerEffects();
			}
			Log.i("Registered %d status effects", registered);
			Utils.register();
		} catch (Exception e) {
			Log.printAndPropagate(e);
		}

	}
	
	public static class Utils {
		
		public static StatusEffect magnetism_disabled = null;
		
		public static void register() {
			magnetism_disabled = Registry.register(Registries.STATUS_EFFECT, new Identifier(LibMod.MOD_ID, "magnetism_disabled"), new ModStatusEffect(StatusEffectCategory.BENEFICIAL, MAGNETISM_COLOR, magnetism.isInstant()).onRegister());
			Log.i("Registered dummy effects");
		}
	}

}
