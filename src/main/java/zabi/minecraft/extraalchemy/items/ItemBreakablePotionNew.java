package zabi.minecraft.extraalchemy.items;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemBreakablePotionNew extends ItemBreakablePotion {
	
	public ItemBreakablePotionNew() {
		super();
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand){
		player.setActiveHand(hand);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entityLiving) {
		if (!(entityLiving instanceof EntityPlayer)) return stack;
		ItemStack fxs = stack.copy();
		EntityPlayer player = (EntityPlayer) entityLiving;
		if (!world.isRemote) {
			ArrayList<PotionEffect> list = new ArrayList<PotionEffect>();
			PotionUtils.addCustomPotionEffectToList(stack.getTagCompound(), list);
            for (PotionEffect potioneffect : list) {
            	 if (potioneffect.getPotion().isInstant()) {
                     potioneffect.getPotion().affectEntity(null, null, entityLiving, potioneffect.getAmplifier(), 1.0D);
                 } else {
                     entityLiving.addPotionEffect(new PotionEffect(potioneffect));
                 }
            }
        } else {
        	Random rand = new Random();
        	player.renderBrokenItemStack(fxs);
        	world.playSound(player.posX, player.posY, player.posZ, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 0.8F, 1f+rand.nextFloat(), false);
        }
		if (!player.capabilities.isCreativeMode) {
			stack.setCount(stack.getCount()-1);
        }
		return stack;
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 10;
	}
}
