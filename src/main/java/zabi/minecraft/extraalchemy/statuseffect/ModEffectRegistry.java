package zabi.minecraft.extraalchemy.statuseffect;

import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import zabi.minecraft.extraalchemy.utils.LibMod;

public class ModEffectRegistry {

	public static void init() {
		Registry.register(Registry.STATUS_EFFECT, new Identifier(LibMod.MOD_ID, "test"), new ModStatusEffect(StatusEffectType.field_18271, 0, true));
	}
	
}
