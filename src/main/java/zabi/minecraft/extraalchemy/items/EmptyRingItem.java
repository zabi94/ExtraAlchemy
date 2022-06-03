package zabi.minecraft.extraalchemy.items;

import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import zabi.minecraft.extraalchemy.config.ModConfig;

public class EmptyRingItem extends Item {

	public EmptyRingItem() {
		super(new Settings().maxCount(1).group(ItemSettings.EXTRA_ALCHEMY_GROUP));
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		if (ModConfig.INSTANCE.enableVials) {
			tooltip.add(Text.translatable("item.extraalchemy.empty_ring.tooltip1"));
			tooltip.add(Text.translatable("item.extraalchemy.empty_ring.tooltip2"));
		} else {
			tooltip.add(Text.translatable("item.extraalchemy.empty_ring.tootlip.disabled"));
		}
	}

}
