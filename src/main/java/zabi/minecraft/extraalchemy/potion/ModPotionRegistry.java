package zabi.minecraft.extraalchemy.potion;

import java.lang.reflect.Field;

import net.minecraft.util.Identifier;
import zabi.minecraft.extraalchemy.config.ConfigInstance;
import zabi.minecraft.extraalchemy.config.ModConfig;
import zabi.minecraft.extraalchemy.statuseffect.ModEffectRegistry;
import zabi.minecraft.extraalchemy.utils.LibMod;
import zabi.minecraft.extraalchemy.utils.Log;

public class ModPotionRegistry {

	public static ModPotion crumbling = ModPotion.ModPotionTimed.generateAll("crumbling", ModEffectRegistry.crumbling, 20*60, 20*100, 20*40);
	public static ModPotion fuse = ModPotion.ModPotionTimed.generateAll("fuse", ModEffectRegistry.fuse, 20*20, 20*10, 20*30);
	public static ModPotion magnetism = ModPotion.ModPotionTimed.generateAll("magnetism", ModEffectRegistry.magnetism, 20*180, 20*240, 20*120);
	public static ModPotion photosynthesis = ModPotion.ModPotionTimed.generateAll("photosynthesis", ModEffectRegistry.photosynthesis, 20*80, 20*100, 20*60);
	public static ModPotion recall = ModPotion.ModPotionTimed.generateAll("recall", ModEffectRegistry.recall, 20*60, 20*80, 20*40);
	public static ModPotion sails = ModPotion.ModPotionTimed.generateAll("sails", ModEffectRegistry.sails, 20*60*2, 20*60*4, 20*60);
	public static ModPotion learning = ModPotion.ModPotionTimed.generateAll("learning", ModEffectRegistry.learning, 20*60*4, 20*60*6, 20*60*2);
	public static ModPotion gravity = ModPotion.ModPotionTimed.generateAll("gravity", ModEffectRegistry.gravity, 20*60, 20*60*2, 20*30);
	
	public static ModPotion returning = new ModPotion.ModPotionInstant("returning", ModEffectRegistry.returning, 0);
	public static ModPotion concentration = new ModPotion.ModPotionInstant("concentration", ModEffectRegistry.concentration, 0);
	
	public static void registerAll() {
		try {
			int registered = 0;
			int disabled = 0;
			for (Field field:ModPotionRegistry.class.getDeclaredFields()) {
				if (ModPotion.class.isAssignableFrom(field.getType())) {
					boolean active = (boolean) ConfigInstance.Potions.class.getField(field.getName()).get(ModConfig.INSTANCE.potions);
					Identifier id = new Identifier(LibMod.MOD_ID, field.getName());
					if (active) {
						Log.d("Registering potion "+id);
						registered += ((ModPotion) field.get(null)).registerTree(LibMod.MOD_ID, field.getName());
					} else {
						Log.d("Potion disabled: "+id);
						disabled++;
					}
				}
			}
			Log.i("Registered %d potions, %d base potion were disabled (child potions excluded)", registered, disabled);
		} catch (Exception e) {
			Log.printAndPropagate(e);
		}
	}

}
