package zabi.minecraft.extraalchemy.recipes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import zabi.minecraft.extraalchemy.ExtraAlchemy;
import zabi.minecraft.extraalchemy.ModConfig;
import zabi.minecraft.extraalchemy.items.ItemPotionRing;
import zabi.minecraft.extraalchemy.items.ModItems;
import zabi.minecraft.extraalchemy.lib.Log;
import zabi.minecraft.extraalchemy.lib.Reference;
import zabi.minecraft.extraalchemy.potion.PotionReference;
import zabi.minecraft.extraalchemy.recipes.brew.RecipeManager;
import zabi.minecraft.extraalchemy.recipes.crafting.QuickVialRecipeHandler;
import zabi.minecraft.extraalchemy.recipes.crafting.SplitPotionRecipeHandler;
import zabi.minecraft.extraalchemy.recipes.crafting.StickyPotionRecipeHandler;

@Mod.EventBusSubscriber
public class Recipes {

	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private static int customRecipes = 0;

	public static void registerRecipes() {

		loadCustomRecipes();


		//3 Variants
		if (ModConfig.potions.p_fuse) 				RecipeManager.registerRecipeWithVariant(PotionTypes.AWKWARD, new ItemStack(Items.FIREWORK_CHARGE), PotionReference.INSTANCE.TYPE_FUSE_NORMAL, PotionReference.INSTANCE.TYPE_FUSE_QUICK, PotionReference.INSTANCE.TYPE_FUSE_STRONG);
		if (ModConfig.potions.p_magnetism) 			RecipeManager.registerRecipeWithVariant(PotionTypes.AWKWARD, new ItemStack(Items.IRON_INGOT), PotionReference.INSTANCE.TYPE_MAGNETISM_NORMAL, PotionReference.INSTANCE.TYPE_MAGNETISM_LONG, PotionReference.INSTANCE.TYPE_MAGNETISM_STRONG);
		if (ModConfig.potions.p_piper) 				RecipeManager.registerRecipeWithVariant(PotionTypes.AWKWARD, new ItemStack(Items.WHEAT), PotionReference.INSTANCE.TYPE_PIPER_NORMAL, PotionReference.INSTANCE.TYPE_PIPER_LONG, PotionReference.INSTANCE.TYPE_PIPER_STRONG);
		if (ModConfig.potions.p_photosynthesis) 	RecipeManager.registerRecipeWithVariant(PotionTypes.AWKWARD, new ItemStack(Items.BEETROOT_SEEDS), PotionReference.INSTANCE.TYPE_PHOTOSYNTHESIS_NORMAL, PotionReference.INSTANCE.TYPE_PHOTOSYNTHESIS_LONG, PotionReference.INSTANCE.TYPE_PHOTOSYNTHESIS_STRONG);
		if (ModConfig.potions.p_reincarnation)		RecipeManager.registerRecipeWithVariant(PotionTypes.AWKWARD, new ItemStack(Items.LEATHER), PotionReference.INSTANCE.TYPE_REINCARNATION_NORMAL, PotionReference.INSTANCE.TYPE_REINCARNATION_LONG, PotionReference.INSTANCE.TYPE_REINCARNATION_STRONG);

		if (ModConfig.potions.p_dislocation)	 	RecipeManager.registerRecipeWithVariant(PotionTypes.MUNDANE, new ItemStack(Items.CHORUS_FRUIT), PotionReference.INSTANCE.TYPE_DISLOCATION_NORMAL, PotionReference.INSTANCE.TYPE_DISLOCATION_LONG, PotionReference.INSTANCE.TYPE_DISLOCATION_STRONG);
		if (ModConfig.potions.p_combustion) 		RecipeManager.registerRecipeWithVariant(PotionTypes.MUNDANE, new ItemStack(Item.getItemFromBlock(Blocks.COAL_BLOCK)), PotionReference.INSTANCE.TYPE_COMBUSTION_NORMAL, PotionReference.INSTANCE.TYPE_COMBUSTION_LONG, PotionReference.INSTANCE.TYPE_COMBUSTION_STRONG);

		if (ModConfig.potions.p_learning)			RecipeManager.registerRecipeWithVariant(PotionTypes.THICK, new ItemStack(Item.getItemFromBlock(Blocks.LAPIS_BLOCK)), PotionReference.INSTANCE.TYPE_LEARNING_NORMAL, PotionReference.INSTANCE.TYPE_LEARNING_LONG, PotionReference.INSTANCE.TYPE_LEARNING_STRONG);
		if (ModConfig.potions.p_gravity) 			RecipeManager.registerRecipeWithVariant(PotionTypes.THICK, new ItemStack(Items.NETHERBRICK), PotionReference.INSTANCE.TYPE_GRAVITY_NORMAL, PotionReference.INSTANCE.TYPE_GRAVITY_LONG, PotionReference.INSTANCE.TYPE_GRAVITY_STRONG);
		if (ModConfig.potions.p_crumbling) 			RecipeManager.registerRecipeWithVariant(PotionTypes.THICK, new ItemStack(Items.FLINT), PotionReference.INSTANCE.TYPE_CRUMBLING_NORMAL, PotionReference.INSTANCE.TYPE_CRUMBLING_LONG, PotionReference.INSTANCE.TYPE_CRUMBLING_STRONG);

		if (ModConfig.potions.p_recall) 			RecipeManager.registerRecipeWithVariant(PotionTypes.LONG_SLOWNESS, new ItemStack(Items.ENDER_EYE), PotionReference.INSTANCE.TYPE_RECALL_NORMAL, PotionReference.INSTANCE.TYPE_RECALL_LONG, PotionReference.INSTANCE.TYPE_RECALL_STRONG);
		if (ModConfig.potions.p_sinking) 			RecipeManager.registerRecipeWithVariant(PotionTypes.WATER_BREATHING, new ItemStack(Items.CLAY_BALL), PotionReference.INSTANCE.TYPE_SINKING_NORMAL, PotionReference.INSTANCE.TYPE_SINKING_LONG, PotionReference.INSTANCE.TYPE_SINKING_STRONG);
		if (ModConfig.potions.p_pacifism) 			RecipeManager.registerRecipeWithVariant(PotionTypes.STRONG_HARMING, new ItemStack(Items.GOLDEN_APPLE), PotionReference.INSTANCE.TYPE_PACIFISM_NORMAL, PotionReference.INSTANCE.TYPE_PACIFISM_LONG, PotionReference.INSTANCE.TYPE_PACIFISM_STRONG);
		if (ModConfig.potions.p_hurry) 				RecipeManager.registerRecipeWithVariant(PotionReference.INSTANCE.TYPE_CHARGED2, new ItemStack(Items.COOKIE), PotionReference.INSTANCE.TYPE_HURRY_NORMAL, PotionReference.INSTANCE.TYPE_HURRY_LONG, PotionReference.INSTANCE.TYPE_HURRY_STRONG);
		if (ModConfig.potions.p_leech)				RecipeManager.registerRecipeWithVariant(PotionTypes.HARMING, new ItemStack(Items.SPECKLED_MELON), PotionReference.INSTANCE.TYPE_LEECH_NORMAL, PotionReference.INSTANCE.TYPE_LEECH_LONG, PotionReference.INSTANCE.TYPE_LEECH_STRONG);
		if (ModConfig.potions.p_sails)				RecipeManager.registerRecipeWithVariant(PotionTypes.LONG_SWIFTNESS, new ItemStack(Items.FISH), PotionReference.INSTANCE.TYPE_SAILS_NORMAL, PotionReference.INSTANCE.TYPE_SAILS_LONG, PotionReference.INSTANCE.TYPE_SAILS_STRONG);
		if (ModConfig.potions.p_beheading)			RecipeManager.registerRecipeWithVariant(PotionReference.INSTANCE.TYPE_CHARGED2, new ItemStack(Items.ROTTEN_FLESH), PotionReference.INSTANCE.TYPE_BEHEADING_NORMAL, PotionReference.INSTANCE.TYPE_BEHEADING_LONG, PotionReference.INSTANCE.TYPE_BEHEADING_STRONG);
		if (ModConfig.potions.p_pain)				RecipeManager.registerRecipeWithVariant(PotionTypes.WEAKNESS, new ItemStack(Items.IRON_NUGGET), PotionReference.INSTANCE.TYPE_PAIN_NORMAL, PotionReference.INSTANCE.TYPE_PAIN_LONG, PotionReference.INSTANCE.TYPE_PAIN_STRONG);
		if (ModConfig.potions.p_push)				RecipeManager.registerRecipeWithVariant(PotionReference.INSTANCE.TYPE_PULL_NORMAL, new ItemStack(Items.FERMENTED_SPIDER_EYE), PotionReference.INSTANCE.TYPE_PUSH_NORMAL, PotionReference.INSTANCE.TYPE_PUSH_LONG, PotionReference.INSTANCE.TYPE_PUSH_STRONG);
		if (ModConfig.potions.p_pull)				RecipeManager.registerRecipeWithVariant(PotionTypes.SWIFTNESS, new ItemStack(Items.GOLD_NUGGET), PotionReference.INSTANCE.TYPE_PULL_NORMAL, PotionReference.INSTANCE.TYPE_PULL_LONG, PotionReference.INSTANCE.TYPE_PULL_STRONG);

		//1 Variant
		if (ModConfig.potions.p_concentration) 	RecipeManager.registerRecipe(PotionTypes.AWKWARD, PotionReference.INSTANCE.TYPE_CONCENTRATION, Items.EGG);
		if (ModConfig.potions.p_freezing)			RecipeManager.registerRecipe(PotionTypes.AWKWARD, PotionReference.INSTANCE.TYPE_FREEZING, Items.SNOWBALL);
		if (ModConfig.potions.p_return)			RecipeManager.registerRecipe(PotionTypes.AWKWARD, PotionReference.INSTANCE.TYPE_RETURN, Items.PRISMARINE_SHARD);
		if (ModConfig.potions.p_cheatDeath)		RecipeManager.registerRecipe(PotionReference.INSTANCE.TYPE_CHARGED2, PotionReference.INSTANCE.TYPE_CHEAT_DEATH, Items.GOLDEN_APPLE);
		if (ModConfig.potions.p_dispel)			RecipeManager.registerRecipe(PotionReference.INSTANCE.TYPE_LEARNING_STRONG, PotionReference.INSTANCE.TYPE_DISPEL, Items.POISONOUS_POTATO);
		if (ModConfig.potions.p_charged_level2) 	RecipeManager.registerRecipe(PotionReference.INSTANCE.TYPE_CHARGED, PotionReference.INSTANCE.TYPE_CHARGED2, Items.PRISMARINE_CRYSTALS);
		if (ModConfig.potions.p_charged_level1) 	RecipeManager.registerRecipe(PotionTypes.THICK, PotionReference.INSTANCE.TYPE_CHARGED, Items.GOLD_INGOT);


		if (ModConfig.options.breakingPotions) {
			GameRegistry.addShapedRecipe(new ResourceLocation(Reference.MID, "vial"), null, new ItemStack(ModItems.vial_break, 3)," F ","G G"," G ", 
					'G', "blockGlass", 
					'F', Items.FLINT);
		}
		if (ModConfig.options.enable_potion_bag) {
			GameRegistry.addShapedRecipe(new ResourceLocation(Reference.MID, "recipe_potion_bag"), null, new ItemStack(ModItems.potion_bag), 
					" V ","LWL","LLL", 
					'V', Items.GLASS_BOTTLE, 
					'W', "chestWood", 
					'L', "leather");
		}
		
		if (ModConfig.options.enablePotionRings) {
			GameRegistry.addShapedRecipe(new ResourceLocation(Reference.MID, "empty_ring"), null, new ItemStack(ModItems.empty_ring), 
					"NIN", "IBI", "NIN",
					'I', Items.GOLD_INGOT,
					'N', Items.GOLD_NUGGET,
					'B', Items.GLASS_BOTTLE
			);
			
			ItemPotionRing.addRecipes();
		}
		
	}

	private static void loadCustomRecipes() {
		if (ExtraAlchemy.recipesDirectory.mkdir()) {
			try {
				generateExampleRecipe();
			} catch (IOException e) {
				Log.e("Cannot generate example file");
				e.printStackTrace();
			}
		} else if (!ExtraAlchemy.recipesDirectory.isDirectory()) {
			Log.e("Cannot access custom potion folder");
			return;
		}
		for (File json:ExtraAlchemy.recipesDirectory.listFiles()) {
			if (json.isFile() && json.getName().endsWith(".json")) {
				readJson(json);
			}
		}
		Log.i("Generated "+customRecipes+" custom recipes");
	}

	private static void generateExampleRecipe() throws IOException {
		GsonRecipe example = new GsonRecipe();
		
		example.name = "normal_potion";
		example.ingredient = "minecraft:rotten_flesh";
		example.durationSeconds = 120;
		example.level = 0;
		example.effect = "minecraft:unluck";
		example.autoGenerateLongVersion = true;
		example.autoGenerateStrongVersion = true;
		example.durationLongVersion = 240;
		example.durationStrongVersion = 60;
		writeFile("example_normal_potion", example);
		
		example.name = "instant_with_meta";
		example.ingredient = "minecraft:dye@2";
		example.durationSeconds = 0;
		example.level = 0;
		example.basePotionType = "minecraft:harming";
		example.effect = "minecraft:instant_damage";
		example.autoGenerateLongVersion = false;
		example.autoGenerateStrongVersion = true;
		example.durationLongVersion = 0;
		example.durationStrongVersion = 0;
		writeFile("example_instant_effect_ingredient_metadata", example);
		
		example.name = "any_meta";
		example.ingredient = "minecraft:wool";
		example.durationSeconds = 0;
		example.level = 0;
		example.basePotionType = "minecraft:thick";
		example.effect = "minecraft:poison";
		example.autoGenerateLongVersion = false;
		example.autoGenerateStrongVersion = true;
		example.durationLongVersion = 0;
		example.durationStrongVersion = 0;
		writeFile("example_any_metadata", example);
	}
	
	private static void writeFile(String filename, GsonRecipe recipe) throws IOException {
		FileWriter writer = null;
		File destination = new File(ExtraAlchemy.recipesDirectory, filename+".json_disabled");
		try {
			writer = new FileWriter(destination);
			writer.write(gson.toJson(recipe));
		} finally {
			if (writer != null) writer.close();
		}
	}

	private static void readJson(File json) {
		try {
			GsonRecipe recipe = gson.fromJson(new FileReader(json), GsonRecipe.class);
			if (recipe.check()) {
				ResourceLocation typeName = new ResourceLocation(recipe.name);
				PotionType type = null;
				if (ForgeRegistries.POTION_TYPES.containsKey(typeName)) {
					type = ForgeRegistries.POTION_TYPES.getValue(typeName);
				} else {
					type = recipe.generateType().setArtificial(true);
					ForgeRegistries.POTION_TYPES.register(type);
				}
				RecipeManager.registerRecipe(recipe.getBase(), type, recipe.getIngredient());
				Log.d("Registered custom potion recipe: "+type.getRegistryName());
				customRecipes++;
				
				if (recipe.autoGenerateLongVersion) {
					PotionType longType = recipe.generateTypeLong().setArtificial(true);
					ForgeRegistries.POTION_TYPES.register(longType);
					RecipeManager.registerRecipe(type, longType, RecipeManager.redstone);
					Log.d("Registered custom potion recipe: "+longType.getRegistryName());
					customRecipes++;
				}
				
				if (recipe.autoGenerateStrongVersion) {
					PotionType strongType = recipe.generateTypeStrong().setArtificial(true);
					ForgeRegistries.POTION_TYPES.register(strongType);
					RecipeManager.registerRecipe(type, strongType, RecipeManager.glowstone);
					Log.d("Registered custom potion recipe: "+strongType.getRegistryName());
					customRecipes++;
				}
			} else {
				Log.w("Json recipe contains errors: "+ json.getAbsolutePath());
			}
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			Log.e("Cannot read custom potion recipe from file "+json.getName());
			e.printStackTrace();
		}
	}

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> evt) {
		IForgeRegistry<IRecipe> reg = evt.getRegistry();
		if (ModConfig.options.allowPotionCombining) reg.register(new StickyPotionRecipeHandler());
		if (ModConfig.options.allowPotionSplitting) reg.register(new SplitPotionRecipeHandler());
		if (ModConfig.options.breakingPotions) reg.register(new QuickVialRecipeHandler());
	}
}
