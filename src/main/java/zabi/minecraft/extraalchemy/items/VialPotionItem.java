package zabi.minecraft.extraalchemy.items;

import java.util.Random;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.Potion;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import zabi.minecraft.extraalchemy.mixin.access.InvokerLivingEntity;
import zabi.minecraft.extraalchemy.utils.PotionUtilities;

public class VialPotionItem extends PotionItem {

	public VialPotionItem() {
		super(new Settings().maxCount(16));
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
			PotionUtilities.getEffects(stack).stream()
			.forEach(statusEffectInstance -> {
				if (statusEffectInstance.getEffectType().value().isInstant()) {
					statusEffectInstance.getEffectType().value().applyInstantEffect(playerEntity, playerEntity, user, statusEffectInstance.getAmplifier(), 1.0D);
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
	
	@Override
	public Text getName(ItemStack stack) {
		PotionContentsComponent pcc = stack.get(DataComponentTypes.POTION_CONTENTS);
		if (pcc == null || pcc.potion().isEmpty()) {
			return Text.translatable(getTranslationKey(), Text.translatable(Items.POTION.getTranslationKey()));
		}
		return Text.translatable(getTranslationKey(), Text.translatable(Potion.finishTranslationKey(pcc.potion(), "item.minecraft.potion.effect.")));
	}

}
