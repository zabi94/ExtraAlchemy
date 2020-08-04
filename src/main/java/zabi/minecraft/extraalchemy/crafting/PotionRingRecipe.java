package zabi.minecraft.extraalchemy.crafting;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import zabi.minecraft.extraalchemy.config.ModConfig;
import zabi.minecraft.extraalchemy.items.ModItems;
import zabi.minecraft.extraalchemy.items.PotionRingItem;
import zabi.minecraft.extraalchemy.utils.Log;

public class PotionRingRecipe extends SpecialCraftingRecipe {

	private int cost;
	private int length;
	private int renew;
	private Potion potion;

	public PotionRingRecipe(Identifier id, int cost, int length, int renew, Potion potion) {
		super(id);
		this.cost = cost;
		this.length = length;
		this.potion = potion;
		this.renew = renew;
	}

	@Override
	public boolean matches(CraftingInventory inv, World world) {
		if (!ModConfig.INSTANCE.enableRings || potion.getEffects().size() == 0) { //Globally disabled and specifically disabled
			return false;
		}

		boolean foundPotion = false;
		boolean foundRing = false;

		for (int i = 0; i < inv.getInvSize(); i++) {
			ItemStack is = inv.getInvStack(i); 
			Item s = is.getItem();
			if (s.equals(Items.POTION)) {
				if (foundPotion || !PotionUtil.getPotion(is).equals(this.potion)) {
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
	public ItemStack craft(CraftingInventory inv) {
		ItemStack result = new ItemStack(ModItems.POTION_RING);
		PotionUtil.setPotion(result, potion);
		result.getTag().putInt("cost", cost);
		result.getTag().putInt("length", length);
		result.getTag().putInt("renew", renew);
		result.getTag().putBoolean("disabled", true);
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

	public static class Serializer implements RecipeSerializer<PotionRingRecipe> {

		@Override
		public PotionRingRecipe read(Identifier id, JsonObject json) {
			int cost = json.get("cost").getAsInt();
			int length = json.get("length").getAsInt();
			int renewTime = json.has("renew") ? json.get("renew").getAsInt() : 1;
			String potion_name = json.get("potion").getAsString();
			Potion pot = Registry.POTION.get(new Identifier(potion_name));
			if (pot.getEffects().size() > 1) {
				Log.w("The ring recipe %s has more than 1 effect associated with it, this functionality is meant for 1-effect potions.");
			} 
			if (pot.getEffects().stream().allMatch(sei -> sei.getEffectType().isInstant())) {
				Log.w("The ring recipe %s has no non-instant effects associated with %s, this functionality is meant for long lasting effects.", id, potion_name);
			}
			return new PotionRingRecipe(id, cost, length, renewTime, pot);
		}

		@Override
		public PotionRingRecipe read(Identifier id, PacketByteBuf buf) {
			int cost = buf.readInt();
			int length = buf.readInt();
			int renew = buf.readInt();
			String potion_name = buf.readString();
			Potion pot = Registry.POTION.get(new Identifier(potion_name));
			return new PotionRingRecipe(id, cost, length, renew, pot);
		}

		@Override
		public void write(PacketByteBuf buf, PotionRingRecipe recipe) {
			buf.writeInt(recipe.cost);
			buf.writeInt(recipe.length);
			buf.writeInt(recipe.renew);
			buf.writeString(Registry.POTION.getId(recipe.potion).toString());
		}

	}

	public static void generateDefaults() {
		if ("true".equals(System.getProperty("devGenerateDefaultRingRecipes"))) {
			final Gson gson = new GsonBuilder().setPrettyPrinting().create();
			final File POTFOLDER = new File("POT_OUT");
			POTFOLDER.mkdir();
			Registry.POTION.stream()
			.filter(p -> p.getEffects().size() == 1)
			.filter(p -> !p.hasInstantEffect())
			.filter(PotionRingItem.ignoreLongVersions())
			.forEach(pot -> {
				PotionJson en = new PotionJson(pot);
				File out = new File(POTFOLDER, en.potion.replace(':', '_')+".json");
				try (FileWriter fw = new FileWriter(out)) {
					fw.write(gson.toJson(en));
					Log.w("Generated: " + out.getName());
				} catch (IOException e1) {
					Log.e("Not generated: " + out.getName());
					e1.printStackTrace();
				}
			});
			throw new RuntimeException("Ring Potion Recipes generated in "+POTFOLDER.getAbsolutePath());
		}
	}
	
	public static class PotionJson {
		
		public PotionJson(Potion potion) {
			StatusEffectInstance sei = potion.getEffects().get(0);
			this.cost = 2 * (sei.getAmplifier() + 1);
			this.length = 4 + sei.getDuration() / 200;
			this.renew = 1;
			this.potion = Registry.POTION.getId(potion).toString();
		}
		
		public int cost, length, renew;
		public String potion, type = "extraalchemy:potion_ring";
		
	}
}
