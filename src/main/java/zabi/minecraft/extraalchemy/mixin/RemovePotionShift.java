package zabi.minecraft.extraalchemy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.ContainerScreen;
import net.minecraft.container.Container;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import zabi.minecraft.extraalchemy.config.ModConfig;

@Mixin(AbstractInventoryScreen.class)
public abstract class RemovePotionShift<T extends Container> extends ContainerScreen<T> {

	@Shadow protected boolean offsetGuiForEffects;
	
	public RemovePotionShift(T container_1, PlayerInventory playerInventory_1, Text textComponent_1) {
		super(container_1, playerInventory_1, textComponent_1);
	}
	
	@Inject(method = "applyStatusEffectOffset", at = @At("HEAD"), cancellable = true)
	public void stopShift(CallbackInfo ci) {
		if (ModConfig.INSTANCE.removeInventoryPotionShift) {
			ci.cancel();
			this.x = (this.width - this.containerWidth) / 2;
			this.offsetGuiForEffects = false;
		}
	}
	
	@Inject(method = "render", at = @At("RETURN"))
	public void renderOverride(int int_1, int int_2, float float_1, CallbackInfo ci) {
	      if (!this.offsetGuiForEffects) {
	         this.drawStatusEffects();
	      }
	   }

	@Shadow protected abstract void drawStatusEffects();

}
