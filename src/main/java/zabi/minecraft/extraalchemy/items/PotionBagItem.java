package zabi.minecraft.extraalchemy.items;

import java.util.List;
import java.util.Optional;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import zabi.minecraft.extraalchemy.client.tooltip.StatusEffectContainer;
import zabi.minecraft.extraalchemy.screen.potion_bag.PotionBagScreenhandlerFactory;
import zabi.minecraft.extraalchemy.utils.LibMod;
import zabi.minecraft.extraalchemy.utils.Log;

public class PotionBagItem extends Item implements DyeableItem, StatusEffectContainer {

	public static final String TAG_MODE = "ea_select_mode";
	public static final String TAG_INVENTORY = "ea_inventory";
	public static final String TAG_SELECTED = "ea_selected";

	private static final TagKey<Item> TAG_POTION = TagKey.of(Registries.ITEM.getKey(), LibMod.id("potion_for_bag"));

	public PotionBagItem() {
		super(new Item.Settings().maxCount(1));
	}

	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);

		Optional<Potion> optpot = getSelectedPotion(stack);

		if (optpot.isPresent()) {
			int avail = getSelectedPotionAmount(stack).get();
			Text potion = Text.translatable(optpot.get().finishTranslationKey("item.minecraft.potion.effect.")).formatted(Formatting.DARK_PURPLE, Formatting.BOLD);
			Text amount = Text.literal(""+avail).formatted(Formatting.BLUE);
			tooltip.add(Text.translatable("item.extraalchemy.potion_bag.selected.potion", potion, amount));
			
		} else {
			tooltip.add(Text.translatable("item.extraalchemy.potion_bag.selected.none").formatted(Formatting.DARK_RED));
		}
		Text option = Text.translatable("item.extraalchemy.potion_bag.autoselect.option."+getSelectionMode(stack).toString().toLowerCase()).formatted(Formatting.GOLD);
		tooltip.add(Text.translatable("item.extraalchemy.potion_bag.autoselect", option));
		tooltip.add(Text.translatable("item.extraalchemy.potion_bag.autoselect.change", Text.keybind("key.extraalchemy.potion_bag_mode").formatted(Formatting.AQUA)));

		if (optpot.isPresent()) {
			tooltip.add(Text.empty());
			PotionUtil.buildTooltip(PotionUtil.setPotion(new ItemStack(Items.POTION), optpot.get()), tooltip, 1f);
		}
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		if (!world.isClient) {
			if (user.isSneaking()) {
				PotionBagScreenhandlerFactory factory = new PotionBagScreenhandlerFactory(stack, hand);
				user.openHandledScreen(factory);
			} else {
				handleRefill(stack); 
				Optional<Potion> optpot = getSelectedPotion(stack);
				if (getSelectedPotionAmount(stack).get() > 0 && optpot.isPresent()) {
					user.setCurrentHand(hand);
				} 
				user.getInventory().markDirty();
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
			bag.getOrCreateNbt().remove(TAG_SELECTED);
		} else {
			NbtCompound potionTag = new NbtCompound();
			potionTag.putString("Potion", Registries.POTION.getId(PotionUtil.getPotion(potionStack)).toString());
			bag.getOrCreateNbt().put(TAG_SELECTED, potionTag);
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
		for (int i = 0; i < inv.size(); i++) {
			ItemStack currentStack = inv.getStack(i);
			Potion isp = PotionUtil.getPotion(currentStack);
			if (isp.equals(target) && currentStack.getItem() != Items.AIR) {
				currentStack.getItem().finishUsing(currentStack, user.world, user);
				break;
			}
		}
		inv.onClose((PlayerEntity) user);
		PlayerInventory pinv = ((PlayerEntity) user).getInventory();
		pinv.markDirty();
	}
	
	public static boolean isValidPotionItem(ItemStack stack) {
		if (stack.isIn(TAG_POTION)) {
			return !PotionUtil.getPotionEffects(stack).isEmpty();
		}
		return false;
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
	public boolean hasGlint(ItemStack stack) {
		return getSelectedPotion(stack).isPresent();
	}

	public Optional<Potion> getSelectedPotion(ItemStack bag) {
		Potion pot = PotionUtil.getPotion(bag.getOrCreateNbt().getCompound(TAG_SELECTED));
		if (pot.getEffects().isEmpty()) return Optional.empty();
		return Optional.of(pot);
	}

	public Optional<Integer> getSelectedPotionAmount(ItemStack bag) {
		int count = 0;
		Optional<Potion> potopt = getSelectedPotion(bag);
		if (potopt.isPresent()) {
			BagInventory inv = new BagInventory(bag, null);
			for (int i = 0; i < inv.size(); i++) {
				Potion isp = PotionUtil.getPotion(inv.getStack(i));
				if (isp.equals(potopt.get()) && inv.getStack(i).getItem() != Items.AIR) count++;
			}
		}
		return Optional.of(count);
	}

	public SelectionMode getSelectionMode(ItemStack bag) {
		return SelectionMode.values()[bag.getOrCreateNbt().getInt(TAG_MODE) % SelectionMode.values().length];
	}

	public static void toggleStatusForPlayer(PlayerEntity player, Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		if (stack.getItem() == ModItems.POTION_BAG) {
			int current_mode = stack.getOrCreateNbt().getInt(TAG_MODE);
			stack.getNbt().putInt(TAG_MODE, (current_mode + 1) % SelectionMode.values().length);
			player.getInventory().markDirty();
		} else {
			Log.w("Not holding a bag");
		}
	}

	public Optional<ItemStack> getFirstAvailablePotion(ItemStack stack) {
		BagInventory inv = new BagInventory(stack, null);
		for (int i = 0; i < inv.size(); i++) {
			ItemStack currentStack = inv.getStack(i);
			Potion isp = PotionUtil.getPotion(currentStack);
			if (!isp.getEffects().isEmpty() && currentStack.getItem() != Items.AIR) {
				return Optional.of(currentStack.copy());
			}
		}
		return Optional.empty();
	}
	
	@Override
	public int getColor(ItemStack stack) {
		NbtCompound compoundTag = stack.getSubNbt("display");
		return compoundTag != null && compoundTag.contains("color", 99) ? compoundTag.getInt("color") : 0xce7720;
	}

	public static class BagInventory implements Inventory {

		private static final int SLOT_AMOUNT = 18;

		private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(SLOT_AMOUNT, ItemStack.EMPTY);
		private Hand openedWith;

		public BagInventory(ItemStack bag, Hand hand) {
			openedWith = hand;
			deserialize(bag.getOrCreateNbt().getCompound(TAG_INVENTORY));
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
				player.getStackInHand(openedWith).getOrCreateNbt().put(TAG_INVENTORY, serialize(inventory));
			} else {
				if (!player.world.isClient) {
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

	public static enum SelectionMode {
		HOLD, NEXT, DESELECT
	}

	@Override
	public List<StatusEffectInstance> getContainedEffects(ItemStack stack) {
		Optional<Potion> optpot = getSelectedPotion(stack);
		if (optpot.isEmpty()) {
			return List.of();
		}
		return optpot.get().getEffects();
	}

	@Override
	public boolean hasEffects(ItemStack stack) {
		return getSelectedPotion(stack).isPresent() && StatusEffectContainer.super.hasEffects(stack);
	}

}
