package zabi.minecraft.extraalchemy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.player.PlayerEntity;
import zabi.minecraft.extraalchemy.statuseffect.ModEffectRegistry;

@Mixin(TemptGoal.class)
public abstract class TemptGoalMixin {
	
	@Shadow protected PlayerEntity closestPlayer;
	
	@Inject(method = "isTempedBy", at = @At(value = "HEAD"), cancellable = true)
	public void addPiperCheck(CallbackInfoReturnable<Boolean> cir) {
		if (closestPlayer != null && closestPlayer.getStatusEffect(ModEffectRegistry.piper) != null) {
			cir.setReturnValue(true);
		}
	}
	
}
