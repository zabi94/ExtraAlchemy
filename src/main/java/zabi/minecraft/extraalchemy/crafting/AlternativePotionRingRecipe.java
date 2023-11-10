package zabi.minecraft.extraalchemy.crafting;

import java.util.Collections;
import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import zabi.minecraft.extraalchemy.config.ModConfig;
import zabi.minecraft.extraalchemy.items.ModItems;

public class AlternativePotionRingRecipe extends SpecialCraftingRecipe {

	private int cost;
	private int length;
	private int renew;
	private int level;
	private StatusEffect effect;

	public AlternativePotionRingRecipe(int cost, int length, int level, int renew, StatusEffect potion) {
		super(CraftingRecipeCategory.EQUIPMENT);
		this.cost = cost;
		this.length = length;
		this.effect = potion;
		this.renew = renew;
		this.level = level;
	}

	@Override
	public boolean matches(RecipeInputInventory inv, World world) {
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
	public ItemStack craft(RecipeInputInventory inv, DynamicRegistryManager var2) {
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
	public ItemStack getResult(DynamicRegistryManager registryManager) {
		return craft(null, registryManager);
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
		
		private final Codec<AlternativePotionRingRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.INT.fieldOf("cost").forGetter(a -> a.cost),
				Codec.INT.fieldOf("length").forGetter(a -> a.length),
				Codec.INT.fieldOf("level").forGetter(a -> a.level),
				Codec.INT.fieldOf("renew").forGetter(a -> a.renew),
				Registries.STATUS_EFFECT.getCodec().fieldOf("effect").forGetter(a -> a.effect)
		).apply(instance, AlternativePotionRingRecipe::new));
		
		@Override
		public AlternativePotionRingRecipe read(PacketByteBuf buf) {
			int cost = buf.readInt();
			int length = buf.readInt();
			int renew = buf.readInt();
			int level = buf.readInt();
			String potion_name = buf.readString();
			StatusEffect pot = Registries.STATUS_EFFECT.get(new Identifier(potion_name));
			return new AlternativePotionRingRecipe(cost, length, level, renew, pot);
		}

		@Override
		public void write(PacketByteBuf buf, AlternativePotionRingRecipe recipe) {
			buf.writeInt(recipe.cost);
			buf.writeInt(recipe.length);
			buf.writeInt(recipe.renew);
			buf.writeInt(recipe.level);
			buf.writeString(Registries.STATUS_EFFECT.getId(recipe.effect).toString());
		}

		@Override
		public Codec<AlternativePotionRingRecipe> codec() {
			return CODEC;
		}

	}

}
