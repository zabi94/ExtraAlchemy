package zabi.minecraft.extraalchemy.items;

import java.util.List;
import java.util.Optional;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.PotionUtil;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import zabi.minecraft.extraalchemy.client.tooltip.StatusEffectContainer;
import zabi.minecraft.extraalchemy.screen.potion_bag.BagInventory;
import zabi.minecraft.extraalchemy.screen.potion_bag.PotionBagScreenhandlerFactory;
import zabi.minecraft.extraalchemy.utils.LibMod;
import zabi.minecraft.extraalchemy.utils.Log;
import zabi.minecraft.extraalchemy.utils.PotionDelegate;

public class PotionBagItem extends Item implements DyeableItem, StatusEffectContainer {

	private static final String TAG_MODE = "ea_select_mode";
	public static final String TAG_INVENTORY = "ea_inventory";
	private static final String TAG_SELECTED = "ea_selected_potion";

	private static final TagKey<Item> TAG_POTION = TagKey.of(Registries.ITEM.getKey(), LibMod.id("potion_for_bag"));

	public PotionBagItem() {
		super(new Item.Settings().maxCount(1));
	}

	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);

		Optional<PotionDelegate> optpot = getSelectedPotion(stack);

		if (optpot.isPresent()) {
			int avail = getSelectedPotionAmount(stack).get();
			Text potion = Text.translatable(optpot.get().getTranslationKey()).formatted(Formatting.DARK_PURPLE, Formatting.BOLD);
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
			optpot.get().addToTooltip(tooltip);
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
				Optional<PotionDelegate> optpot = getSelectedPotion(stack);
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

	public void selectPotion(ItemStack bag, ItemStack potionStack) {
		if (potionStack == null) {
			bag.getOrCreateNbt().remove(TAG_SELECTED);
		} else {
			PotionDelegate pd = new PotionDelegate(potionStack); 
			bag.getOrCreateNbt().put(TAG_SELECTED, pd.serialize());
		}
	}

	@Override
	public ItemStack finishUsing(ItemStack bag, World world, LivingEntity user) {
		if (!world.isClient) {
			if (user instanceof PlayerEntity) {
				Optional<PotionDelegate> potopt = getSelectedPotion(bag);
				if (potopt.isPresent()) {
					PotionDelegate target = potopt.get();
					BagInventory inv = new BagInventory(bag, user.getActiveHand());
					findPotionAndApply(user, target, inv);
				}
				((PlayerEntity) user).getItemCooldownManager().set(this, 20);
			}
		}
		return super.finishUsing(bag, world, user);
	}

	private void findPotionAndApply(LivingEntity user, PotionDelegate target, BagInventory inv) {
		for (int i = 0; i < inv.size(); i++) {
			ItemStack currentStack = inv.getStack(i);
			if (target.match(currentStack)) {
				currentStack.getItem().finishUsing(currentStack, user.getEntityWorld(), user);
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

	public Optional<PotionDelegate> getSelectedPotion(ItemStack bag) {
		NbtCompound selectedTag = bag.getOrCreateNbt().getCompound(TAG_SELECTED);
		if (selectedTag.isEmpty()) return Optional.empty();
		return PotionDelegate.deserialize(selectedTag);
	}

	public Optional<Integer> getSelectedPotionAmount(ItemStack bag) {
		int count = 0;
		Optional<PotionDelegate> potopt = getSelectedPotion(bag);
		if (potopt.isPresent()) {
			BagInventory inv = new BagInventory(bag, null);
			for (int i = 0; i < inv.size(); i++) {
				if (potopt.get().match(inv.getStack(i))) count++;
			}
			return Optional.of(count);
		}
		return Optional.empty();
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
			PotionDelegate potion = new PotionDelegate(currentStack);
			if (!potion.isEmpty()) {
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

	public static enum SelectionMode {
		HOLD, NEXT, DESELECT
	}

	@Override
	public List<StatusEffectInstance> getContainedEffects(ItemStack stack) {
		Optional<PotionDelegate> optpot = getSelectedPotion(stack);
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
