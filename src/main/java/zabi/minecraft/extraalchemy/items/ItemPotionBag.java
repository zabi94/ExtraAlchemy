package zabi.minecraft.extraalchemy.items;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ItemStackHelper;
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
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.extraalchemy.ExtraAlchemy;
import zabi.minecraft.extraalchemy.ModConfig;
import zabi.minecraft.extraalchemy.gui.GuiHandler;
import zabi.minecraft.extraalchemy.inventory.PotionBagInventory;
import zabi.minecraft.extraalchemy.lib.Log;
import zabi.minecraft.extraalchemy.lib.Reference;

public class ItemPotionBag extends Item {
	
    
	public ItemPotionBag() {
		this.setMaxStackSize(1);
	    if (ModConfig.options.addSeparateTab) this.setCreativeTab(ExtraAlchemy.TAB);
	    else this.setCreativeTab(CreativeTabs.BREWING);
	    this.setRegistryName(new ResourceLocation(Reference.MID, "potion_bag"));
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		PotionBagInventory inv = getInventory(stack, false);
		if (inv==null) {
			tooltip.add("Old format detected, please shift right click at least once to convert");
			return;
		}
		int ch = getCharges(inv, false);
		if (ch>0) {
			tooltip.add(I18n.format("item.potion_bag.charges", ch));
			tooltip.add("");
			PotionUtils.addPotionTooltip(inv.getStackInSlot(0), tooltip, 1); 
		}
    }

	@Override
	 public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand) {
		if (!player.world.isRemote) {
			PotionBagInventory inv = getInventory(player.getHeldItem(hand), true); //Needs to be here to refresh the inventory
			if (player.isSneaking()) {
				player.openGui(ExtraAlchemy.instance, GuiHandler.IDs.GUI_POTION_BAG.ordinal(), player.getEntityWorld(), (int) player.posX, (int) player.posY, (int) player.posZ);
			} else if (getCharges(inv, true)>0) {
				player.setActiveHand(hand);
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	public PotionBagInventory getInventory(ItemStack stack, boolean refresh) {
		
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
			NBTTagCompound newTag = new NBTTagCompound();
			NonNullList<ItemStack> list = NonNullList.withSize(19, ItemStack.EMPTY);
			ItemStackHelper.saveAllItems(newTag, list, true);
			stack.getTagCompound().setTag("new_inv", newTag);
		}
		
		if (stack.getTagCompound().hasKey("inventory")) {
			if (refresh) {
				Log.d("Converting bag inventory to new format. Current tag:\n"+stack.getTagCompound());
				NBTTagCompound old = stack.getSubCompound("inventory");
				NBTTagCompound newTag = new NBTTagCompound();
				NonNullList<ItemStack> list = NonNullList.withSize(19, ItemStack.EMPTY);
				for (int i=0; i<19;i++) {
					ItemStack is = new ItemStack(old.getCompoundTag("inv"+i));
					Log.d("Found potion, "+is);
					list.set(i, is);
				}
				ItemStackHelper.saveAllItems(newTag, list, true);
				stack.getTagCompound().setTag("new_inv", newTag);
				stack.getTagCompound().removeTag("inventory");
			} else {
				return null;//Old format detected in client
			}
		}
		
		return new PotionBagInventory(stack);
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		entityLiving.stopActiveHand();
		PotionBagInventory inventory = getInventory(stack, true);
		PotionType pt = PotionUtils.getPotionFromItem(inventory.getStackInSlot(0));
		for (int i=1;i<19;i++) {
			ItemStack pot = inventory.getStackInSlot(i);
			if (!pot.isEmpty() && PotionUtils.getPotionFromItem(pot).equals(pt)) {
				for (PotionEffect pe:pt.getEffects()) {
					if (pe.getPotion().isInstant()) {
						pe.getPotion().affectEntity(null, null, entityLiving, pe.getAmplifier(), 1.0D);
					} else {
						entityLiving.addPotionEffect(new PotionEffect(pe));
					}
				}
				inventory.setStackInSlot(i, ItemStack.EMPTY);
				getCharges(inventory, true); //Updates potion if empty
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
	@SideOnly(Side.CLIENT)
	public String getItemStackDisplayName(ItemStack stack) {
        return I18n.format("item.potion_bag");
    }
	
	private int getCharges(PotionBagInventory inventory, boolean update) {
		ItemStack selected = inventory.getStackInSlot(0);
		if (selected.isEmpty()) {
			if (update) return selectNextAndGetCharges(inventory);
			return 0;
		}
		
		PotionType selectedPT = PotionUtils.getPotionFromItem(selected); 
		
		int count = 0;
		for (int i=1;i<19;i++) {
			ItemStack potslot = inventory.getStackInSlot(i);
			if (!potslot.isEmpty() && PotionUtils.getPotionFromItem(potslot).equals(selectedPT)) {
				count += potslot.getCount();
			}
		}
		if (count==0 && update) return selectNextAndGetCharges(inventory);
		return count;
	}

	private int selectNextAndGetCharges(PotionBagInventory inventory) {
		for (int i=1;i<19;i++) {
			ItemStack potslot = inventory.getStackInSlot(i);
			if (!potslot.isEmpty()) {
				inventory.setStackInSlot(0, potslot.copy());
				return getCharges(inventory, false);
			}
		}
		return 0;
	}
	
	
	
}
