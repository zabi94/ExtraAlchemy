package zabi.minecraft.extraalchemy.items;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.extraalchemy.ExtraAlchemy;
import zabi.minecraft.extraalchemy.lib.Config;
import zabi.minecraft.extraalchemy.lib.Reference;

public class ItemVial extends Item {

	
	public ItemVial() {
		this.setMaxStackSize(16);
		this.canRepair = false;
        if (Config.addSeparateTab.getBoolean()) this.setCreativeTab(ExtraAlchemy.TAB);
        else this.setCreativeTab(CreativeTabs.BREWING);
        this.setRegistryName(new ResourceLocation(Reference.MID, "vial_break"));
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand){
		ItemStack emptyVialStack = player.getHeldItem(hand);
		if (!world.isRemote) {
            for (int i=0;i<player.inventory.mainInventory.size();i++) {
            	ItemStack potionStack = player.inventory.getStackInSlot(i);
            	if (!potionStack.isEmpty() && potionStack.getItem().equals(Items.SPLASH_POTION) && potionStack.hasTagCompound() && potionStack.getTagCompound().hasKey("Potion")) {

            		ItemStack fullVialStack = new ItemStack(ModItems.breakable_potion);
            		PotionUtils.addPotionToItemStack(fullVialStack, PotionUtils.getPotionFromItem(potionStack));
            		EntityItem itemen = new EntityItem(world, player.posX, player.posY, player.posZ, fullVialStack);
            		
            		if (player == null || !player.capabilities.isCreativeMode) {
            			potionStack.setCount(potionStack.getCount()-1);
                    }
            		world.spawnEntity(itemen);
            		player.inventory.markDirty();
            		
            		if (player == null || !player.capabilities.isCreativeMode) {
                        emptyVialStack.setCount(emptyVialStack.getCount()-1);
                    }
            		break;
            	}
            }
        }
		
		if (world.isRemote) {
			world.playSound(player.posX, player.posY, player.posZ, SoundEvents.BLOCK_BREWING_STAND_BREW, SoundCategory.PLAYERS, 0.8F, 2f, false);
		}
		
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, emptyVialStack);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
        return I18n.format("item.emptyvial");
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(I18n.format("item.vial.rightclick"));
	}

}

