package zabi.minecraft.extraalchemy.items;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import zabi.minecraft.extraalchemy.ExtraAlchemy;
import zabi.minecraft.extraalchemy.client.tooltip.StatusEffectContainer;
import zabi.minecraft.extraalchemy.config.ModConfig;
import zabi.minecraft.extraalchemy.statuseffect.ToggleableEffect;

public class PotionRingItem extends Item implements StatusEffectContainer {
	
	public PotionRingItem() {
		super(new Item.Settings()
				.maxCount(1)
				.component(ModComponents.DISABLED, false)
		);
	}

	@Override
	public boolean hasGlint(ItemStack stack) {
		return !stack.getComponents().get(ModComponents.DISABLED);
	}
	
	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		super.appendTooltip(stack, context, tooltip, type);
		try {
			
			PotionRingData prd = stack.getOrDefault(ModComponents.POTION_RING_DATA, PotionRingData.EMPTY);
			
			if (prd.effect().isEmpty()) {
				tooltip.add(Text.literal("Error: rings must have an effect attached!").formatted(Formatting.DARK_RED, Formatting.BOLD));
				return;
			}
			
			StatusEffect effect = prd.effect().get();

			Text potionName = Text.translatable(effect.getTranslationKey()).formatted(Formatting.DARK_PURPLE);
			Text potionLevel = Text.translatable("potion.potency."+prd.level).formatted(Formatting.DARK_PURPLE);
			
			tooltip.add(Text.translatable("item.extraalchemy.potion_ring.potion", potionName, potionLevel));
			
			
			
			int cost = prd.cost;
			if (cost > 0) {
				tooltip.add(Text.translatable("item.extraalchemy.potion_ring.cost", Text.literal(""+cost).formatted(Formatting.GOLD)));
			} else {
				tooltip.add(Text.translatable("item.extraalchemy.potion_ring.creative").formatted(Formatting.LIGHT_PURPLE, Formatting.BOLD));
			}

			int length = prd.duration;
			
			tooltip.add(Text.translatable("item.extraalchemy.potion_ring.length", Text.literal(""+length).formatted(Formatting.BLUE)));

			boolean disabled = stack.getComponents().contains(ModComponents.DISABLED) && stack.getComponents().get(ModComponents.DISABLED);
			if (disabled) {
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
		boolean disabled = stack.getOrDefault(ModComponents.DISABLED, true);
		stack.set(ModComponents.DISABLED, !disabled);
		return stack;
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if (!ExtraAlchemy.areRingModsInstalled() || ModConfig.INSTANCE.allowRingsInInventoryWithThirdPartyMods) {
			onTick(stack, entity);
		}
	}
	
	public static void onTick(ItemStack stack, Entity entity) {
		boolean disabled = stack.getOrDefault(ModComponents.DISABLED, true);
		if (!disabled && entity instanceof LivingEntity) {
			LivingEntity e = (LivingEntity) entity;
			PotionRingData prd = stack.getOrDefault(ModComponents.POTION_RING_DATA, PotionRingData.EMPTY);
			if (prd.effect().isEmpty()) return;
			RegistryEntry<StatusEffect> entry = Registries.STATUS_EFFECT.getEntry(prd.effect().get());
			StatusEffectInstance onEntity = e.getStatusEffect(entry);
			int cost = prd.cost;
			if (onEntity == null || onEntity.getDuration() <= prd.renewalTime*20) {
				if (drainXP(e, cost, prd.effect().get())) {
					int length = prd.duration;
					e.addStatusEffect(new StatusEffectInstance(entry, length*20, prd.level(), false, false, true));
				}
			}
		}
	}
	
	private static boolean drainXP(LivingEntity e, int cost, StatusEffect effect) {
		if (cost <= 0) {
			return true;
		}
		
		if (effect instanceof ToggleableEffect te) {
			if (!te.isActive(e)) {
				return false;
			}
		}

		if (e instanceof PlayerEntity p) {
			if (p.isCreative()) {
				return true;
			}
			if (p.totalExperience < cost && p.experienceLevel == 0) {
				return false;
			}
			p.addExperience(-cost);
		}
		
		return true;
	}
	
	public static record PotionRingData(Optional<StatusEffect> effect, int level, int cost, int duration, int renewalTime) {
		
		public static final PotionRingData EMPTY = new PotionRingData(Optional.empty(), 0, 0, 0, 0);
		
		public static final Codec<PotionRingData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Registries.STATUS_EFFECT.getCodec().optionalFieldOf("effect").forGetter(PotionRingData::effect),
				Codec.INT.fieldOf("level").forGetter(PotionRingData::level),
				Codec.INT.fieldOf("cost").forGetter(PotionRingData::cost),
				Codec.INT.fieldOf("duration").forGetter(PotionRingData::duration),
				Codec.INT.fieldOf("renewalTime").forGetter(PotionRingData::renewalTime)
			).apply(instance, PotionRingData::new));
		
	}

	@Override
	public List<StatusEffectInstance> getContainedEffects(ItemStack stack) {
		PotionRingData prd = stack.getOrDefault(ModComponents.POTION_RING_DATA, PotionRingData.EMPTY);
		Optional<StatusEffect> eff = prd.effect();
		if (eff.isEmpty()) return Collections.emptyList();
		RegistryEntry<StatusEffect> rse = Registries.STATUS_EFFECT.getEntry(eff.get());
		return List.of(new StatusEffectInstance(rse, 0, prd.level()));
	}

}
