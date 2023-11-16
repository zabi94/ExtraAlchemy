package zabi.minecraft.extraalchemy.screen.potion_bag;

import java.util.Optional;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import zabi.minecraft.extraalchemy.items.ModItems;
import zabi.minecraft.extraalchemy.items.PotionBagItem;
import zabi.minecraft.extraalchemy.screen.ModScreenHandlerTypes;
import zabi.minecraft.extraalchemy.utils.PotionDelegate;

public class PotionBagScreenHandler extends ScreenHandler {

	protected BagInventory bagInventory;
	protected FakeSelectionInventory fakeInventory;
	protected PlayerInventory playerInventory;
	protected ItemStack bagStack;
	
	public PotionBagScreenHandler(int syncId, PlayerInventory playerInventory, PlayerEntity player, Hand hand) {
		super(ModScreenHandlerTypes.POTION_BAG, syncId);
		if (hand == null) {
			Item mainHand = player.getMainHandStack().getItem();
			Item offHand = player.getOffHandStack().getItem();
			if (mainHand == ModItems.POTION_BAG) {
				hand = Hand.MAIN_HAND;
			} else if (offHand == ModItems.POTION_BAG) {
				hand = Hand.OFF_HAND;
			}
		}
		this.bagStack = player.getStackInHand(hand);
		this.playerInventory = playerInventory;
		bagInventory = new BagInventory(bagStack, hand);
		fakeInventory = new FakeSelectionInventory();
		
		addSlot(new SelectorSlot(fakeInventory, bagStack, 80, 36));
		for (int j=0;j<2;j++) for (int i=0;i<9;i++) {
			this.addSlot(new PotionOnlySlot(bagInventory, i+9*j, 18*i + 8, 18*j+90));
		}
		addPlayerSlots(playerInventory, 8, 140);
	}
	
	public PotionBagScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, playerInventory.player, null);
	}
	
	protected void addPlayerSlots(PlayerInventory playerInventory, int x, int y) {
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				if (playerInventory.main.get(j + (i * 9) + 9).getItem() == ModItems.POTION_BAG) {
					this.addSlot(new BagLockedSlot(playerInventory, j + (i * 9) + 9, x + (j * 18), y + (i * 18)));
				} else {
					this.addSlot(new Slot(playerInventory, j + (i * 9) + 9, x + (j * 18), y + (i * 18)));
				}
			}
		}
		for (int i = 0; i < 9; ++i) {
			if (playerInventory.main.get(i).getItem() == ModItems.POTION_BAG) {
				this.addSlot(new BagLockedSlot(playerInventory, i, x + (i * 18), y + 58));
			} else {
				this.addSlot(new Slot(playerInventory, i, x + (i * 18), y + 58));
			}
		}
	}
	
	@Override
	public boolean canUse(PlayerEntity player) {
		return true;
	}
	
	@Override
	public void onClosed(PlayerEntity player) {
		bagInventory.onClose(player);
		fakeInventory.onClose(player);
		player.getInventory().markDirty();
		player.getInventory().onClose(player);
		super.onClosed(player);
	}
	
	@Override
	public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
		return stack.getItem() != ModItems.POTION_BAG && slot.getStack().getItem() != ModItems.POTION_BAG && slot.canInsert(stack);
	}
	
	@Override
	public boolean canInsertIntoSlot(Slot slot) {
		return super.canInsertIntoSlot(slot) && !(slot instanceof BagLockedSlot);
	}
	
	@Override
	protected boolean insertItem(ItemStack stack, int startIndex, int endIndex, boolean fromLast) {
		if (stack.getItem() == ModItems.POTION_BAG) {
			return false;
		}
		return super.insertItem(stack, startIndex, endIndex, fromLast);
	}
	
	@Override
	public void onSlotClick(int slotId, int mouseButton, SlotActionType actionType, PlayerEntity player) {
		
		if (actionType == SlotActionType.SWAP) return;
		
		if (slotId!=0 || (actionType == SlotActionType.CLONE && player.getAbilities().creativeMode && this.getCursorStack().isEmpty())) {
			super.onSlotClick(slotId, mouseButton, actionType, player);
			return;
		}

		if (actionType != SlotActionType.PICKUP) return;

		//slotId = 0, actionType = PICKUP
		ItemStack iso = this.getCursorStack();
		if (!iso.isEmpty()) {
			PotionDelegate pd = new PotionDelegate(iso);
			if (!pd.isEmpty()) {
				ItemStack nis = iso.copy();
				nis.setCount(1);
				((Slot) slots.get(slotId)).setStackNoCallbacks(nis);
			}
		} else {
			((Slot) slots.get(slotId)).setStackNoCallbacks(ItemStack.EMPTY);
		}
		
		this.playerInventory.markDirty();
	}
	
	@Override
	public ItemStack quickMove(PlayerEntity player, int index) {
		if (index > 18) {
			return transferToBag(player, index);
		} else {
			return transferToPlayer(player, index);
		}
	}

	private ItemStack transferToBag(PlayerEntity player, int index) {
		return mergeAndUpdate(player, this.slots.get(index), index, 1, 18, false);
	}

	private ItemStack transferToPlayer(PlayerEntity player, int index) {
		return mergeAndUpdate(player, this.slots.get(index), index, 19, 19+36, true);
	}
	
	protected ItemStack mergeAndUpdate(PlayerEntity player, Slot slot, int slotIndex, int startIndex, int stopIndex, boolean reversed) {
		final ItemStack origStack = slot.getStack();
		final ItemStack copyStack = origStack.copy();
		if (!this.insertItem(origStack, startIndex, stopIndex, reversed)) {
			return ItemStack.EMPTY;
		}
		slot.onQuickTransfer(origStack, copyStack);
		if (origStack.isEmpty()) {
			slot.setStackNoCallbacks(ItemStack.EMPTY);
		} else {
			slot.markDirty();
		}
		if (origStack.getCount() == copyStack.getCount()) {
			return ItemStack.EMPTY;
		}
		slot.onTakeItem(player, origStack);
		return origStack;
	}

	public static class SelectorSlot extends Slot {

		private ItemStack bagStack;

		public SelectorSlot(Inventory inventory, ItemStack stack, int x, int y) {
			super(inventory, 0, x, y);
			bagStack = stack;
		}

		@Override
		public void onQuickTransfer(ItemStack originalItem, ItemStack itemStack) {
		}
		
		@Override
		public void onTakeItem(PlayerEntity player, ItemStack stack) {
			ModItems.POTION_BAG.selectPotion(bagStack, null);
			this.markDirty();
		}
		
		@Override
		public boolean canInsert(ItemStack stack) {
			return !(new PotionDelegate(stack).isEmpty());
		}

		@Override
		public int getMaxItemCount() {
			return 1;
		}

		@Override
		public ItemStack getStack() {
			Optional<PotionDelegate> selectedOpt = ModItems.POTION_BAG.getSelectedPotion(bagStack);
			if (selectedOpt.isPresent()) {
				return selectedOpt.get().getStack();
			} else {
				return ItemStack.EMPTY;
			}
		}

		@Override
		public void setStackNoCallbacks(ItemStack stack) {
			ModItems.POTION_BAG.selectPotion(bagStack, stack);
			this.markDirty();
		}

		@Override
		public ItemStack takeStack(int amount) {
			ModItems.POTION_BAG.selectPotion(bagStack, null);
			this.markDirty();
			return ItemStack.EMPTY;
		}

	}
	
	public static class PotionOnlySlot extends Slot {
		
		public PotionOnlySlot(Inventory inventory, int index, int x, int y) {
			super(inventory, index, x, y);
		}
		
		@Override
		public boolean canInsert(ItemStack stack) {
			return PotionBagItem.isValidPotionItem(stack);
		}
		
	}

	public static class BagLockedSlot extends Slot {

		public BagLockedSlot(Inventory inventory, int index, int x, int y) {
			super(inventory, index, x, y);
		}
		
		@Override
		public boolean canTakeItems(PlayerEntity playerEntity) {
			return false;
		}
		
		@Override
		public void setStackNoCallbacks(ItemStack stack) {
			//NO-OP
		}
		
		@Override
		public ItemStack takeStack(int amount) {
			return ItemStack.EMPTY;
		}
		
		@Override
		public boolean isEnabled() {
			return true;
		}
		
	}
}
