package zabi.minecraft.extraalchemy.items;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.extraalchemy.ExtraAlchemy;
import zabi.minecraft.extraalchemy.gui.GuiHandler;
import zabi.minecraft.extraalchemy.lib.Config;
import zabi.minecraft.extraalchemy.lib.Reference;

public class ItemPotionBag extends Item {
	
    
	public ItemPotionBag() {
		this.setMaxStackSize(1);
	    if (Config.addSeparateTab.getBoolean()) this.setCreativeTab(ExtraAlchemy.TAB);
	    else this.setCreativeTab(CreativeTabs.BREWING);
	    this.setRegistryName(new ResourceLocation(Reference.MID, "potion_bag"));
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		int ch = getCharges(stack);
		if (ch>0) {
			tooltip.add(String.format(I18n.translateToLocal("item.potion_bag.charges"), ch));
			if (GuiScreen.isShiftKeyDown()) {
				tooltip.add("");
				ItemStack deleg = getPotionBottle(stack);
				PotionUtils.addPotionTooltip(deleg, tooltip, 1); 
			}
		}
    }
	
	private ItemStack getPotionBottle(ItemStack stack) {
		return new ItemStack(stack.getTagCompound().getCompoundTag("inventory").getCompoundTag("inv0"));
	}

	@Override
	 public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand) {
		ItemStack is = player.getHeldItem(hand);
		initTag(is);
		boolean flag = player.isSneaking();
		if (getCharges(player.getHeldItem(hand))>0 && !flag) {
			player.setActiveHand(hand);
		} else if (flag) {
			player.openGui(ExtraAlchemy.instance, GuiHandler.IDs.GUI_POTION_BAG.ordinal(), player.getEntityWorld(), (int) player.posX, (int) player.posY, (int) player.posZ);
		} else {
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, player.getHeldItem(hand));
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	private void initTag(ItemStack stack) {
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("inventory")) return;
		NBTTagCompound tag_e;
		if (stack.hasTagCompound()) {
			tag_e = stack.getTagCompound();
		} else {
			tag_e = new NBTTagCompound();
		}
		NBTTagCompound tag = new NBTTagCompound();
		for (int i=0; i<19;i++) {
			NBTTagCompound nw = new NBTTagCompound();
			ItemStack.EMPTY.writeToNBT(nw);
			tag.setTag("inv"+i, nw);
		}
		tag_e.setTag("inventory", tag);
		stack.setTagCompound(tag_e);
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		PotionType pt = PotionUtils.getPotionFromItem(getPotionBottle(stack));
		NBTTagCompound inv = stack.getTagCompound().getCompoundTag("inventory");
		for (int i=1;i<19;i++) {
			ItemStack pot = new ItemStack(inv.getCompoundTag("inv"+i));
			if (!pot.isEmpty() && PotionUtils.getPotionFromItem(pot).equals(pt)) {
				for (PotionEffect pe:pt.getEffects()) entityLiving.addPotionEffect(new PotionEffect(pe));
				ItemStack.EMPTY.writeToNBT(inv.getCompoundTag("inv"+i));
				return stack;
			}
		}
		return stack;
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return (int) (Items.POTIONITEM.getMaxItemUseDuration(stack)*0.9);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocal("item.potion_bag");
    }
	
	private int getCharges(ItemStack stack) {
		initTag(stack);
		NBTTagCompound inv = stack.getTagCompound().getCompoundTag("inventory");
		ItemStack selected = new ItemStack(inv.getCompoundTag("inv0"));
		if (selected.isEmpty()) return 0;
		
		PotionType selectedPT = PotionUtils.getPotionFromItem(selected); 
		
		int count = 0;
		for (int i=1;i<19;i++) {
			ItemStack potslot = new ItemStack(inv.getCompoundTag("inv"+i));
			if (!potslot.isEmpty() && PotionUtils.getPotionFromItem(potslot).equals(selectedPT)) {
				count += potslot.getCount();
			}
		}
		return count;
	}
	
	
	
}
