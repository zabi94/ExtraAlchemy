package zabi.minecraft.extraalchemy.items;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import zabi.minecraft.extraalchemy.utils.LibMod;

public class ItemSettings {
	
	public static final ItemGroup EXTRA_ALCHEMY_GROUP = FabricItemGroupBuilder.create(new Identifier(LibMod.MOD_ID, "all")).icon(() -> new ItemStack(ModItems.POTION_BAG)).build();
	
}
