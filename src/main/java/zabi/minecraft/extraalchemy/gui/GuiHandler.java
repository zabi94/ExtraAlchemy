package zabi.minecraft.extraalchemy.gui;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import zabi.minecraft.extraalchemy.gui.container.ContainerPotionBag;
import zabi.minecraft.extraalchemy.gui.container.window.GuiPotionBag;
import zabi.minecraft.extraalchemy.inventory.PotionBagInventory;
import zabi.minecraft.extraalchemy.items.ModItems;
import zabi.minecraft.extraalchemy.lib.Log;


public class GuiHandler implements IGuiHandler {

	public enum IDs {
		GUI_POTION_BAG
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (IDs.values()[ID]) {
		case GUI_POTION_BAG:
			if (player.getHeldItemMainhand().getItem().equals(ModItems.potion_bag)) return new ContainerPotionBag(new PotionBagInventory(player.getHeldItemMainhand()), player.inventory);
			if (player.getHeldItemOffhand().getItem().equals(ModItems.potion_bag)) return new ContainerPotionBag(new PotionBagInventory(player.getHeldItemOffhand()), player.inventory);
			return null;
		default:
			Log.w("invalid GUI requested: " + ID);
			return null;

		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (IDs.values()[ID]) {
		case GUI_POTION_BAG:
			return new GuiPotionBag((Container) getServerGuiElement(ID, player, world, x, y, z));
		default:
			Log.w("invalid GUI requested");
			return null;

		}
	}

}
