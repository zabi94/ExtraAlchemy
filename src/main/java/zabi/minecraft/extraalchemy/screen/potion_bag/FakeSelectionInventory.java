package zabi.minecraft.extraalchemy.screen.potion_bag;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtInt;
import net.minecraft.util.Hand;
import zabi.minecraft.extraalchemy.items.PotionBagItem;

public class FakeSelectionInventory implements Inventory {

	private Hand openedWith;
	
	public FakeSelectionInventory(Hand hand) {
		openedWith = hand;
	}

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
	
	@Override
	public void onClose(PlayerEntity player) {
		if (openedWith != null) {
			player.getStackInHand(openedWith).getOrCreateNbt().put(PotionBagItem.TAG_LAST_CHANGE, NbtInt.of(player.getRandom().nextInt()));
		}
	}
	
}
