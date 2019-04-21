package zabi.minecraft.extraalchemy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.ContainerScreen;
import net.minecraft.client.gui.ingame.AbstractPlayerInventoryScreen;
import net.minecraft.container.Container;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.TextComponent;
import zabi.minecraft.extraalchemy.config.ModConfig;
import zabi.minecraft.extraalchemy.utils.Log;

@Mixin(AbstractPlayerInventoryScreen.class)
public abstract class RemovePotionShift<T extends Container> extends ContainerScreen<T> {

	@Shadow protected boolean offsetGuiForEffects;
	
	public RemovePotionShift(T container_1, PlayerInventory playerInventory_1, TextComponent textComponent_1) {
		super(container_1, playerInventory_1, textComponent_1);
	}
	
	@Inject(method = "method_2476", at = @At("HEAD"), cancellable = true)
	public void stopShift(CallbackInfo ci) {
		if (ModConfig.INSTANCE.removeInventoryPotionShift) {
			ci.cancel();
			this.left = (this.width - this.containerWidth) / 2;
			this.offsetGuiForEffects = false;
			Log.i("Stopped shift");
		} else {
			Log.i("passed shift");
		}
	}
	
	@Inject(method = "render", at = @At("RETURN"))
	public void renderOverride(int int_1, int int_2, float float_1, CallbackInfo ci) {
	      if (!this.offsetGuiForEffects) {
	         this.drawPotionEffects();
	      }
	   }

	@Shadow protected abstract void drawPotionEffects();

}
