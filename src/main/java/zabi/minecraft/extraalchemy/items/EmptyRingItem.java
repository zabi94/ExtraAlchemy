package zabi.minecraft.extraalchemy.items;

import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import zabi.minecraft.extraalchemy.config.ModConfig;

public class EmptyRingItem extends Item {

	public EmptyRingItem() {
		super(new net.minecraft.item.Item.Settings().maxCount(1));
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		if (ModConfig.INSTANCE.enableVials) {
			tooltip.add(Text.translatable("item.extraalchemy.empty_ring.tooltip1"));
			tooltip.add(Text.translatable("item.extraalchemy.empty_ring.tooltip2"));
		} else {
			tooltip.add(Text.translatable("item.extraalchemy.empty_ring.tootlip.disabled"));
		}
	}

}
