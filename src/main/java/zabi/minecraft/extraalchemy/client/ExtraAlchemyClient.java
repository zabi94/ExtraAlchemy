package zabi.minecraft.extraalchemy.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.minecraft.item.ItemStack;
import zabi.minecraft.extraalchemy.client.input.KeybindDispatcher;
import zabi.minecraft.extraalchemy.client.network.ClientPacketRegistry;
import zabi.minecraft.extraalchemy.client.rendering.ColorsRegistration;
import zabi.minecraft.extraalchemy.client.screen.ModScreens;
import zabi.minecraft.extraalchemy.client.tooltip.PotionTooltipComponent;
import zabi.minecraft.extraalchemy.client.tooltip.PotionTooltipData;
import zabi.minecraft.extraalchemy.client.tooltip.StatusEffectContainer;
import zabi.minecraft.extraalchemy.utils.proxy.ClientProxy;

public class ExtraAlchemyClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		
		new ClientProxy().registerProxy();
		
		ClientPacketRegistry.init();
		KeybindDispatcher.initKeybinds();
		KeybindDispatcher.registerListeners();
		ColorsRegistration.init();
		ModScreens.init();
		
		TooltipComponentCallback.EVENT.register(data -> {
			if (data instanceof PotionTooltipData ptd) {
				ItemStack stack = ptd.getStack();
				StatusEffectContainer sec = StatusEffectContainer.of(stack);
				if (sec.hasEffects(stack)) return new PotionTooltipComponent(sec, stack);
			}
			return null;
		});
	}

}
