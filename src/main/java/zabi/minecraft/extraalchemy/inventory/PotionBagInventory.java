package zabi.minecraft.extraalchemy.inventory;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class PotionBagInventory extends AutomatableInventory {
	
	private ItemStack bagStack;
	
	public PotionBagInventory(ItemStack stack) {
		super(19);
		bagStack=stack;
		this.loadFromNBT(bagStack.getOrCreateSubCompound("new_inv"));
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
	public boolean canMachineInsert(int slot, ItemStack stack) {
		return slot!=0 && stack.getItem()==Items.POTIONITEM;
	}

	@Override
	public boolean canMachineExtract(int slot, ItemStack stack) {
		return slot!=0;
	}

	@Override
	public void onMarkDirty() {
		NBTTagCompound bagTag = bagStack.getTagCompound();
		if (bagTag==null) bagTag = new NBTTagCompound();
		bagTag.setTag("new_inv", this.saveToNbt());
	}
}