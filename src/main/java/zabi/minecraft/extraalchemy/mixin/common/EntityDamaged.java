package zabi.minecraft.extraalchemy.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTracker;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import zabi.minecraft.extraalchemy.statuseffect.ModEffectRegistry;

@Mixin(DamageTracker.class)
public class EntityDamaged {
	
	@Shadow LivingEntity entity;
	
	@Inject(method = "onDamage", at = @At(value = "TAIL"))
	public void onDamageDealt(DamageSource damageSource, float originalHealth, float f, CallbackInfo ci) {
		StatusEffectInstance sei = entity.getStatusEffect(ModEffectRegistry.pacifism);
		if (sei != null && damageSource.getAttacker() instanceof LivingEntity && !damageSource.isSourceCreativePlayer()) {
			StatusEffectInstance slowness = new StatusEffectInstance(StatusEffects.SLOWNESS, 200, sei.getAmplifier(), false, true, true);
			StatusEffectInstance weakness = new StatusEffectInstance(StatusEffects.WEAKNESS, 200, sei.getAmplifier(), false, true, true);
			((LivingEntity) damageSource.getAttacker()).addStatusEffect(slowness);
			((LivingEntity) damageSource.getAttacker()).addStatusEffect(weakness);
		}
	}

}
