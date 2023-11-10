package zabi.minecraft.extraalchemy.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.potion.Potion;
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

public class PotionRingRecipe extends SpecialCraftingRecipe {

	private int cost;
	private int length;
	private int renew;
	private Potion potion;

	public PotionRingRecipe(int cost, int length, int renew, Potion potion) {
		super(CraftingRecipeCategory.EQUIPMENT);
		this.cost = cost;
		this.length = length;
		this.potion = potion;
		this.renew = renew;
	}

	@Override
	public boolean matches(RecipeInputInventory inv, World world) {
		if (!ModConfig.INSTANCE.enableRings || potion.getEffects().size() != 1) { //Globally disabled and specifically disabled
			return false;
		}
		
		if (potion.getEffects().size() == 0) return false;

		boolean foundPotion = false;
		boolean foundRing = false;

		for (int i = 0; i < inv.size(); i++) {
			ItemStack is = inv.getStack(i); 
			Item s = is.getItem();
			if (s.equals(Items.POTION)) {
				if (foundPotion || !doesPotionMatch(PotionUtil.getPotion(is))) {
					return false;
				} else {
					foundPotion = true;
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
		return foundRing && foundPotion;
	}

	@Override
	public ItemStack craft(RecipeInputInventory inv, DynamicRegistryManager var2) {
		ItemStack result = new ItemStack(ModItems.POTION_RING);
		PotionUtil.setPotion(result, potion);
		result.getOrCreateNbt();
		result.getNbt().putInt("cost", cost);
		result.getNbt().putInt("length", length);
		result.getNbt().putInt("renew", renew);
		result.getNbt().putBoolean("disabled", true);
		return result;
	}
	
	@Override
	public ItemStack getResult(DynamicRegistryManager regMan) {
		return craft(null, regMan);
	}
	
	@Override
	public boolean fits(int width, int height) {
		return width > 1 || height > 1;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return CraftingRecipes.RING_CRAFTING_SERIALIZER;
	}
	
	private boolean doesPotionMatch(Potion stack) {
		if (stack.getEffects().size() != 1) {
			return false;
		}
		StatusEffectInstance stackInstance = stack.getEffects().get(0);
		StatusEffectInstance recipeInstance = potion.getEffects().get(0);
		return stackInstance.getEffectType().equals(recipeInstance.getEffectType()) && stackInstance.getAmplifier() == recipeInstance.getAmplifier();
	}
	
	public static class Serializer implements RecipeSerializer<PotionRingRecipe> {

		private final Codec<PotionRingRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.INT.fieldOf("cost").forGetter(a -> a.cost),
				Codec.INT.fieldOf("length").forGetter(a -> a.length),
				Codec.INT.fieldOf("renew").forGetter(a -> a.renew),
				Registries.POTION.getCodec().fieldOf("potion").forGetter(a -> a.potion)
		).apply(instance, PotionRingRecipe::new));
		
		@Override
		public PotionRingRecipe read(PacketByteBuf buf) {
			int cost = buf.readInt();
			int length = buf.readInt();
			int renew = buf.readInt();
			String potion_name = buf.readString();
			Potion pot = Registries.POTION.get(new Identifier(potion_name));
			return new PotionRingRecipe(cost, length, renew, pot);
		}

		@Override
		public void write(PacketByteBuf buf, PotionRingRecipe recipe) {
			buf.writeInt(recipe.cost);
			buf.writeInt(recipe.length);
			buf.writeInt(recipe.renew);
			buf.writeString(Registries.POTION.getId(recipe.potion).toString());
		}

		@Override
		public Codec<PotionRingRecipe> codec() {
			return CODEC;
		}

	}
}
