package zabi.minecraft.extraalchemy.items;

import java.util.List;
import java.util.Random;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import zabi.minecraft.extraalchemy.mixin.access.InvokerLivingEntity;

public class VialPotionItem extends PotionItem {

	public VialPotionItem() {
		super(new Settings().maxCount(16));
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
			((InvokerLivingEntity) (Object) user).extraalchemy_spawnParticles(stack, 15);
			world.playSound(user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 0.8F, 1f+rand.nextFloat(), false);
		}
		if (playerEntity != null) {
			playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
			if (!playerEntity.getAbilities().creativeMode) {
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
		return Text.translatable(getTranslationKey(), Text.translatable(PotionUtil.getPotion(stack).finishTranslationKey("item.minecraft.potion.effect.")));
	}

	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		PotionUtil.buildTooltip(stack, tooltip, 1.0F, 20);
	}

}
