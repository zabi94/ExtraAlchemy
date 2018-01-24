package zabi.minecraft.extraalchemy.items;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.extraalchemy.ExtraAlchemy;
import zabi.minecraft.extraalchemy.lib.Config;
import zabi.minecraft.extraalchemy.lib.Reference;

public class ItemBreakablePotion extends ItemPotion {
	
	protected ItemBreakablePotion() {
        this.setMaxStackSize(16);
        if (Config.addSeparateTab.getBoolean()) this.setCreativeTab(ExtraAlchemy.TAB);
        else this.setCreativeTab(CreativeTabs.BREWING);
        this.setRegistryName( new ResourceLocation(Reference.MID, "breakable_potion"));
	}
	
	public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocal(PotionUtils.getPotionFromItem(stack).getNamePrefixed("potion.effect."))+" "+I18n.translateToLocal("item.breakable");
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        PotionUtils.addPotionTooltip(stack, tooltip, 1.0F);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
        if (this.isInCreativeTab(tab)) for (PotionType potiontype : PotionType.REGISTRY) {
        	list.add(PotionUtils.addPotionToItemStack(new ItemStack(this), potiontype));
        }
    }
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand){
		if (!world.isRemote) {
            for (PotionEffect potioneffect : PotionUtils.getEffectsFromStack(player.getHeldItem(hand))) {
            	player.addPotionEffect(new PotionEffect(potioneffect));
            }
        } else {
        	Random rand = new Random();
        	player.swingArm(hand);
        	player.renderBrokenItemStack(player.getHeldItem(hand));
        	world.playSound(player.posX, player.posY, player.posZ, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 0.8F, 1f+rand.nextFloat(), false);
        }
		if (player != null && !player.capabilities.isCreativeMode) {
			player.getHeldItem(hand).setCount(player.getHeldItem(hand).getCount()-1);
        }
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
	
}
