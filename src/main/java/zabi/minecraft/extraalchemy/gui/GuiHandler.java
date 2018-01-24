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
		//GUI_BOOK, GUI_BONE_AMULET, GUI_EXTENDED_INVENTORY, GUI_CHAINMAIL, GUI_FURNACE, GUI_DECANTER, GUI_FILTER, GUI_ENCHANTER, GUI_DISENCHANTER
		GUI_POTION_BAG
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		
//		BlockPos pos=new BlockPos(x, y, z);
		
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
//		BlockPos pos=new BlockPos(x, y, z);
		switch (IDs.values()[ID]) {
		case GUI_POTION_BAG:
			Container c=(Container) getServerGuiElement(ID, player, world, x, y, z);
			if (c!=null) return new GuiPotionBag(c);
			else return null;
//		case GUI_BOOK:
//			return new GuiPerks();
//		case GUI_BONE_AMULET:
//			return new GuiBoneAmulet((Container) getServerGuiElement(ID, player, world, x, y, z));
//		case GUI_CHAINMAIL:
//			return new GuiChainMail((Container) getServerGuiElement(ID, player, world, x, y, z));
//		case GUI_EXTENDED_INVENTORY:
//			return new GuiExtendedInventory((Container) getServerGuiElement(ID, player, world, x, y, z));
//		case GUI_FURNACE:
//			return new GuiPortableFurnace((Container) getServerGuiElement(ID, player, world, x, y, z));
//		case GUI_DECANTER:
//			return new GuiDecanter((Container) getServerGuiElement(ID, player, world, x, y, z), (TileEntityDecanter) world.getTileEntity(pos));
//		case GUI_FILTER:
//			return new GuiFilter((Container) getServerGuiElement(ID, player, world, x, y, z));
//		case GUI_ENCHANTER:
//			return new GuiEnchanter((Container) getServerGuiElement(ID, player, world, x, y, z), (TileEntityEnchanter) world.getTileEntity(pos));
//		case GUI_DISENCHANTER:
//			return new GuiDisenchanter((Container) getServerGuiElement(ID, player, world, x, y, z), (TileEntityDisenchanter) world.getTileEntity(pos));
		default:
			Log.w("invalid GUI requested");
			return null;

		}
	}

}
