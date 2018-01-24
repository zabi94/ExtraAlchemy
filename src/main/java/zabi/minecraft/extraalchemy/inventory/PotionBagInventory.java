package zabi.minecraft.extraalchemy.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class PotionBagInventory implements IInventory {
	
	private ItemStack bagStack;
	
	public PotionBagInventory(ItemStack stack) {
		bagStack=stack;
	}
	
	@Override
	public int getSizeInventory() {
		return 19;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return new ItemStack(bagStack.getTagCompound().getCompoundTag("inventory").getCompoundTag("inv"+index));
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack oldStack = getStackInSlot(index).copy();
		if (index==0) {
			removeStackFromSlot(0);//Rimuovi se va in casino
			return ItemStack.EMPTY;
		}
		NBTTagCompound newStack = new NBTTagCompound();
		ItemStack ist = getStackInSlot(index).copy();
		ist.setCount(ist.getCount()-count);
		if (ist!=null) ist.writeToNBT(newStack);
		bagStack.getTagCompound().getCompoundTag("inventory").setTag("inv"+index, newStack);
		oldStack.setCount(count);
		return oldStack;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack its = getStackInSlot(index).copy();
		setInventorySlotContents(index, ItemStack.EMPTY);
		return index==0?ItemStack.EMPTY:its;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		NBTTagCompound tg = new NBTTagCompound();
		if (index>0 || stack.isEmpty()) {
			if (stack!=null) stack.copy().writeToNBT(tg);
			bagStack.getTagCompound().getCompoundTag("inventory").setTag("inv"+index, tg);
		} else {
			ItemStack pot = new ItemStack(Items.POTIONITEM);
			PotionUtils.addPotionToItemStack(pot, PotionUtils.getPotionFromItem(stack));
			pot.writeToNBT(tg);
			bagStack.getTagCompound().getCompoundTag("inventory").setTag("inv0", tg);
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void markDirty() {
		//No-op
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return !stack.isEmpty() && !PotionUtils.getEffectsFromStack(stack).isEmpty() && stack.hasTagCompound() && !stack.getTagCompound().hasKey("alteredPotion");
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		for (int i = 1; i < getSizeInventory();i++) setInventorySlotContents(i, ItemStack.EMPTY);
	}

	@Override
	public String getName() {
		return hasCustomName()?bagStack.getDisplayName():bagStack.getItem().getItemStackDisplayName(bagStack);
	}

	@Override
	public boolean hasCustomName() {
		return bagStack.hasDisplayName();
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentString(getName());
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return true;
	}
}