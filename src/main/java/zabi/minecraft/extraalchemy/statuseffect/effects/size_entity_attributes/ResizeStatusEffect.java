package zabi.minecraft.extraalchemy.statuseffect.effects.size_entity_attributes;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectType;
import zabi.minecraft.extraalchemy.statuseffect.ModEffectRegistry;
import zabi.minecraft.extraalchemy.statuseffect.ModStatusEffect;

public class ResizeStatusEffect extends ModStatusEffect {
	
	public ResizeStatusEffect(StatusEffectType type, int color, boolean isInstant) {
		super(type, color, isInstant);
	}

	@Override
	public void applyInstantEffect(Entity source, Entity attacker, LivingEntity target, int amplifier, double proximity) {
		target.removeStatusEffect(ModEffectRegistry.growing);
		target.removeStatusEffect(ModEffectRegistry.shrinking);
	}
	
}
