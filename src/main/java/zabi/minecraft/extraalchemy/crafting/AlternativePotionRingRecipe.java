package zabi.minecraft.extraalchemy.crafting;

import java.util.Collections;
import java.util.List;

import com.google.gson.JsonObject;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import zabi.minecraft.extraalchemy.config.ModConfig;
import zabi.minecraft.extraalchemy.items.ModItems;
import zabi.minecraft.extraalchemy.utils.Log;

public class AlternativePotionRingRecipe extends SpecialCraftingRecipe {

	private int cost;
	private int length;
	private int renew;
	private int level;
	private StatusEffect effect;

	public AlternativePotionRingRecipe(Identifier id, int cost, int length, int level, int renew, StatusEffect potion) {
		super(id);
		this.cost = cost;
		this.length = length;
		this.effect = potion;
		this.renew = renew;
		this.level = level;
	}

	@Override
	public boolean matches(CraftingInventory inv, World world) {
		if (!ModConfig.INSTANCE.enableRings) { //Globally disabled and specifically disabled
			return false;
		}
		
		boolean foundEffect = false;
		boolean foundRing = false;

		for (int i = 0; i < inv.size(); i++) {
			ItemStack is = inv.getStack(i); 
			Item s = is.getItem();
			if (s.equals(Items.POTION)) {
				if (foundEffect || !doesPotionMatch(PotionUtil.getPotionEffects(is))) {
					return false;
				} else {
					foundEffect = true;
				}
			} else if (s.equals(ModItems.EMPTY_RING)) {
				if (foundRing) {
					return false;
				} else {
					foundRing = true;
				}
			} else if (!s.equals(Items.AIR)) {
				return false;
			}
		}
		return foundRing && foundEffect;
	}

	@Override
	public ItemStack craft(CraftingInventory inv) {
		ItemStack result = new ItemStack(ModItems.POTION_RING);
		PotionUtil.setCustomPotionEffects(result, Collections.singleton(new StatusEffectInstance(effect)));
		result.getOrCreateNbt();
		result.getNbt().putInt("cost", cost);
		result.getNbt().putInt("length", length);
		result.getNbt().putInt("renew", renew);
		result.getNbt().putInt("level", level);
		result.getNbt().putBoolean("disabled", true);
		return result;
	}
	
	@Override
	public ItemStack getOutput() {
		return craft(null);
	}
	
	@Override
	public boolean fits(int width, int height) {
		return width > 1 || height > 1;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return CraftingRecipes.RING_CRAFTING_SERIALIZER;
	}
	
	private boolean doesPotionMatch(List<StatusEffectInstance> stack) {
		if (stack.size() != 1) {
			return false;
		}
		StatusEffectInstance stackInstance = stack.get(0);
		return stackInstance.getEffectType().equals(effect) && stackInstance.getAmplifier() == level;
	}
	
	public static class Serializer implements RecipeSerializer<AlternativePotionRingRecipe> {

		@Override
		public AlternativePotionRingRecipe read(Identifier id, JsonObject json) {
			int cost = json.get("cost").getAsInt();
			int length = json.get("length").getAsInt();
			int level = json.get("level").getAsInt();
			int renewTime = json.has("renew") ? json.get("renew").getAsInt() : 1;
			String effect_name = json.get("effect").getAsString();
			StatusEffect effect = Registry.STATUS_EFFECT.get(new Identifier(effect_name));
			if (effect.isInstant()) {
				Log.w("The ring recipe %s has an instant effect associated with %s, this functionality is meant for long lasting effects.", id, effect_name);
			}
			return new AlternativePotionRingRecipe(id, cost, length, level, renewTime, effect);
		}

		@Override
		public AlternativePotionRingRecipe read(Identifier id, PacketByteBuf buf) {
			int cost = buf.readInt();
			int length = buf.readInt();
			int renew = buf.readInt();
			int level = buf.readInt();
			String potion_name = buf.readString();
			StatusEffect pot = Registry.STATUS_EFFECT.get(new Identifier(potion_name));
			return new AlternativePotionRingRecipe(id, cost, length, level, renew, pot);
		}

		@Override
		public void write(PacketByteBuf buf, AlternativePotionRingRecipe recipe) {
			buf.writeInt(recipe.cost);
			buf.writeInt(recipe.length);
			buf.writeInt(recipe.renew);
			buf.writeInt(recipe.level);
			buf.writeString(Registry.STATUS_EFFECT.getId(recipe.effect).toString());
		}

	}

}
