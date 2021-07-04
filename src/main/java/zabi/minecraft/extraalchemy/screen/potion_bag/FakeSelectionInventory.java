package zabi.minecraft.extraalchemy.screen.potion_bag;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class FakeSelectionInventory implements Inventory {

	@Override
	public void clear() {
		//No op
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public ItemStack getStack(int slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeStack(int slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		// No op
	}

	@Override
	public void markDirty() {
		// No op
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return false;
	}

}
