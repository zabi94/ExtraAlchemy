package zabi.minecraft.extraalchemy.items;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.UseAction;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import zabi.minecraft.extraalchemy.mixin.InvokerLivingEntity;

public class VialPotionItem extends PotionItem {

	public VialPotionItem() {
		super(new Settings().group(ItemSettings.EXTRA_ALCHEMY_GROUP).maxCount(16));
	}

	public ItemStack getDefaultStack() {
		return PotionUtil.setPotion(super.getDefaultStack(), Potions.WATER);
	}
	
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}
	
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity)user : null;
		if (playerEntity instanceof ServerPlayerEntity) {
			Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity)playerEntity, stack);
		}

		if (!world.isClient) {
			PotionUtil.getPotionEffects(stack).forEach(statusEffectInstance -> {
				if (statusEffectInstance.getEffectType().isInstant()) {
					statusEffectInstance.getEffectType().applyInstantEffect(playerEntity, playerEntity, user, statusEffectInstance.getAmplifier(), 1.0D);
				} else {
					user.addStatusEffect(new StatusEffectInstance(statusEffectInstance));
				}
			});
		} else {
			Random rand = new Random();
			((InvokerLivingEntity) (Object) user).spawnParticles(stack, 15);
			world.playSound(user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 0.8F, 1f+rand.nextFloat(), false);
		}
		if (playerEntity != null) {
			playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
			if (!playerEntity.abilities.creativeMode) {
				stack.decrement(1);
			}
		}
		return stack;
	}

	public int getMaxUseTime(ItemStack stack) {
		return 10;
	}

	public String getTranslationKey(ItemStack stack) {
		return PotionUtil.getPotion(stack).finishTranslationKey(this.getTranslationKey() + ".effect.");
	}
	
	@Override
	public Text getName(ItemStack stack) {
		return new TranslatableText(getTranslationKey(), new TranslatableText(PotionUtil.getPotion(stack).finishTranslationKey("item.minecraft.potion.effect.")));
	}

	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack stack,World world, List<Text> tooltip, TooltipContext context) {
		PotionUtil.buildTooltip(stack, tooltip, 1.0F);
	}

	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
		if (this.isIn(group)) {
			Iterator<Potion> iterator = Registry.POTION.iterator();
			while(iterator.hasNext()) {
				Potion potion = iterator.next();
				if (potion != Potions.EMPTY) {
					stacks.add(PotionUtil.setPotion(new ItemStack(this), potion));
				}
			}
		}

	}

}
