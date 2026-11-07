package dev.zabi94.extraalchemy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.zabi94.extraalchemy.mobEffect.ModEffectRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.TemptGoal;

@Mixin(TemptGoal.class)
public abstract class TemptGoalMixin {
	
	@Inject(method = "shouldFollow", at = @At(value = "HEAD"), cancellable = true)
	public void addPiperCheck(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
		if (entity != null && entity.getEffect(ModEffectRegistry.piper.asHolder()) != null) {
			cir.setReturnValue(true);
		}
	}
	
}
