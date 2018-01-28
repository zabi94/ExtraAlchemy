package zabi.minecraft.extraalchemy.recipes.crafting;

import java.util.Collections;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemPotion;
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
import zabi.minecraft.extraalchemy.lib.Reference;

public class SplitPotionRecipeHandler extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe	 {

	public SplitPotionRecipeHandler() {
		this.setRegistryName(Reference.MID, "split_potion");
	}
	
	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		if (inv.getWidth()!=3 || inv.getHeight()!=3) return false;
		if (inv.getStackInSlot(4).isEmpty() || inv.getStackInSlot(4).getItem().equals(Items.TIPPED_ARROW) || PotionUtils.getEffectsFromStack(inv.getStackInSlot(4)).isEmpty()) return false;
		int counter = 0;
		for (int i=0;i<9;i++) if (i!=4) {
			if (!inv.getStackInSlot(i).isEmpty()) {
				if (!inv.getStackInSlot(i).getItem().equals(Items.GLASS_BOTTLE) && (inv.getStackInSlot(4).getItem() instanceof ItemPotion)) return false;
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
		for (int i=0;i<9;i++) if (i!=4) {
			if (!inv.getStackInSlot(i).isEmpty()) counter++;
		}
		ItemStack s = new ItemStack(inv.getStackInSlot(4).getItem());
		s.setStackDisplayName(inv.getStackInSlot(4).getDisplayName());
		PotionEffect firstEffectFound = PotionUtils.getEffectsFromStack(inv.getStackInSlot(4)).get(0);
		int newDuration = firstEffectFound.getDuration()/counter;
		if (newDuration<5) return NonNullList.<ItemStack>create();
		PotionUtils.appendEffects(s, Collections.singleton(new PotionEffect(firstEffectFound.getPotion(), newDuration, firstEffectFound.getAmplifier(), firstEffectFound.getIsAmbient(), firstEffectFound.doesShowParticles())));
		s.getTagCompound().setBoolean("alteredPotion", true);
		NonNullList<ItemStack> isl = NonNullList.<ItemStack>withSize(9, ItemStack.EMPTY);
		for (int j=0;j<9;j++) {
			if (j!=4 && !inv.getStackInSlot(j).isEmpty()) {
				isl.set(j, s.copy());
			} 
		}
		
		return isl;
	}
	
}
