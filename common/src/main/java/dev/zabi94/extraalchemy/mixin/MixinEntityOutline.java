package dev.zabi94.extraalchemy.mixin;

import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.blaze3d.platform.WindowEventHandler;

import dev.zabi94.extraalchemy.mobEffect.ModEffectRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.thread.ReentrantBlockableEventLoop;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

@Mixin(Minecraft.class)
public abstract class MixinEntityOutline extends ReentrantBlockableEventLoop<Runnable> implements WindowEventHandler {
	
	@Shadow @Nullable public LocalPlayer player;
	
	public MixinEntityOutline() {
		super(null, false);
	}

	@Inject(at = @At("HEAD"), method = "shouldEntityAppearGlowing", cancellable = true)
	public void outlineWhenDetectionAppliedToPlayer(final Entity entity, CallbackInfoReturnable<Boolean> cir) {
		if ((entity instanceof LivingEntity) && player != null) {
			MobEffectInstance detection = player.getEffect(ModEffectRegistry.detection.asHolder());
			if (detection != null && entity.position().closerThan(player.position(), 16*(detection.getAmplifier() + 1))) {
				cir.setReturnValue(true);
			}
		}
	}

}
