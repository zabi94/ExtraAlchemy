package zabi.minecraft.extraalchemy.screen.potion_bag;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import zabi.minecraft.extraalchemy.items.PotionBagItem;
import zabi.minecraft.extraalchemy.utils.Log;

public class BagInventory implements Inventory {

	private static final int SLOT_AMOUNT = 18;

	private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(SLOT_AMOUNT, ItemStack.EMPTY);
	private Hand openedWith;

	public BagInventory(ItemStack bag, Hand hand) {
		openedWith = hand;
		deserialize(bag.getOrCreateNbt().getCompound(PotionBagItem.TAG_INVENTORY));
	}

	@Override
	public void clear() {
		inventory.clear();
	}

	@Override
	public int size() {
		return SLOT_AMOUNT;
	}

	@Override
	public boolean isEmpty() {
		return inventory.stream().allMatch(ItemStack::isEmpty);
	}

	@Override
	public ItemStack getStack(int slot) {
		return inventory.get(slot);
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		return Inventories.splitStack(inventory, slot, amount);
	}

	@Override
	public ItemStack removeStack(int slot) {
		return Inventories.removeStack(inventory, slot);
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		inventory.set(slot, stack);
	}

	@Override
	public void markDirty() {
		//NO-OP
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		if (openedWith == null) {
			return true; //Client
		}
		return player.getStackInHand(openedWith).getItem() instanceof PotionBagItem;
	}

	@Override
	public void onClose(PlayerEntity player) {
		if (openedWith != null) {
			player.getStackInHand(openedWith).getOrCreateNbt().put(PotionBagItem.TAG_INVENTORY, serialize(inventory));
		} else {
			if (!player.getEntityWorld().isClient) {
				Log.w("Server did not have any hand info associated with the inventory");
			}
		}
	}

	private NbtElement serialize(DefaultedList<ItemStack> items) {
		NbtCompound tag = new NbtCompound();
		for (int i = 0; i < items.size(); i++) {
			tag.put("inv"+i, items.get(i).writeNbt(new NbtCompound()));
		}
		return tag;
	}

	private void deserialize(NbtCompound tag) {
		for (int i = 0; i < inventory.size(); i++) {
			inventory.set(i, ItemStack.fromNbt(tag.getCompound("inv"+i)));
		}
	}

	@Override
	public boolean isValid(int slot, ItemStack stack) {
		return PotionBagItem.isValidPotionItem(stack);
	}

}