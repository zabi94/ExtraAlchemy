package zabi.minecraft.extraalchemy.recipes.crafting;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import zabi.minecraft.extraalchemy.items.ModItems;
import zabi.minecraft.extraalchemy.lib.Reference;
import zabi.minecraft.extraalchemy.potion.PotionReference;

public class StickyPotionRecipeHandler extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	public static List<String> potionBlacklist = Arrays.asList(new String[] {PotionReference.INSTANCE.CHEAT_DEATH_POTION.getName()});
	
	public StickyPotionRecipeHandler() {
		this.setRegistryName(Reference.MID, "sticky_potion");
	}

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		
		if (inv.getWidth()!=3 || inv.getHeight()!=3) return false;
		if (inv.getStackInSlot(4).isEmpty() || !inv.getStackInSlot(4).getItem().equals(Items.SLIME_BALL)) return false;
		PotionEffect firstEffectFound = null;
		Item firstItemFound = null;
		int counter = 0;
		for (int slot = 0; slot < 9; slot++) {
			if (slot!=4) {
				if (!inv.getStackInSlot(slot).isEmpty()) {
					final int eSlot = slot;
					if (inv.getStackInSlot(eSlot).getItem().equals(ModItems.modified_potion) || inv.getStackInSlot(eSlot).getItem().equals(Items.POTIONITEM) || inv.getStackInSlot(eSlot).getItem().equals(ModItems.breakable_potion)) {
						List<PotionEffect> stackEffects = PotionUtils.getEffectsFromStack(inv.getStackInSlot(eSlot));
						if (firstEffectFound==null) {
							if (stackEffects.get(stackEffects.size()-1).getPotion().isInstant()) return false;
							String name = stackEffects.get(stackEffects.size()-1).getPotion().getName();
							if (potionBlacklist.contains(name)) return false;
							firstEffectFound = stackEffects.get(stackEffects.size()-1);
							firstItemFound = inv.getStackInSlot(eSlot).getItem();
							counter++;
							continue;
						}
						if (firstEffectFound.equals(stackEffects.get(stackEffects.size()-1)) && firstItemFound.equals(inv.getStackInSlot(eSlot).getItem())) {
							counter++;
						} else return false;
					} else return false;
				}
			}
		}
		return counter>1;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		int counter = 0;
		PotionEffect firstEffectFound = null;
		PotionType firstTypeFound = null;
		Item firstItemFound = null;
		for (int i = 0; i < 9; i++) {
			if (i!=4 && !inv.getStackInSlot(i).isEmpty()) {
				counter++;
				if (firstEffectFound==null) {
					ItemStack istack = inv.getStackInSlot(i);
					List<PotionEffect> lista = PotionUtils.getEffectsFromStack(istack);
					firstEffectFound = lista.get(lista.size()-1);
					firstTypeFound = PotionUtils.getPotionFromItem(istack);
					firstItemFound = istack.getItem();
				}
			}
		}
		if (firstItemFound == Items.POTIONITEM) firstItemFound = ModItems.modified_potion;
		ItemStack s = new ItemStack(firstItemFound);
		PotionUtils.appendEffects(s, Collections.singleton(new PotionEffect(firstEffectFound.getPotion(), firstEffectFound.getDuration()*counter, firstEffectFound.getAmplifier(), firstEffectFound.getIsAmbient(), firstEffectFound.doesShowParticles())));
		PotionUtils.addPotionToItemStack(s, firstTypeFound);
		return s;
	}

	@Override
	public boolean canFit(int w, int h) {
		return w==3||h==3;
	}
	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}

}
