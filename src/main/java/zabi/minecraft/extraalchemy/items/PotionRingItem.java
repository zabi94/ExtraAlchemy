package zabi.minecraft.extraalchemy.items;

import java.util.List;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.PotionUtil;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import zabi.minecraft.extraalchemy.ExtraAlchemy;
import zabi.minecraft.extraalchemy.config.ModConfig;
import zabi.minecraft.extraalchemy.utils.PlayerLevelUtil;

public class PotionRingItem extends Item {

	public PotionRingItem() {
		super(new Item.Settings().maxCount(1));
	}

	@Override
	public boolean hasGlint(ItemStack stack) {
		return !stack.getOrCreateNbt().getBoolean("disabled");
	}

	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		try {
			
			if (PotionUtil.getPotionEffects(stack).size() != 1) {
				tooltip.add(Text.literal("Error: rings must have exactly 1 effect attached!").formatted(Formatting.DARK_RED, Formatting.BOLD));
				return;
			}
			
			StatusEffectInstance sei = PotionUtil.getPotionEffects(stack).get(0);

			Text potionName = Text.translatable(sei.getTranslationKey()).formatted(Formatting.DARK_PURPLE);
			Text potionLevel = Text.translatable("potion.potency."+sei.getAmplifier()).formatted(Formatting.DARK_PURPLE);
			
			tooltip.add(Text.translatable("item.extraalchemy.potion_ring.potion", potionName, potionLevel));
			
			NbtCompound tag = stack.getOrCreateNbt();

			int cost = tag.getInt("cost");
			if (cost > 0) {
				tooltip.add(Text.translatable("item.extraalchemy.potion_ring.cost", Text.literal(""+cost).formatted(Formatting.GOLD)));
			} else {
				tooltip.add(Text.translatable("item.extraalchemy.potion_ring.creative").formatted(Formatting.LIGHT_PURPLE, Formatting.BOLD));
			}

			tooltip.add(Text.translatable("item.extraalchemy.potion_ring.length", Text.literal(""+tag.getInt("length")).formatted(Formatting.BLUE)));

			if (tag.getBoolean("disabled")) {
				tooltip.add(Text.translatable("item.extraalchemy.potion_ring.disabled").formatted(Formatting.GOLD));
			} else {
				tooltip.add(Text.translatable("item.extraalchemy.potion_ring.enabled").formatted(Formatting.GREEN));
			}
		} catch (Exception e) {
			tooltip.add(Text.literal("An error occurred when displaying the tooltip.").formatted(Formatting.RED));
			tooltip.add(Text.literal("Destroy this item ASAP to avoid crashes.").formatted(Formatting.RED, Formatting.BOLD));
			tooltip.add(Text.literal(e.getMessage()).formatted(Formatting.DARK_GRAY));
		}
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand); 
		if (user.isSneaking()) {
			toggleRingStack(stack);
			return new TypedActionResult<ItemStack>(ActionResult.SUCCESS, stack);
		}
		return new TypedActionResult<ItemStack>(ActionResult.FAIL, stack);
	}

	public static ItemStack toggleRingStack(ItemStack stack) {
		NbtCompound tag = stack.getOrCreateNbt();
		tag.putBoolean("disabled", !tag.getBoolean("disabled"));
		return stack;
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if (!ExtraAlchemy.areRingModsInstalled() || ModConfig.INSTANCE.allowRingsInInventoryWithThirdPartyMods) {
			onTick(stack, entity);
		}
	}
	
	public static void onTick(ItemStack stack, Entity entity) {
		if (!stack.getOrCreateNbt().getBoolean("disabled") && entity instanceof LivingEntity) {
			LivingEntity e = (LivingEntity) entity;
			for (StatusEffectInstance sei : PotionUtil.getPotionEffects(stack)) {
				StatusEffect statusEffect = sei.getEffectType();
				StatusEffectInstance onEntity = e.getStatusEffect(statusEffect);
				if (onEntity == null || onEntity.getDuration() <= stack.getNbt().getInt("renew")*20) {
					if (drainXP(e, stack.getNbt().getInt("cost"))) {
						int length = stack.getNbt().getInt("length");
						e.addStatusEffect(new StatusEffectInstance(statusEffect, length*20, sei.getAmplifier(), false, false, true));
					}
				}
			}
		}
	}
	

	private static boolean drainXP(LivingEntity e, int cost) {
		if (cost <= 0 || !(e instanceof PlayerEntity)) { //TODO check if effect is disabled
			return true;
		}

		PlayerEntity p = (PlayerEntity) e;
		if (p.isCreative()) return true;
		if (PlayerLevelUtil.getPlayerXP(p) < cost) {
			return false;
		}
		PlayerLevelUtil.addPlayerXP(p, -cost);

		return true;
	}

//	public static Predicate<Potion> ignoreLongVersions() {
//		Set<KeyProperty> seen = new HashSet<>();
//		return t -> seen.add(new KeyProperty(t.getEffects().get(0).getAmplifier(), t.getEffects().get(0).getEffectType()));
//	}
//
//	public static class KeyProperty extends Pair<Integer, StatusEffect> {
//		public KeyProperty(Integer left, StatusEffect right) {
//			super(left, right);
//		}
//
//		@Override
//		public int hashCode() {
//			final int prime = 31;
//			int result = 1;
//			result = prime * result + getLeft();
//			result = prime * result + ((getRight() == null) ? 0 : getRight().hashCode());
//			return result;
//		}
//
//		@Override
//		public boolean equals(Object obj) {
//			if (this == obj)
//				return true;
//			if (obj == null)
//				return false;
//			if (getClass() != obj.getClass())
//				return false;
//			KeyProperty other = (KeyProperty) obj;
//			if (getLeft() != other.getLeft())
//				return false;
//			if (getRight() == null) {
//				if (other.getRight() != null)
//					return false;
//			} else if (!getRight().equals(other.getRight()))
//				return false;
//			return true;
//		}
//
//
//
//	}

}
