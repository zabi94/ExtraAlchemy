package zabi.minecraft.extraalchemy.statuseffect.effects;

import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.network.packet.s2c.play.EntityStatusEffectS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import zabi.minecraft.extraalchemy.entitydata.EntityProperties;
import zabi.minecraft.extraalchemy.statuseffect.ModStatusEffect;

public class ConcentrationStatusEffect extends ModStatusEffect {

	public ConcentrationStatusEffect(StatusEffectType type, int color, boolean isInstant) {
		super(type, color, isInstant);
	}
	
	@Override
	public void applyInstantEffect(Entity source, Entity attacker, LivingEntity target, int amplifier, double d) {
		List<StatusEffectInstance> replaceable = target.getStatusEffects().stream()
				.filter(s -> s.shouldShowParticles())
				.collect(Collectors.toList());
		
		for (StatusEffectInstance i:replaceable) {
			target.removeStatusEffectInternal(i.getEffectType());
			StatusEffectInstance nopart = new StatusEffectInstance(i.getEffectType(), i.getDuration(), i.getAmplifier(), i.isAmbient(), false, i.shouldShowIcon()); 
			target.getActiveStatusEffects().put(i.getEffectType(), nopart); //Not triggering onEffectApplied-like method calls
			((EntityProperties) target).markEffectsDirty();
			if (target instanceof ServerPlayerEntity) {
				((ServerPlayerEntity) target).networkHandler.sendPacket(new EntityStatusEffectS2CPacket(target.getEntityId(), nopart));
			}
		}
	}

}
