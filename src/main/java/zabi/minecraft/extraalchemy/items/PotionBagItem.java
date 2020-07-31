package zabi.minecraft.extraalchemy.items;

import java.util.List;
import java.util.Optional;

import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.text.KeybindText;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import zabi.minecraft.extraalchemy.screen.ModContainerTypes;
import zabi.minecraft.extraalchemy.utils.Log;

public class PotionBagItem extends Item {

	public static final String TAG_MODE = "ea_select_mode";
	public static final String TAG_INVENTORY = "ea_inventory";
	public static final String TAG_SELECTED = "ea_selected";

	public PotionBagItem() {
		super(new Item.Settings().maxCount(1).group(ItemSettings.EXTRA_ALCHEMY_GROUP));
	}

	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);

		Optional<Potion> optpot = getSelectedPotion(stack);

		if (optpot.isPresent()) {
			tooltip.add(new TranslatableText("item.extraalchemy.potion_bag.selected.potion", new TranslatableText(optpot.get().finishTranslationKey("item.minecraft.potion.effect.")).formatted(Formatting.BOLD, Formatting.DARK_PURPLE)));
			int avail = getSelectedPotionAmount(stack).get();
			tooltip.add(new TranslatableText("item.extraalchemy.potion_bag.selected.available", new LiteralText(""+avail).formatted(avail == 0 ? Formatting.DARK_RED : Formatting.RESET, avail == 0 ? Formatting.DARK_RED : Formatting.BOLD)));
		} else {
			tooltip.add(new TranslatableText("item.extraalchemy.potion_bag.selected.none"));
		}
		Text option = new TranslatableText("item.extraalchemy.potion_bag.autoselect.option."+getSelectionMode(stack).toString().toLowerCase()).formatted(Formatting.GOLD);
		tooltip.add(new TranslatableText("item.extraalchemy.potion_bag.autoselect", option));
		tooltip.add(new TranslatableText("item.extraalchemy.potion_bag.autoselect.change", new KeybindText("key.extraalchemy.potion_bag_mode")));

		if (optpot.isPresent()) {
			tooltip.add(new LiteralText(""));
			PotionUtil.buildTooltip(PotionUtil.setPotion(new ItemStack(Items.POTION), optpot.get()), tooltip, 1f);
		}
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		if (!world.isClient) {
			if (user.isSneaking()) {
				ContainerProviderRegistry.INSTANCE.openContainer(ModContainerTypes.POTION_BAG, user, buf -> {
					buf.writeBoolean(hand == Hand.MAIN_HAND);
				});
			} else {
				handleRefill(stack); 
				Optional<Potion> optpot = getSelectedPotion(stack);
				if (getSelectedPotionAmount(stack).get() > 0 && optpot.isPresent()) {
					user.setCurrentHand(hand);
				} 
				user.inventory.markDirty();
			}
		}
		return TypedActionResult.success(stack);
	}

	private void handleRefill(ItemStack stack) {
		if (getSelectedPotionAmount(stack).get() == 0) {
			switch (getSelectionMode(stack)) {
			case DESELECT:
				selectPotion(stack, null);
				break;
			case NEXT: 
				getFirstAvailablePotion(stack).ifPresent(potionStack -> {
					selectPotion(stack, potionStack);
				});
				break;
			case HOLD:
			default:
				break;
			}
		}
	}


	private void selectPotion(ItemStack bag, ItemStack potionStack) {
		if (potionStack == null) {
			bag.getOrCreateTag().remove(TAG_SELECTED);
		} else {
			CompoundTag potionTag = new CompoundTag();
			potionTag.putString("Potion", Registry.POTION.getId(PotionUtil.getPotion(potionStack)).toString());
			bag.getOrCreateTag().put(TAG_SELECTED, potionTag);
		}
	}

	@Override
	public ItemStack finishUsing(ItemStack bag, World world, LivingEntity user) {
		if (!world.isClient) {
			if (user instanceof PlayerEntity) {
				Optional<Potion> potopt = getSelectedPotion(bag);
				if (potopt.isPresent()) {
					Potion target = potopt.get();
					BagInventory inv = new BagInventory(bag, user.getActiveHand());
					findPotionAndApply(user, target, inv);
				}
				((PlayerEntity) user).getItemCooldownManager().set(this, 20);
			}
		}
		return super.finishUsing(bag, world, user);
	}

	private void findPotionAndApply(LivingEntity user, Potion target, BagInventory inv) {
		for (int i = 0; i < inv.getInvSize(); i++) {
			ItemStack currentStack = inv.getInvStack(i);
			Potion isp = PotionUtil.getPotion(currentStack);
			if (isp.equals(target) && currentStack.getItem() != Items.AIR) {
				currentStack.decrement(1);
				for (StatusEffectInstance sei:target.getEffects()) {
					user.addStatusEffect(new StatusEffectInstance(sei.getEffectType(), sei.getDuration(), sei.getAmplifier(), false, false, true));
				}
				inv.onInvClose((PlayerEntity) user);
				((PlayerEntity) user).inventory.markDirty();
				break;
			}
		}
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 20;
	}
	
	@Override
	public boolean hasEnchantmentGlint(ItemStack stack) {
		return getSelectedPotion(stack).isPresent();
	}

	public Optional<Potion> getSelectedPotion(ItemStack bag) {
		Potion pot = PotionUtil.getPotion(bag.getOrCreateTag().getCompound(TAG_SELECTED));
		if (pot.getEffects().isEmpty()) return Optional.empty();
		return Optional.of(pot);
	}

	public Optional<Integer> getSelectedPotionAmount(ItemStack bag) {
		int count = 0;
		Optional<Potion> potopt = getSelectedPotion(bag);
		if (potopt.isPresent()) {
			BagInventory inv = new BagInventory(bag, null);
			for (int i = 0; i < inv.getInvSize(); i++) {
				Potion isp = PotionUtil.getPotion(inv.getInvStack(i));
				if (isp.equals(potopt.get()) && inv.getInvStack(i).getItem() != Items.AIR) count++;
			}
		}
		return Optional.of(count);
	}

	public SelectionMode getSelectionMode(ItemStack bag) {
		return SelectionMode.values()[bag.getOrCreateTag().getInt(TAG_MODE) % SelectionMode.values().length];
	}

	public static void toggleStatusForPlayer(PlayerEntity player, Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		if (stack.getItem() == ModItems.POTION_BAG) {
			int current_mode = stack.getOrCreateTag().getInt(TAG_MODE);
			stack.getTag().putInt(TAG_MODE, (current_mode + 1) % SelectionMode.values().length);
			player.inventory.markDirty();
		} else {
			Log.w("Not holding a bag");
		}
	}

	public Optional<ItemStack> getFirstAvailablePotion(ItemStack stack) {
		BagInventory inv = new BagInventory(stack, null);
		for (int i = 0; i < inv.getInvSize(); i++) {
			ItemStack currentStack = inv.getInvStack(i);
			Potion isp = PotionUtil.getPotion(currentStack);
			if (!isp.getEffects().isEmpty() && currentStack.getItem() != Items.AIR) {
				return Optional.of(currentStack.copy());
			}
		}
		return Optional.empty();
	}

	public static class BagInventory implements Inventory {

		private static final int SLOT_AMOUNT = 18;

		private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(SLOT_AMOUNT, ItemStack.EMPTY);
		private Hand openedWith;

		public BagInventory(ItemStack bag, Hand hand) {
			openedWith = hand;
			deserialize(bag.getOrCreateTag().getCompound(TAG_INVENTORY));
		}

		@Override
		public void clear() {
			inventory.clear();
		}

		@Override
		public int getInvSize() {
			return SLOT_AMOUNT;
		}
		
		@Override
		public boolean isInvEmpty() {
			return inventory.stream().allMatch(ItemStack::isEmpty);
		}

		@Override
		public ItemStack getInvStack(int slot) {
			return inventory.get(slot);
		}

		@Override
		public ItemStack takeInvStack(int slot, int amount) {
			return Inventories.splitStack(inventory, slot, amount);
		}
		
		@Override
		public ItemStack removeInvStack(int slot) {
			return Inventories.removeStack(inventory, slot);
		}

		@Override
		public void setInvStack(int slot, ItemStack stack) {
			inventory.set(slot, stack);
		}

		@Override
		public void markDirty() {
			//NO-OP
		}
		
		@Override
		public boolean canPlayerUseInv(PlayerEntity player) {
			if (openedWith == null) {
				return true; //Client
			}
			return player.getStackInHand(openedWith).getItem() instanceof PotionBagItem;
		}

		@Override
		public void onInvClose(PlayerEntity player) {
			if (openedWith != null) {
				player.getStackInHand(openedWith).getOrCreateTag().put(TAG_INVENTORY, serialize(inventory));
			} else {
				if (!player.world.isClient) {
					Log.w("Server did not have any hand info associated with the inventory");
				}
			}
		}

		private Tag serialize(DefaultedList<ItemStack> items) {
			CompoundTag tag = new CompoundTag();
			for (int i = 0; i < items.size(); i++) {
				tag.put("inv"+i, items.get(i).toTag(new CompoundTag()));
			}
			return tag;
		}

		private void deserialize(CompoundTag tag) {
			for (int i = 0; i < inventory.size(); i++) {
				inventory.set(i, ItemStack.fromTag(tag.getCompound("inv"+i)));
			}
		}

		@Override
		public boolean isValidInvStack(int slot, ItemStack stack) {
			return !PotionUtil.getPotion(stack).getEffects().isEmpty();
		}

	}

	public static enum SelectionMode {
		HOLD, NEXT, DESELECT
	}


}
