package zabi.minecraft.extraalchemy.recipes.crafting;

import java.util.Collections;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.IForgeRegistryEntry;
import zabi.minecraft.extraalchemy.items.ModItems;
import zabi.minecraft.extraalchemy.lib.Reference;

public class SplitPotionRecipeHandler extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe	 {

	public SplitPotionRecipeHandler() {
		this.setRegistryName(Reference.MID, "split_potion");
	}
	
	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		if (inv.getWidth()!=3 || inv.getHeight()!=3) return false;
		Item splittingGlassRecipient = null;
		if (inv.getStackInSlot(4).getItem()==ModItems.modified_potion || inv.getStackInSlot(4).getItem()==Items.POTIONITEM) splittingGlassRecipient = Items.GLASS_BOTTLE;
		if (inv.getStackInSlot(4).getItem()==Items.SPLASH_POTION || inv.getStackInSlot(4).getItem()==ModItems.breakable_potion) splittingGlassRecipient = ModItems.vial_break;
		if (splittingGlassRecipient==null) return false;
		int counter = 0;
		for (int i=0;i<9;i++) if (i!=4) {
			if (!inv.getStackInSlot(i).isEmpty()) {
				if (!inv.getStackInSlot(i).getItem().equals(splittingGlassRecipient)) return false;
				if (inv.getStackInSlot(i).getCount()!=1) return false;
				counter++;
			}
		}
		return counter>1;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		int counter = 0;
		ItemStack res=ItemStack.EMPTY;
		if (inv.getStackInSlot(4).getItem()==Items.SPLASH_POTION) res = new ItemStack(Items.GLASS_BOTTLE);
		for (int i=0;i<9;i++) if (i!=4) {
			if (!inv.getStackInSlot(i).isEmpty()) {
				if (res.isEmpty()) res = inv.getStackInSlot(i).copy();
				counter++;
			}
		}
		res.setCount(1);
		PotionEffect firstEffectFound = PotionUtils.getEffectsFromStack(inv.getStackInSlot(4)).get(0);
		int newDuration = firstEffectFound.getDuration()/counter;
		if (newDuration<100) return ItemStack.EMPTY;
		if (FMLCommonHandler.instance().getEffectiveSide().equals(Side.CLIENT)) {
			res.setTagCompound(new NBTTagCompound());
			res.getTagCompound().setInteger("splitresult", newDuration/20);
		}
		return res;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean canFit(int w, int h) {
		return w==3||h==3;
	}
	
	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		int counter = 0;
		Item resItem = inv.getStackInSlot(4).getItem();
		if (resItem==Items.POTIONITEM) resItem = ModItems.modified_potion;
		if (resItem==Items.SPLASH_POTION) resItem = ModItems.breakable_potion;
		for (int i=0;i<9;i++) if (i!=4) {
			if (!inv.getStackInSlot(i).isEmpty()) {
				counter++;
			}
		}
		ItemStack s = new ItemStack(resItem);
		List<PotionEffect> list = PotionUtils.getEffectsFromStack(inv.getStackInSlot(4));
		PotionEffect firstEffectFound = list.get(list.size()-1);
		int newDuration = firstEffectFound.getDuration()/counter;
		if (newDuration<5) return NonNullList.<ItemStack>create();
		PotionUtils.appendEffects(s, Collections.singleton(new PotionEffect(firstEffectFound.getPotion(), newDuration, firstEffectFound.getAmplifier(), firstEffectFound.getIsAmbient(), firstEffectFound.doesShowParticles())));
		PotionUtils.addPotionToItemStack(s, PotionUtils.getPotionFromItem(inv.getStackInSlot(4)));
		NonNullList<ItemStack> isl = NonNullList.<ItemStack>withSize(9, ItemStack.EMPTY);
		for (int j=0;j<9;j++) {
			if (j!=4 && !inv.getStackInSlot(j).isEmpty()) {
				isl.set(j, s.copy());
			} 
		}
		
		return isl;
	}
	
}
