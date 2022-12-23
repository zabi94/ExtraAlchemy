package zabi.minecraft.extraalchemy.compat.pehkui;

import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import zabi.minecraft.extraalchemy.config.ModConfig;
import zabi.minecraft.extraalchemy.potion.ModPotion;
import zabi.minecraft.extraalchemy.recipes.BrewingRecipeRegistrar;
import zabi.minecraft.extraalchemy.statuseffect.ModStatusEffect;
import zabi.minecraft.extraalchemy.utils.LibMod;
import zabi.minecraft.extraalchemy.utils.Log;

public class PehkuiPotions {
	
	public static ModStatusEffect growing = new GrowthStatusEffect(StatusEffectCategory.NEUTRAL, 0xFF9600, false);
	public static ModStatusEffect shrinking = new ShrinkStatusEffect(StatusEffectCategory.NEUTRAL, 0x00FFC8, false);

	public static ModPotion shrinking_ = ModPotion.ModPotionTimed.generateAll("shrinking", shrinking, 20*120, 20*240, 20*60);
	public static ModPotion growing_ = ModPotion.ModPotionTimed.generateAll("growing", growing, 20*120, 20*240, 20*60);
	
	public static int registerEffects() {
		Log.i("Registering pehkui effects");
		Identifier id_grow = new Identifier(LibMod.MOD_ID, "growing");
		Registry.register(Registries.STATUS_EFFECT, id_grow, growing.onRegister());
		Identifier id_shrink = new Identifier(LibMod.MOD_ID, "shrinking");
		Registry.register(Registries.STATUS_EFFECT, id_shrink, shrinking.onRegister());
		return 2;
	}
	
	public static int registerPotions() {
		Log.i("Registering pehkui potions");
		int reg = 0;
		if (ModConfig.INSTANCE.potions.shrinking) {
			shrinking_.registerTree(LibMod.MOD_ID, "shrinking");
			reg++;
		}
		if (ModConfig.INSTANCE.potions.growing) {
			growing_.registerTree(LibMod.MOD_ID, "growing");
			reg++;
		}
		return reg;
	}
	
	public static void registerRecipes() {
		Log.i("Registering pehkui recipes");
		BrewingRecipeRegistrar.registerPotion(ModConfig.INSTANCE.potions.shrinking, shrinking_, Items.BROWN_MUSHROOM, Potions.AWKWARD);
		BrewingRecipeRegistrar.registerPotion(ModConfig.INSTANCE.potions.growing, growing_, Items.RED_MUSHROOM, Potions.AWKWARD);
	}
	
}
