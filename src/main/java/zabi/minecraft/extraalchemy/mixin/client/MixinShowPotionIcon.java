package zabi.minecraft.extraalchemy.mixin.client;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.item.TooltipData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import zabi.minecraft.extraalchemy.client.tooltip.PotionTooltipData;
import zabi.minecraft.extraalchemy.client.tooltip.StatusEffectContainer;

@Mixin(Item.class)
public class MixinShowPotionIcon {
	
	@Inject(method = "getTooltipData", at = @At("RETURN"), cancellable = true)
	public void potionutils_getTooltipData(ItemStack stack, CallbackInfoReturnable<Optional<TooltipData>> cir) {
		Optional<TooltipData> returning = cir.getReturnValue();
		if (returning.isEmpty() && StatusEffectContainer.of(stack).hasEffects(stack)) {
			cir.setReturnValue(Optional.of(new PotionTooltipData(stack)));
		}
    }

}
