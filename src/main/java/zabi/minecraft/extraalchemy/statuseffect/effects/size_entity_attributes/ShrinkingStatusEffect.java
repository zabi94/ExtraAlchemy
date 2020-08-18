package zabi.minecraft.extraalchemy.statuseffect.effects.size_entity_attributes;

import java.util.UUID;

import moriyashiine.sizeentityattributes.SizeEntityAttributes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectType;
import zabi.minecraft.extraalchemy.statuseffect.ModEffectRegistry;
import zabi.minecraft.extraalchemy.statuseffect.ModStatusEffect;

public class ShrinkingStatusEffect extends ModStatusEffect {
	
	public static String uuid = "2a69b27f-e024-4b4f-8110-7e35c740e8d6";
	public static String uuid_own = "3df9b27f-a0d4-4b4f-8110-7e3fc740e8d6";

	public ShrinkingStatusEffect(StatusEffectType type, int color, boolean isInstant) {
		super(type, color, isInstant);
		this.addAttributeModifier(SizeEntityAttributes.HEIGHT_MULTIPLIER, uuid_own, -0.5D, Operation.MULTIPLY_TOTAL);
		this.addAttributeModifier(SizeEntityAttributes.WIDTH_MULTIPLIER, uuid_own, -0.5D, Operation.MULTIPLY_TOTAL);
		this.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED, uuid, 0.5D, Operation.MULTIPLY_TOTAL);
		this.addAttributeModifier(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, uuid, -1D, Operation.MULTIPLY_TOTAL);
		this.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, uuid, -0.25D, Operation.MULTIPLY_TOTAL);
	}

	@Override
	public void applyUpdateEffect(LivingEntity entity, int level) {
		//NO-OP
	}

	@Override
	protected boolean canApplyEffect(int remainingTicks, int level) {
		return false;
	}
	
	@Override
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		entity.removeStatusEffect(ModEffectRegistry.growing);
		super.onApplied(entity, attributes, amplifier);
	}
	
	@Override
	public double adjustModifierAmount(int amplifier, EntityAttributeModifier modifier) {
		if (modifier.getId().equals(UUID.fromString(uuid_own))) {
			return -1d + (1d/(1<<(amplifier + 1))); //Shrinking now has a -1 + 1/(2^lvl) reduction in size (-0% -> -50% -> -75% -> -87.5%...)
		}
		return super.adjustModifierAmount(amplifier, modifier);
	}
	
}
