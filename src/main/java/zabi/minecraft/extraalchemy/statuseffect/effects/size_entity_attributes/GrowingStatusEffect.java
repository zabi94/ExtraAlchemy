package zabi.minecraft.extraalchemy.statuseffect.effects.size_entity_attributes;

import moriyashiine.sizeentityattributes.SizeEntityAttributes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.player.PlayerEntity;
import zabi.minecraft.extraalchemy.statuseffect.ModEffectRegistry;
import zabi.minecraft.extraalchemy.statuseffect.ModStatusEffect;

public class GrowingStatusEffect extends ModStatusEffect {
	
	public static String uuid = "2a69b27f-e024-4b4f-8110-7e35c740e8d6";
	public static String uuid_own = "3df9b27f-a0d4-4b4f-8110-7e3fc740e8d6";

	public GrowingStatusEffect(StatusEffectType type, int color, boolean isInstant) {
		super(type, color, isInstant);
		this.addAttributeModifier(SizeEntityAttributes.HEIGHT_MULTIPLIER, uuid_own, 0.5D, Operation.MULTIPLY_TOTAL);
		this.addAttributeModifier(SizeEntityAttributes.WIDTH_MULTIPLIER, uuid_own, 0.5D, Operation.MULTIPLY_TOTAL);
		this.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED, uuid, -0.2D, Operation.MULTIPLY_TOTAL);
		this.addAttributeModifier(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, uuid, 0.5D, Operation.MULTIPLY_TOTAL);
		this.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, uuid, 0.5D, Operation.MULTIPLY_TOTAL);
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
		entity.removeStatusEffect(ModEffectRegistry.shrinking);
		super.onApplied(entity, attributes, amplifier);
	}
	
	@Override
	public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		super.onRemoved(entity, attributes, amplifier);
		if (entity instanceof PlayerEntity) {
			entity.stepHeight = 0.6f;
		}
	}
	
}
