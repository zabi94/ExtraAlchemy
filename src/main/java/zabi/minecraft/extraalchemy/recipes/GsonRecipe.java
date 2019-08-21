package zabi.minecraft.extraalchemy.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import zabi.minecraft.extraalchemy.lib.Log;
import zabi.minecraft.extraalchemy.potion.PotionTypeBase;

public class GsonRecipe {

	public int level = 0;
	public int durationSeconds = 0;
	public String ingredient = "";
	public String basePotionType = "minecraft:awkward";
	public String effect = "";
	public String name = "";
	public boolean autoGenerateStrongVersion = false;
	public boolean autoGenerateLongVersion = false;
	public int durationLongVersion = 0;
	public int durationStrongVersion = 0;
	
	public boolean check() {
		if (name.trim().length() == 0) {
			Log.e("Potion custom name is empty");
			return false;
		}
		if (effect.trim().length() == 0) {
			Log.e("Potion effect is empty");
			return false;
		}
		if (basePotionType.trim().length() == 0) {
			Log.e("Potion base type is empty");
			return false;
		}
		if (ingredient.trim().length() == 0) {
			Log.e("Potion ingredient is empty");
			return false;
		}
		String ingredientItem = ingredient.split("@")[0];
		if (!ForgeRegistries.POTIONS.containsKey(r(effect))) {
			Log.e("Potion effect doesn't exist: "+effect);
			return false;
		}
		if (!ForgeRegistries.POTION_TYPES.containsKey(r(basePotionType))) {
			Log.e("Potion base type doesn't exist: "+basePotionType);
			return false;
		}
		if (!ForgeRegistries.ITEMS.containsKey(r(ingredientItem))) {
			Log.e("Potion ingredient doesn't exist: "+ingredientItem);
			return false;
		}
		
		Potion p = ForgeRegistries.POTIONS.getValue(r(effect));
		if (!(p.isInstant() == (durationSeconds == 0)) && (p.isInstant() == !autoGenerateLongVersion) && (p.isInstant() == (durationLongVersion == 0)) && (p.isInstant() == (durationStrongVersion == 0))) {
			Log.e("An instant potion cannot have non-zero durations. Non instant potions must specify a non-zero duration");
			return false;
		}
		return true;
	}
	
	private static ResourceLocation r(String s) {
		return new ResourceLocation(s);
	}
	
	public ItemStack getIngredient() {
		String[] parts = ingredient.split("@");
		return new ItemStack(ForgeRegistries.ITEMS.getValue(r(parts[0])), 1, parts.length>1?Integer.parseInt(parts[1]):OreDictionary.WILDCARD_VALUE);
	}
	
	private Potion getEffect() {
		return ForgeRegistries.POTIONS.getValue(r(effect));
	}
	
	public PotionType getBase() {
		return ForgeRegistries.POTION_TYPES.getValue(r(basePotionType));
	}
	
	public PotionTypeBase generateType() {
		return new PotionTypeBase(getEffect(), durationSeconds*20, level, name);
	}
	
	public PotionTypeBase generateTypeLong() {
		return new PotionTypeBase(getEffect(), durationLongVersion*20, level, name+"_long");
	}
	
	public PotionTypeBase generateTypeStrong() {
		return new PotionTypeBase(getEffect(), durationStrongVersion*20, level+1, name+"_strong");
	}
}
