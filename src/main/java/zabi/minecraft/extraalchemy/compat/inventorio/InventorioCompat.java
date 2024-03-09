package zabi.minecraft.extraalchemy.compat.inventorio;

import org.jetbrains.annotations.NotNull;

import me.lizardofoz.inventorio.api.InventorioAPI;
import me.lizardofoz.inventorio.api.InventorioAddonSection;
import me.lizardofoz.inventorio.api.InventorioTickHandler;
import me.lizardofoz.inventorio.player.PlayerInventoryAddon;
import net.minecraft.item.ItemStack;
import zabi.minecraft.extraalchemy.items.ModItems;
import zabi.minecraft.extraalchemy.utils.LibMod;

public class InventorioCompat {

	public static void init() {
		
		InventorioAPI.registerInventoryTickHandler(LibMod.id("potion_ring_ticker"), new InventorioTickHandler() {
			@Override
			public void tick(@NotNull PlayerInventoryAddon invAddon, @NotNull InventorioAddonSection section, @NotNull ItemStack stack, int index) {
				if (!invAddon.getPlayer().getEntityWorld().isClient && stack.getItem().equals(ModItems.POTION_RING)) {
					ModItems.POTION_RING.inventoryTick(stack, invAddon.getPlayer().getEntityWorld(), invAddon.getPlayer(), -1, false);
				}
			}
		});
		
	}

}
