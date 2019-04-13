package zabi.minecraft.extraalchemy.statuseffect;

import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import zabi.minecraft.extraalchemy.statuseffect.effects.MagnetismStatusEffect;
import zabi.minecraft.extraalchemy.statuseffect.effects.PhotosynthesisStatusEffect;
import zabi.minecraft.extraalchemy.utils.LibMod;

public class ModEffectRegistry {

	public static ModStatusEffect magnetism = new MagnetismStatusEffect(StatusEffectType.field_18271, 0xb8b8b8, false);
	public static ModStatusEffect photosynthesis = new PhotosynthesisStatusEffect(StatusEffectType.field_18271, 0x3cbd19, false);
	
	public static void init() {
		Registry.register(Registry.STATUS_EFFECT, new Identifier(LibMod.MOD_ID, "magnetism"), magnetism);
		Registry.register(Registry.STATUS_EFFECT, new Identifier(LibMod.MOD_ID, "photosynthesis"), photosynthesis);
	}
	
}
