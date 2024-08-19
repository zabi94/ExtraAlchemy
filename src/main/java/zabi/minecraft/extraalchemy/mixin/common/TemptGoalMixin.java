package zabi.minecraft.extraalchemy.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import zabi.minecraft.extraalchemy.statuseffect.ModEffectRegistry;

@Mixin(TemptGoal.class)
public abstract class TemptGoalMixin {
	
	@Inject(method = "isTemptedBy", at = @At(value = "HEAD"), cancellable = true)
	public void addPiperCheck(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
		RegistryEntry<StatusEffect> reg = Registries.STATUS_EFFECT.getEntry(ModEffectRegistry.piper);
		if (entity != null && entity.getStatusEffect(reg) != null) {
			cir.setReturnValue(true);
		}
	}
	
}
