package dev.zabi94.extraalchemy.potion;

import java.lang.reflect.Field;

import dev.zabi94.extraalchemy.mobEffect.ModEffectRegistry;
import dev.zabi94.extraalchemy.utils.LibMod;
import dev.zabi94.extraalchemy.utils.Log;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class ModPotionRegistry {

	public static ModPotion crumbling = ModPotion.ModPotionTimed.generateAll("crumbling", BuiltInRegistries.MOB_EFFECT.wrapAsHolder(ModEffectRegistry.crumbling), 20*60, 20*100, 20*40);
	public static ModPotion fuse = ModPotion.ModPotionTimed.generateAll("fuse", BuiltInRegistries.MOB_EFFECT.wrapAsHolder(ModEffectRegistry.fuse), 20*20, 20*10, 20*30);
	public static ModPotion magnetism = ModPotion.ModPotionTimed.generateAll("magnetism", BuiltInRegistries.MOB_EFFECT.wrapAsHolder(ModEffectRegistry.magnetism), 20*180, 20*240, 20*120);
	public static ModPotion photosynthesis = ModPotion.ModPotionTimed.generateAll("photosynthesis", BuiltInRegistries.MOB_EFFECT.wrapAsHolder(ModEffectRegistry.photosynthesis), 20*80, 20*100, 20*60);
	public static ModPotion recall = ModPotion.ModPotionTimed.generateAll("recall", BuiltInRegistries.MOB_EFFECT.wrapAsHolder(ModEffectRegistry.recall), 20*60, 20*80, 20*40);
	public static ModPotion sails = ModPotion.ModPotionTimed.generateAll("sails", BuiltInRegistries.MOB_EFFECT.wrapAsHolder(ModEffectRegistry.sails), 20*60*2, 20*60*4, 20*60);
	public static ModPotion learning = ModPotion.ModPotionTimed.generateAll("learning", BuiltInRegistries.MOB_EFFECT.wrapAsHolder(ModEffectRegistry.learning), 20*60*4, 20*60*6, 20*60*2);
	public static ModPotion gravity = ModPotion.ModPotionTimed.generateAll("gravity", BuiltInRegistries.MOB_EFFECT.wrapAsHolder(ModEffectRegistry.gravity), 20*60, 20*60*2, 20*30);
	public static ModPotion combustion = ModPotion.ModPotionTimed.generateAll("combustion", BuiltInRegistries.MOB_EFFECT.wrapAsHolder(ModEffectRegistry.combustion), 20*30, 20*50, 20*15);
	public static ModPotion pacifism = ModPotion.ModPotionTimed.generateAll("pacifism", BuiltInRegistries.MOB_EFFECT.wrapAsHolder(ModEffectRegistry.pacifism), 20*25, 20*40, 20*15);
	public static ModPotion piper = ModPotion.ModPotionTimed.generateWithLengthened("piper", BuiltInRegistries.MOB_EFFECT.wrapAsHolder(ModEffectRegistry.piper), 20*120, 20*240);
	public static ModPotion detection = ModPotion.ModPotionTimed.generateAll("detection", BuiltInRegistries.MOB_EFFECT.wrapAsHolder(ModEffectRegistry.detection), 20*60, 20*120, 20*30);
	
	public static ModPotion returning = new ModPotion.ModPotionInstant("returning", BuiltInRegistries.MOB_EFFECT.wrapAsHolder(ModEffectRegistry.returning), 0);
	public static ModPotion concentration = new ModPotion.ModPotionInstant("concentration", BuiltInRegistries.MOB_EFFECT.wrapAsHolder(ModEffectRegistry.concentration), 0);

	public static void registerAll() {
		try {
			int registered = 0;
			for (Field field:ModPotionRegistry.class.getDeclaredFields()) {
				if (ModPotion.class.isAssignableFrom(field.getType())) {
					Identifier id = LibMod.id(field.getName());
					Log.d("Registering potion "+id);
					registered += ((ModPotion) field.get(null)).registerTree(LibMod.MOD_ID, field.getName());
				}
			}
			
			Log.i("Registered %d potions", registered);
		} catch (Exception e) {
			Log.printAndPropagate(e);
		}
	}

}
