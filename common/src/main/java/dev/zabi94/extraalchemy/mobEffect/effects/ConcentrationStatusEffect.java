package dev.zabi94.extraalchemy.mobEffect.effects;

import java.util.List;
import java.util.stream.Collectors;

import dev.zabi94.extraalchemy.entitydata.EntityProperties;
import dev.zabi94.extraalchemy.mobEffect.ModStatusEffect;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class ConcentrationStatusEffect extends ModStatusEffect {

	public ConcentrationStatusEffect(MobEffectCategory type, int color, boolean isInstant) {
		super(type, color, isInstant);
	}
	
	@Override
	public void applyInstantaneousEffect(ServerLevel world, Entity effectEntity, Entity attacker, LivingEntity target, int amplifier, double proximity) {
		List<MobEffectInstance> replaceable = target.getActiveEffects().stream()
				.filter(s -> s.isVisible())
				.collect(Collectors.toList());
		
		for (MobEffectInstance i:replaceable) {
			target.removeEffectNoUpdate(i.getEffect());
			MobEffectInstance nopart = new MobEffectInstance(i.getEffect(), i.getDuration(), i.getAmplifier(), i.isAmbient(), false, i.isVisible()); 
			target.getActiveEffectsMap().put(i.getEffect(), nopart); //Not triggering onEffectApplied-like method calls
			((EntityProperties) target).markEffectsDirty();
			if (target instanceof ServerPlayer srvp) {
				srvp.connection.send(new ClientboundUpdateMobEffectPacket(target.getId(), nopart, true));
			}
		}
	}

}
