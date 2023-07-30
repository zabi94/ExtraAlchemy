package zabi.minecraft.extraalchemy.crafting;

import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import zabi.minecraft.extraalchemy.config.ModConfig;
import zabi.minecraft.extraalchemy.items.ModItems;

public class PotionVialRecipe extends SpecialCraftingRecipe {
	
	public PotionVialRecipe(Identifier id, CraftingRecipeCategory category) {
		super(id, CraftingRecipeCategory.MISC);
	}
	
	@Override
	public boolean matches(RecipeInputInventory inv, World world) {
		if (!ModConfig.INSTANCE.enableVials) {
			return false;
		}
		
		boolean splash = false;
		boolean vial = false;
		
		for (int i = 0; i < inv.size(); i++) {
			Item s = inv.getStack(i).getItem();
			if (s.equals(Items.SPLASH_POTION)) {
				if (splash) {
					return false;
				} else {
					splash = true;
				}
			} else if (s.equals(ModItems.EMPTY_VIAL)) {
				if (vial) {
					return false;
				} else {
					vial = true;
				}
			} else if (!s.equals(Items.AIR)) {
				return false;
			}
		}
		return vial && splash;
	}

	@Override
	public ItemStack craft(RecipeInputInventory inv, DynamicRegistryManager regMan) {
		for (int i = 0; i < inv.size(); i++) {
			ItemStack is = inv.getStack(i);
			if (is.getItem().equals(Items.SPLASH_POTION)) {
				return PotionUtil.setCustomPotionEffects(PotionUtil.setPotion(new ItemStack(ModItems.POTION_VIAL), PotionUtil.getPotion(is)), PotionUtil.getCustomPotionEffects(is));
			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public boolean fits(int width, int height) {
		return width > 1 || height > 1;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return CraftingRecipes.FILL_VIAL_SERIALIZER;
	}
	
}
