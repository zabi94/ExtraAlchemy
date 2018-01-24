package zabi.minecraft.extraalchemy.gui.container;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import zabi.minecraft.extraalchemy.inventory.PotionBagInventory;
import zabi.minecraft.extraalchemy.items.ModItems;

public class ContainerPotionBag extends ContainerBase {

	private String bag_name;
	
	public ContainerPotionBag(PotionBagInventory inv, InventoryPlayer pinv) {
		
		int all_x = 8;
		int all_y = 90;
		addSlotToContainer(new GhostPotionSlot(inv, 0, 72+all_x, 36));
		
		for (int j=0;j<2;j++) for (int i=0;i<9;i++) {
			this.addSlotToContainer(new PotionSlot(inv, 1+i+9*j, 18*i + all_x, 18*j+all_y));
		}
		this.addPlayerSlots(pinv, all_x, 140);
		bag_name=inv.getName();
	}
	
	public String getBagName() {
		return bag_name;
	}
	
	@Override
	public ItemStack slotClick(int slotId, int mouseButton, ClickType clickTypeIn, EntityPlayer player) {
		if (slotId!=0 || clickTypeIn == ClickType.CLONE) return super.slotClick(slotId, mouseButton, clickTypeIn, player);
		ItemStack iso = player.inventory.getItemStack();
		if (!iso.isEmpty()) {
			if (!PotionUtils.getEffectsFromStack(iso).isEmpty()) {
				ItemStack nis = iso.copy();
				nis.setCount(1);
				((Slot) inventorySlots.get(slotId)).putStack(nis);
			}
		} else
			((Slot) inventorySlots.get(slotId)).putStack(ItemStack.EMPTY);
		return ItemStack.EMPTY;
	}
	
	
	
	@Override
	public boolean canDragIntoSlot(Slot slotIn) {
		return !(slotIn instanceof GhostPotionSlot);
	}
	
	public static class PotionSlot extends Slot {

		public PotionSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}
		
		@Override
		public boolean isItemValid(ItemStack stack) {
			return stack.getItem().equals(Items.POTIONITEM);
		}
		
		@Override
		public int getSlotStackLimit() {
			return 1;
		}
	}
	
	public static class GhostPotionSlot extends Slot {

		public GhostPotionSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}
		
		@Override
		public boolean isItemValid(ItemStack stack) {
			return !PotionUtils.getEffectsFromStack(stack).isEmpty() && stack.hasTagCompound() && !stack.getTagCompound().hasKey("alteredPotion");
		}
		
		@Override
		public int getSlotStackLimit() {
			return 1;
		}
				
	}
	
	@Override
	protected void addPlayerSlots(InventoryPlayer playerInventory, int x, int y) {
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, x + j * 18, y + i * 18));
			}
		}
		
		List<ItemStack> pinv = playerInventory.mainInventory;
		
		for (int i = 0; i < 9; ++i) {
			ItemStack isk = pinv.get(i);
			if (isk.isEmpty() || !isk.getItem().equals(ModItems.potion_bag)) addSlotToContainer(new Slot(playerInventory, i, x + i * 18, y + 58));
			else addSlotToContainer(new LockedSlot(playerInventory, i, x + i * 18, y + 58));
		}
	}
	
	public static class LockedSlot extends Slot {
		
		public LockedSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}
		
		@Override
		public boolean canTakeStack(EntityPlayer playerIn) {
			return false;
		}
		
		@Override
		public ItemStack decrStackSize(int amount) {
			return ItemStack.EMPTY;
		}
	}

}
