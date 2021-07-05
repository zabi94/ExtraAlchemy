package zabi.minecraft.extraalchemy.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.player.PlayerEntity;
import zabi.minecraft.extraalchemy.statuseffect.ModEffectRegistry;

@Mixin(TemptGoal.class)
public abstract class TemptGoalMixin {
	
	@Shadow protected PlayerEntity closestPlayer;
	
	@Inject(method = "isTemptedBy", at = @At(value = "HEAD"), cancellable = true)
	public void addPiperCheck(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
		if (entity != null && entity.getStatusEffect(ModEffectRegistry.piper) != null) {
			cir.setReturnValue(true);
		}
	}
	
}
