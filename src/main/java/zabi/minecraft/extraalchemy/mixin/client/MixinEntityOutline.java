package zabi.minecraft.extraalchemy.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import zabi.minecraft.extraalchemy.statuseffect.ModEffectRegistry;

@Mixin(MinecraftClient.class)
public abstract class MixinEntityOutline extends ReentrantThreadExecutor<Runnable> {
	
	@Shadow public ClientPlayerEntity player;
	
	public MixinEntityOutline(String string) {
		super(string);
	}

	@Inject(at = @At("HEAD"), method = "hasOutline", cancellable = true)
	public void outlineWhenDetectionAppliedToPlayer(Entity entity, CallbackInfoReturnable<Boolean> cir) {
		if ((entity instanceof LivingEntity) && player != null) {
			RegistryEntry<StatusEffect> reg = Registries.STATUS_EFFECT.getEntry(ModEffectRegistry.detection);
			StatusEffectInstance detection = player.getStatusEffect(reg);
			if (detection != null && entity.getPos().isInRange(player.getPos(), 16*(detection.getAmplifier() + 1))) {
				cir.setReturnValue(true);
			}
		}
	}

}
