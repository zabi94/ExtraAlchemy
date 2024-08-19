package zabi.minecraft.extraalchemy.items;

import java.util.List;
import java.util.Optional;

import com.mojang.serialization.Codec;

import net.minecraft.client.item.TooltipType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
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
import zabi.minecraft.extraalchemy.utils.CodecUtil;
import zabi.minecraft.extraalchemy.utils.LibMod;
import zabi.minecraft.extraalchemy.utils.Log;
import zabi.minecraft.extraalchemy.utils.PotionUtilities;

public class PotionBagItem extends Item implements StatusEffectContainer {

	public static final String TAG_INVENTORY = "ea_inventory";
//	public static final String TAG_LAST_CHANGE = "ea_changed";

	private static final TagKey<Item> TAG_POTION = TagKey.of(Registries.ITEM.getKey(), LibMod.id("potion_for_bag"));
	
	public PotionBagItem() {
		super(new Item.Settings().maxCount(1)
				.component(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT)
				.component(ModComponents.SELECTION_MODE, SelectionMode.DESELECT)
		);
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		super.appendTooltip(stack, context, tooltip, type);
		
		PotionContentsComponent selectedPotion = stack.get(DataComponentTypes.POTION_CONTENTS);
		
		if (selectedPotion.hasEffects() && selectedPotion.potion().isPresent()) {
			int avail = getSelectedPotionAmount(stack).get();
			Text potion_text = Text.translatable(Potion.finishTranslationKey(selectedPotion.potion(), "")).formatted(Formatting.DARK_PURPLE, Formatting.BOLD);
			Text amount_text = Text.literal(""+avail).formatted(Formatting.BLUE);
			tooltip.add(Text.translatable("item.extraalchemy.potion_bag.selected.potion", potion_text, amount_text));
			
		} else {
			tooltip.add(Text.translatable("item.extraalchemy.potion_bag.selected.none").formatted(Formatting.DARK_RED));
		}
		Text option = Text.translatable("item.extraalchemy.potion_bag.autoselect.option."+getSelectionMode(stack).toString().toLowerCase()).formatted(Formatting.GOLD);
		tooltip.add(Text.translatable("item.extraalchemy.potion_bag.autoselect", option));
		tooltip.add(Text.translatable("item.extraalchemy.potion_bag.autoselect.change", Text.keybind("key.extraalchemy.potion_bag_mode").formatted(Formatting.AQUA)));

		if (selectedPotion.hasEffects() && selectedPotion.potion().isPresent()) {
			selectedPotion.buildTooltip(tooltip::add, 1, context.getUpdateTickRate());
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
				PotionContentsComponent selectedPotion = stack.get(DataComponentTypes.POTION_CONTENTS);
				if (getSelectedPotionAmount(stack).get() > 0 && selectedPotion.hasEffects() && selectedPotion.potion().isPresent()) {
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

	public static void selectPotion(ItemStack bag, ItemStack potionStack) {
		if (potionStack == null) {
			bag.set(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT);
		} else {
			bag.set(DataComponentTypes.POTION_CONTENTS, potionStack.get(DataComponentTypes.POTION_CONTENTS));
		}
	}

	@Override
	public ItemStack finishUsing(ItemStack bag, World world, LivingEntity user) {
		if (!world.isClient) {
			if (user instanceof PlayerEntity) {
				PotionContentsComponent selectedPotion = bag.get(DataComponentTypes.POTION_CONTENTS);
				if (selectedPotion.hasEffects() && selectedPotion.potion().isPresent()) {
					BagInventory inv = new BagInventory(bag, user.getActiveHand());
					findPotionAndApply(user, selectedPotion, inv);
				}
				((PlayerEntity) user).getItemCooldownManager().set(this, 20);
			}
		}
		return super.finishUsing(bag, world, user);
	}

	private void findPotionAndApply(LivingEntity user, PotionContentsComponent target, BagInventory inv) {
		for (int i = 0; i < inv.size(); i++) {
			ItemStack currentStack = inv.getStack(i);
			PotionContentsComponent currentPotion = currentStack.get(DataComponentTypes.POTION_CONTENTS);
			if (currentPotion.potion().isPresent() && target.matches(currentPotion.potion().get())) {
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
			return PotionUtilities.hasPotionEffects(stack);
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

	public Optional<PotionContentsComponent> getSelectedPotion(ItemStack bag) {
		PotionContentsComponent selectedPotion = bag.get(DataComponentTypes.POTION_CONTENTS);
		if (selectedPotion.hasEffects() && selectedPotion.potion().isPresent()) return Optional.empty();
		return Optional.of(selectedPotion);
	}

	public Optional<Integer> getSelectedPotionAmount(ItemStack bag) {
		int count = 0;
		Optional<PotionContentsComponent> potopt = getSelectedPotion(bag);
		if (potopt.isPresent()) {
			PotionContentsComponent target = potopt.get();
			BagInventory inv = new BagInventory(bag, null);
			for (int i = 0; i < inv.size(); i++) {
				PotionContentsComponent currentPotion = inv.getStack(i).get(DataComponentTypes.POTION_CONTENTS);
				if (currentPotion.potion().isPresent() && target.matches(currentPotion.potion().get())) count++;
			}
			return Optional.of(count);
		}
		return Optional.empty();
	}

	private SelectionMode getSelectionMode(ItemStack stack) {
		SelectionMode mode = SelectionMode.HOLD;
		if (stack.contains(ModComponents.SELECTION_MODE)) {
			mode = stack.getOrDefault(ModComponents.SELECTION_MODE, SelectionMode.HOLD);
		}
		return mode;
	}

	public static void toggleStatusForPlayer(PlayerEntity player, Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		if (stack.getItem() == ModItems.POTION_BAG) {
			SelectionMode currentMode = stack.get(ModComponents.SELECTION_MODE);
			stack.set(ModComponents.SELECTION_MODE, currentMode.next());
			player.getInventory().markDirty();
		} else {
			Log.w("Not holding a bag");
		}
	}

	public Optional<ItemStack> getFirstAvailablePotion(ItemStack stack) {
		BagInventory inv = new BagInventory(stack, null);
		for (int i = 0; i < inv.size(); i++) {
			ItemStack currentStack = inv.getStack(i);
			PotionContentsComponent currentPotion = currentStack.get(DataComponentTypes.POTION_CONTENTS);
			if (currentPotion.hasEffects() && currentPotion.potion().isPresent()) {
				return Optional.of(currentStack.copy());
			}
		}
		return Optional.empty();
	}
	
	public static int getColor(ItemStack stack, int index) {
		if (index > 0) return -1;
		return DyedColorComponent.getColor(stack, 0xce7720) | 0xFF000000;
	}

	public static enum SelectionMode {
		
		HOLD, NEXT, DESELECT;
		
		public static final Codec<SelectionMode> CODEC = CodecUtil.enumCodec(SelectionMode.class);
		
		public SelectionMode next() {
			
			SelectionMode[] modes = SelectionMode.values();
			int nextIndex = (this.ordinal() + 1) % modes.length;
			return modes[nextIndex];
			
		}
	}

	@Override
	public List<StatusEffectInstance> getContainedEffects(ItemStack stack) {
		return PotionUtilities.getEffects(stack);
	}

	@Override
	public boolean hasEffects(ItemStack stack) {
		return getSelectedPotion(stack).isPresent() && StatusEffectContainer.super.hasEffects(stack);
	}
	
}

