package zabi.minecraft.extraalchemy.optional_compat;

import net.minecraft.entity.effect.StatusEffectType;
import zabi.minecraft.extraalchemy.potion.ModPotion;
import zabi.minecraft.extraalchemy.potion.ModPotionRegistry;
import zabi.minecraft.extraalchemy.statuseffect.ModEffectRegistry;
import zabi.minecraft.extraalchemy.statuseffect.effects.size_entity_attributes.GrowingStatusEffect;
import zabi.minecraft.extraalchemy.statuseffect.effects.size_entity_attributes.ResizeStatusEffect;
import zabi.minecraft.extraalchemy.statuseffect.effects.size_entity_attributes.ShrinkingStatusEffect;

public class SizeEntityAttributesCompat {
	
	public static void setup() {
		ModEffectRegistry.shrinking = new ShrinkingStatusEffect(StatusEffectType.NEUTRAL, 0x00ffc8, false);
		ModEffectRegistry.growing = new GrowingStatusEffect(StatusEffectType.NEUTRAL, 0xFF9600, false);
		ModEffectRegistry.resize = new ResizeStatusEffect(StatusEffectType.NEUTRAL, 0x6d4ecc, true);
		ModPotionRegistry.shrinking = ModPotion.ModPotionTimed.generateAll("shrinking", ModEffectRegistry.shrinking, 4800, 9600, 3600);
		ModPotionRegistry.growing = ModPotion.ModPotionTimed.generateAll("growing", ModEffectRegistry.growing, 4800, 9600, 3600);
		ModPotionRegistry.resize = ModPotion.ModPotionInstant.generateBase("resize", ModEffectRegistry.resize);
	}
	
}
