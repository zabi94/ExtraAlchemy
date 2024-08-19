package zabi.minecraft.extraalchemy.potion;

import java.lang.reflect.Field;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import zabi.minecraft.extraalchemy.compat.pehkui.PehkuiCompatBridge;
import zabi.minecraft.extraalchemy.statuseffect.ModEffectRegistry;
import zabi.minecraft.extraalchemy.utils.LibMod;
import zabi.minecraft.extraalchemy.utils.Log;

public class ModPotionRegistry {

	public static ModPotion crumbling = ModPotion.ModPotionTimed.generateAll("crumbling", Registries.STATUS_EFFECT.getEntry(ModEffectRegistry.crumbling), 20*60, 20*100, 20*40);
	public static ModPotion fuse = ModPotion.ModPotionTimed.generateAll("fuse", Registries.STATUS_EFFECT.getEntry(ModEffectRegistry.fuse), 20*20, 20*10, 20*30);
	public static ModPotion magnetism = ModPotion.ModPotionTimed.generateAll("magnetism", Registries.STATUS_EFFECT.getEntry(ModEffectRegistry.magnetism), 20*180, 20*240, 20*120);
	public static ModPotion photosynthesis = ModPotion.ModPotionTimed.generateAll("photosynthesis", Registries.STATUS_EFFECT.getEntry(ModEffectRegistry.photosynthesis), 20*80, 20*100, 20*60);
	public static ModPotion recall = ModPotion.ModPotionTimed.generateAll("recall", Registries.STATUS_EFFECT.getEntry(ModEffectRegistry.recall), 20*60, 20*80, 20*40);
	public static ModPotion sails = ModPotion.ModPotionTimed.generateAll("sails", Registries.STATUS_EFFECT.getEntry(ModEffectRegistry.sails), 20*60*2, 20*60*4, 20*60);
	public static ModPotion learning = ModPotion.ModPotionTimed.generateAll("learning", Registries.STATUS_EFFECT.getEntry(ModEffectRegistry.learning), 20*60*4, 20*60*6, 20*60*2);
	public static ModPotion gravity = ModPotion.ModPotionTimed.generateAll("gravity", Registries.STATUS_EFFECT.getEntry(ModEffectRegistry.gravity), 20*60, 20*60*2, 20*30);
	public static ModPotion combustion = ModPotion.ModPotionTimed.generateAll("combustion", Registries.STATUS_EFFECT.getEntry(ModEffectRegistry.combustion), 20*30, 20*50, 20*15);
	public static ModPotion pacifism = ModPotion.ModPotionTimed.generateAll("pacifism", Registries.STATUS_EFFECT.getEntry(ModEffectRegistry.pacifism), 20*25, 20*40, 20*15);
	public static ModPotion piper = ModPotion.ModPotionTimed.generateWithLengthened("piper", Registries.STATUS_EFFECT.getEntry(ModEffectRegistry.piper), 20*120, 20*240);
	public static ModPotion detection = ModPotion.ModPotionTimed.generateAll("detection", Registries.STATUS_EFFECT.getEntry(ModEffectRegistry.detection), 20*60, 20*120, 20*30);
	
	public static ModPotion returning = new ModPotion.ModPotionInstant("returning", Registries.STATUS_EFFECT.getEntry(ModEffectRegistry.returning), 0);
	public static ModPotion concentration = new ModPotion.ModPotionInstant("concentration", Registries.STATUS_EFFECT.getEntry(ModEffectRegistry.concentration), 0);

	public static void registerAll() {
		try {
			int registered = 0;
			for (Field field:ModPotionRegistry.class.getDeclaredFields()) {
				if (ModPotion.class.isAssignableFrom(field.getType())) {
					Identifier id = new Identifier(LibMod.MOD_ID, field.getName());
					Log.d("Registering potion "+id);
					registered += ((ModPotion) field.get(null)).registerTree(LibMod.MOD_ID, field.getName());
				}
			}
			
			if (FabricLoader.getInstance().isModLoaded("pehkui")) {
				registered += PehkuiCompatBridge.registerPotions();
			}
			
			Log.i("Registered %d potions", registered);
		} catch (Exception e) {
			Log.printAndPropagate(e);
		}
	}

}
