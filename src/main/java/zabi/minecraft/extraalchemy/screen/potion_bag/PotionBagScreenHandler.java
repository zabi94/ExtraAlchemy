package zabi.minecraft.extraalchemy.screen.potion_bag;

import java.util.Optional;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import zabi.minecraft.extraalchemy.items.ModItems;
import zabi.minecraft.extraalchemy.items.PotionBagItem;
import zabi.minecraft.extraalchemy.screen.ModScreenHandlerTypes;

public class PotionBagScreenHandler extends ScreenHandler {

	protected PotionBagItem.BagInventory bagInventory;
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
		bagInventory = new PotionBagItem.BagInventory(bagStack, hand);
		addSlot(new SelectorSlot(bagStack, 80, 36));
		for (int j=0;j<2;j++) for (int i=0;i<9;i++) {
			this.addSlot(new Slot(bagInventory, i+9*j, 18*i + 8, 18*j+90));
		}
		addPlayerSlots(playerInventory, 8, 140);
	}
	
	public PotionBagScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, playerInventory.player, null);
	}
	
	protected void addPlayerSlots(PlayerInventory playerInventory, int x, int y) {
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInventory, j + (i * 9) + 9, x + (j * 18), y + (i * 18)));
			}
		}
		for (int i = 0; i < 9; ++i) {
			this.addSlot(new Slot(playerInventory, i, x + (i * 18), y + 58));
		}
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return true;
	}
	
	@Override
	public void close(PlayerEntity player) {
		bagInventory.onClose(player);
		player.inventory.markDirty();
		super.close(player);
	}
	
	@Override
	public ItemStack onSlotClick(int slotId, int mouseButton, SlotActionType actionType, PlayerEntity player) {
		if (slotId!=0 || actionType == SlotActionType.CLONE) return super.onSlotClick(slotId, mouseButton, actionType, player);

		if (actionType != SlotActionType.PICKUP) return ItemStack.EMPTY;

		//slotId = 0, actionType = PICKUP
		
		ItemStack iso = player.inventory.getCursorStack();
		if (!iso.isEmpty()) {
			if (!PotionUtil.getPotion(iso).getEffects().isEmpty()) {
				ItemStack nis = iso.copy();
				nis.setCount(1);
				((Slot) slots.get(slotId)).setStack(nis);
			}
		} else {
			((Slot) slots.get(slotId)).setStack(ItemStack.EMPTY);
		}
		return ItemStack.EMPTY;
	}
	
	@Override
	public ItemStack transferSlot(PlayerEntity player, int index) {
		return ItemStack.EMPTY; //TODO
	}

	public static class SelectorSlot extends Slot {

		private ItemStack bagStack;

		public SelectorSlot(ItemStack stack, int x, int y) {
			super(null, 0, x, y);
			bagStack = stack;
		}

		@Override
		public void onStackChanged(ItemStack originalItem, ItemStack itemStack) {
		}

		@Override
		public ItemStack onTakeItem(PlayerEntity player, ItemStack stack) {
			bagStack.getOrCreateTag().remove(PotionBagItem.TAG_SELECTED);
			return ItemStack.EMPTY;
		}

		@Override
		public boolean canInsert(ItemStack stack) {
			return !PotionUtil.getPotion(stack).getEffects().isEmpty();
		}

		@Override
		public int getMaxStackAmount() {
			return 1;
		}

		@Override
		public ItemStack getStack() {
			Optional<Potion> selectedOpt = ModItems.POTION_BAG.getSelectedPotion(bagStack);
			if (selectedOpt.isPresent()) {
				return PotionUtil.setPotion(new ItemStack(Items.POTION), selectedOpt.get());
			} else {
				return ItemStack.EMPTY;
			}
		}

		@Override
		public boolean hasStack() {
			return ModItems.POTION_BAG.getSelectedPotion(bagStack).isPresent();
		}

		@Override
		public void setStack(ItemStack stack) {
			bagStack.getOrCreateTag().put(PotionBagItem.TAG_SELECTED, stack.getOrCreateTag());
		}

		@Override
		public ItemStack takeStack(int amount) {
			bagStack.getOrCreateTag().remove(PotionBagItem.TAG_SELECTED);
			return ItemStack.EMPTY;
		}

	}

}
