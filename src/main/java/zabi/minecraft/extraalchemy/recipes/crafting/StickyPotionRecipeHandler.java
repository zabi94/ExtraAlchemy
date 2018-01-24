package zabi.minecraft.extraalchemy.recipes.crafting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import zabi.minecraft.extraalchemy.lib.Reference;
import zabi.minecraft.extraalchemy.potion.PotionReference;

public class StickyPotionRecipeHandler extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	public static ArrayList<Item> validItems = new ArrayList<Item>();
	public static List<String> potionBlacklist = Arrays.asList(new String[] {PotionReference.INSTANCE.CHEAT_DEATH_POTION.getName()});
	static {
		validItems.add(Items.POTIONITEM);
		validItems.add(Items.SPLASH_POTION);
		validItems.add(Items.LINGERING_POTION);
		validItems.add(Items.TIPPED_ARROW);
	}
	
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
					if (validItems.contains(inv.getStackInSlot(eSlot).getItem())) {
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
		Item firstItemFound = null;
		String displayName = null;
		for (int i = 0; i < 9; i++) {
			if (i!=4 && !inv.getStackInSlot(i).isEmpty()) {
				counter++;
				if (firstEffectFound==null) {
					List<PotionEffect> lista = PotionUtils.getEffectsFromStack(inv.getStackInSlot(i));
					displayName = inv.getStackInSlot(i).getDisplayName();
					firstEffectFound = lista.get(lista.size()-1);
					firstItemFound = inv.getStackInSlot(i).getItem();
				}
			}
		}
		ItemStack s = new ItemStack(firstItemFound);
		s.setStackDisplayName(displayName);
		PotionUtils.appendEffects(s, Collections.singleton(new PotionEffect(firstEffectFound.getPotion(), firstEffectFound.getDuration()*counter, firstEffectFound.getAmplifier(), firstEffectFound.getIsAmbient(), firstEffectFound.doesShowParticles())));
		s.getTagCompound().setBoolean("alteredPotion", true);
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
