package zabi.minecraft.extraalchemy.items;

import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import zabi.minecraft.extraalchemy.config.ModConfig;

public class EmptyVialItem extends Item {

	public EmptyVialItem() {
		super(new Settings().maxCount(16).group(ItemSettings.EXTRA_ALCHEMY_GROUP));
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (!ModConfig.INSTANCE.enableVials) {
			return new TypedActionResult<ItemStack>(ActionResult.FAIL, user.getStackInHand(hand));
		}
		if (!world.isClient) {
			for (int i = 0; i < PlayerInventory.getHotbarSize(); i++) {
				ItemStack potion = user.inventory.main.get(i);
				if (potion.getItem() == Items.SPLASH_POTION) {
					Potion main = PotionUtil.getPotion(potion);
					List<StatusEffectInstance> custom = PotionUtil.getCustomPotionEffects(potion);
					ItemStack vial = new ItemStack(ModItems.POTION_VIAL);
					PotionUtil.setPotion(vial, main);
					PotionUtil.setCustomPotionEffects(vial, custom);
					user.getStackInHand(hand).decrement(1);
					potion.decrement(1);
					if (!user.giveItemStack(vial)) {
						ItemEntity ei = new ItemEntity(world, user.getX(), user.getY(), user.getZ(), vial);
						ei.setPickupDelay(0);
						world.spawnEntity(ei);
					}
					return new TypedActionResult<ItemStack>(ActionResult.SUCCESS, user.getStackInHand(hand));
				}
			}
		}
		
		return new TypedActionResult<ItemStack>(ActionResult.SUCCESS, user.getStackInHand(hand));
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		if (ModConfig.INSTANCE.enableVials) {
			tooltip.add(new TranslatableText("item.extraalchemy.empty_vial.tootlip1"));
			tooltip.add(new TranslatableText("item.extraalchemy.empty_vial.tootlip2"));
		} else {
			tooltip.add(new TranslatableText("item.extraalchemy.empty_vial.tootlip.disabled"));
		}
	}

}
