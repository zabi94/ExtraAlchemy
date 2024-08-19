package zabi.minecraft.extraalchemy.crafting;

import java.util.List;
import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import zabi.minecraft.extraalchemy.config.ModConfig;
import zabi.minecraft.extraalchemy.items.ModComponents;
import zabi.minecraft.extraalchemy.items.ModItems;
import zabi.minecraft.extraalchemy.items.PotionRingItem;
import zabi.minecraft.extraalchemy.utils.PotionUtilities;

public class PotionRingRecipe extends SpecialCraftingRecipe {

	private int cost;
	private int length;
	private int renew;
	private Optional<Integer> level;
	private Optional<StatusEffect> effect;
	private Optional<Potion> potion;

	public PotionRingRecipe(Optional<Integer> level, int cost, int length, int renew, Optional<StatusEffect> effect, Optional<Potion> potion) {
		super(CraftingRecipeCategory.EQUIPMENT);

		this.cost = cost;
		this.length = length;
		this.renew = renew;
		
		if (effect.isEmpty() && potion.isEmpty()) {
			throw new IllegalArgumentException("Ring recipes must include a potion or a status effect");
		}
		if (effect.isPresent() && level.isEmpty()) {
			throw new IllegalArgumentException("Ring recipes defined by effect must include a potion level");
		}
		if (effect.isPresent() && potion.isPresent()) {
			throw new IllegalArgumentException("Ring recipes can't include both a potion and a status effect");
		}
		if (potion.isPresent() && potion.get().getEffects().size() != 1) {
			throw new IllegalArgumentException("Ring recipes only support potions with exactly 1 effect");
		}
		if (potion.isPresent() && level.isEmpty()) {
			level = Optional.of(potion.get().getEffects().get(0).getAmplifier());
		}
		this.level = level;
		this.effect = effect;
		this.potion = potion;
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
				if (foundEffect || !doesPotionMatch(PotionUtilities.getEffects(is))) {
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
	public ItemStack craft(RecipeInputInventory inv, WrapperLookup wl) {
		return getResult(wl);
	}
	
	@Override
	public ItemStack getResult(WrapperLookup registriesLookup) {
		ItemStack result = new ItemStack(ModItems.POTION_RING);
		result.set(ModComponents.DISABLED, true);
		result.set(ModComponents.POTION_RING_DATA, new PotionRingItem.PotionRingData(Optional.of(getEffect()), level.get(), cost, length, renew));
		return result;
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
		return stackInstance.getEffectType().value().equals(getEffect()) && stackInstance.getAmplifier() == level.get();
	}
	
	private StatusEffect getEffect() {
		if (potion.isPresent()) {
			return potion.get().getEffects().get(0).getEffectType().value();
		}
		return effect.get();
	}
	
	public static class Serializer implements RecipeSerializer<PotionRingRecipe> {
		
		private final MapCodec<PotionRingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Codec.INT.optionalFieldOf("level").forGetter(a -> a.level),
				Codec.INT.fieldOf("cost").forGetter(a -> a.cost),
				Codec.INT.fieldOf("length").forGetter(a -> a.length),
				Codec.INT.fieldOf("renew").forGetter(a -> a.renew),
				Registries.STATUS_EFFECT.getCodec().optionalFieldOf("effect").forGetter(a -> a.effect),
				Registries.POTION.getCodec().optionalFieldOf("potion").forGetter(a -> a.potion)
		).apply(instance, PotionRingRecipe::new));
				
		
		@Override
		public PacketCodec<RegistryByteBuf, PotionRingRecipe> packetCodec() {
			return new PacketCodec<RegistryByteBuf, PotionRingRecipe>() {

				@Override
				public PotionRingRecipe decode(RegistryByteBuf buf) {
					int cost = buf.readInt();
					int length = buf.readInt();
					int renew = buf.readInt();
					int level = buf.readInt();
					String potion_name = buf.readString();
					StatusEffect effect = Registries.STATUS_EFFECT.get(new Identifier(potion_name));
					return new PotionRingRecipe(Optional.of(level), cost, length, renew, Optional.of(effect), Optional.empty());
				}

				@Override
				public void encode(RegistryByteBuf buf, PotionRingRecipe recipe) {
					buf.writeInt(recipe.cost);
					buf.writeInt(recipe.length);
					buf.writeInt(recipe.renew);
					buf.writeInt(recipe.level.get());
					buf.writeString(Registries.STATUS_EFFECT.getId(recipe.getEffect()).toString());
				}
				
			};
		}

		@Override
		public MapCodec<PotionRingRecipe> codec() {
			return CODEC;
		}

	}
}
