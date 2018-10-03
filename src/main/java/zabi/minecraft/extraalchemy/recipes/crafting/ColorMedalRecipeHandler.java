package zabi.minecraft.extraalchemy.recipes.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import zabi.minecraft.extraalchemy.items.ModItems;
import zabi.minecraft.extraalchemy.lib.Reference;

public class ColorMedalRecipeHandler extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe  {

	public ColorMedalRecipeHandler() {
		this.setRegistryName(Reference.MID, "medal_tint");
	}

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		int potionFound = 0, medalFound=0;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack is = inv.getStackInSlot(i);
			if (!is.isEmpty()) {
				if (is.getItem().equals(ModItems.supporter_medal)) medalFound++;
				else if (!PotionUtils.getEffectsFromStack(is).isEmpty()) potionFound++;
				else return false;
			}
		}
		return potionFound==1 && medalFound==1;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack medal = ItemStack.EMPTY,potion = ItemStack.EMPTY;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack is = inv.getStackInSlot(i);
			if (!is.isEmpty()) {
				if (is.getItem().equals(ModItems.supporter_medal)) {
					medal = is.copy();
				} else if (!PotionUtils.getEffectsFromStack(is).isEmpty()) {
					potion = is.copy();
				}
			}
		}
		PotionUtils.addPotionToItemStack(medal, PotionUtils.getPotionFromItem(potion));
		return medal;
	}


	@Override
	public boolean canFit(int width, int height) {
		return width>1||height>1;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(ModItems.supporter_medal);
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		NonNullList<ItemStack> remn = NonNullList.<ItemStack>withSize(inv.getSizeInventory(), ItemStack.EMPTY);
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack is = inv.getStackInSlot(i);
			if (!is.isEmpty()) {
				if (!PotionUtils.getEffectsFromStack(is).isEmpty() && !is.getItem().equals(ModItems.supporter_medal)) {
					remn.set(i, is.copy());
				}
			}
		}
		return remn;
	}
}