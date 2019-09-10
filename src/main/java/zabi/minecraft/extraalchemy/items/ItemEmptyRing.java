package zabi.minecraft.extraalchemy.items;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import zabi.minecraft.extraalchemy.ExtraAlchemy;
import zabi.minecraft.extraalchemy.lib.Reference;

public class ItemEmptyRing extends Item {

	public ItemEmptyRing() {
		this.setMaxStackSize(16);
		this.setCreativeTab(ExtraAlchemy.TAB);
		this.setRegistryName(new ResourceLocation(Reference.MID, "empty_ring"));
		this.setTranslationKey("ea.empty_ring");
	}
	
}
