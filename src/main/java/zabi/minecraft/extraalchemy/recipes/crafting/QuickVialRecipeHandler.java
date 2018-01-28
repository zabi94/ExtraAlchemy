package zabi.minecraft.extraalchemy.recipes.crafting;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import zabi.minecraft.extraalchemy.items.ModItems;
import zabi.minecraft.extraalchemy.lib.Reference;

public class QuickVialRecipeHandler extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	private ItemStack dummyResult = null;
	
	public QuickVialRecipeHandler() {
		this.setRegistryName(Reference.MID, "vial_potion");
		dummyResult = new ItemStack(ModItems.vial_break);
	}
	
	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		boolean foundEmptyVial = false, foundPotion=false;
		
		for (int i = 0;i < inv.getSizeInventory(); i++) {
			ItemStack s = inv.getStackInSlot(i);
			if (!s.isEmpty()) {
				if (s.getItem().equals(Items.SPLASH_POTION) && s.hasTagCompound() && s.getTagCompound().hasKey("Potion")) {
					if (foundPotion) return false;
					else foundPotion=true;
				} else if (s.getItem().equals(ModItems.vial_break)) {
					if (foundEmptyVial) return false;
					else foundEmptyVial=true;
				} else return false;
			}
		}
		
		return foundEmptyVial && foundPotion;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		
		ItemStack is = new ItemStack(ModItems.breakable_potion);
		is.setTagCompound(new NBTTagCompound());
		for (int i = 0;i < inv.getSizeInventory(); i++) {
			ItemStack s = inv.getStackInSlot(i);
			if (!s.isEmpty()) {
				if (s.getItem().equals(Items.SPLASH_POTION)) {
					is.getTagCompound().setString("Potion", s.getTagCompound().getString("Potion"));
					PotionUtils.appendEffects(is, PotionUtils.getPotionFromItem(s).getEffects());
					return is;
				}
			}
		}
		return is;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width>1||height>1;
	}
	
	@Override
	public ItemStack getRecipeOutput() {
		return dummyResult;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		return NonNullList.<ItemStack>withSize(inv.getSizeInventory(), ItemStack.EMPTY);
	}

}
