package zabi.minecraft.extraalchemy.lib;

import java.io.File;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Config {
	
	private static final String[] DescriptionToggleMode = new String[] {
			"F3H", "SHIFT", "CTRL", "ALT", "NONE"
	};
	
	public static Configuration configuration;
	
	public static boolean needPurge = false;
	
	public static ConfigCategory general, compat, visual, tooltips, potions;
	
	public static Property respectSolegnolias, loadAsCovensPlugin/*, loadBotaniaPotions, addBotaniaRecipesAutomatically, loadRusticPotions, addRusticRecipesAutomatically*/;
	public static Property showBadJoke,useJEITooltipWrapping,descriptionMode;
	public static Property allowPotionCombining, allowPotionSplitting, /*versionCheck,*/ hardcoreCheatDeath, cheatDeathRandom, enable_potion_bag, useNewVials, breakingPotions, useFireUndernathBrewingStand;
	public static Property log_potion_types,removeGlowingEffect,rainbowCheatDeath,useCustomParticles,addSeparateTab;
	
	public static Property p_cheatDeath,p_combustion,p_concentration,p_crumbling,p_detection,p_dislocation,p_freezing,p_fuse,p_hurry,p_learning,p_magnetism,p_pacifism,
	p_photosynthesis,p_piper,p_recall,p_reincarnation,p_return,p_sinking,p_gravity,p_leech,p_sails,p_charged_level1,p_charged_level2,p_beheading,p_dispel;
	
	public static void init(File config) {
		Log.d("Loading config");
		boolean fileExisted = config.exists();
		configuration = new Configuration(config, Reference.release+"");
		
		
		general = new ConfigCategory("General Settings");
		compat = new ConfigCategory("Compatibility", general);
		visual = new ConfigCategory("Client");
		tooltips = new ConfigCategory("Tooltips", visual);
		potions = new ConfigCategory("Potions", general);
		
		general.setComment("General settings for Extra Alchemy. Most of the options needs to be the same in a server to join");
		general.setRequiresMcRestart(true);
		compat.setComment("Settings for integration with other mods");
		compat.setRequiresMcRestart(true);
		visual.setComment("Client side only settings");
		visual.setRequiresMcRestart(true);
		tooltips.setComment("Visual settings for tooltips");
		tooltips.setRequiresMcRestart(false);
		potions.setComment("Toggles for single potions' recipes");
		potions.setRequiresMcRestart(true);
		
		configuration.setCategoryRequiresMcRestart(tooltips.getQualifiedName(), false);
		
		configuration.load();
		
		if (configuration.getLoadedConfigVersion()==null && fileExisted) {
			Log.w("Purging old config file");
			String oldPath = config.getAbsolutePath();
			config.renameTo(new File(config.getAbsolutePath()+"."+config.lastModified()+".backup"));
			init(new File(oldPath));
		}
		
		loadValues();
		
		if (configuration.hasChanged()) save();
	}
	
	public static void loadValues() {
		//General
		allowPotionCombining = configuration.get(general.getQualifiedName(), "allowPotionCombining", true);
		allowPotionCombining.setComment("If set to true, this allows the creation of longer potions (and tipped arrows)");
		general.put("allowPotionCombining", allowPotionCombining);
		
		allowPotionSplitting = configuration.get(general.getQualifiedName(), "allowPotionSplitting", true);
		allowPotionSplitting.setComment("If set to true, this allows the creation of shorter potions (and tipped arrows)");
		general.put("allowPotionSplitting", allowPotionSplitting);
		
//		versionCheck = configuration.get(general.getQualifiedName(), "versionCheck", true);
//		versionCheck.setComment("Set to false to disable version checking on startup");
//		general.put("versionCheck", versionCheck);
		
		hardcoreCheatDeath = configuration.get(general.getQualifiedName(), "hardcoreCheatDeath", true);
		hardcoreCheatDeath.setComment("Set to false to disable hardcore cheat death");
		general.put("hardcoreCheatDeath", hardcoreCheatDeath);
		
		cheatDeathRandom = configuration.get(general.getQualifiedName(), "cheatDeathRandom", true);
		cheatDeathRandom.setComment("Set to false to disable random cheat death killing and set it on timer=0");
		general.put("cheatDeathRandom", cheatDeathRandom);
		
		useFireUndernathBrewingStand = configuration.get(general.getQualifiedName(), "useFireUndernathBrewingStand", true);
		useFireUndernathBrewingStand.setComment("If set to true a brewing stand will run not only on blaze powder but also a fire below will suffice");
		general.put("useFireUndernathBrewingStand", useFireUndernathBrewingStand);
		
		breakingPotions = configuration.get(general.getQualifiedName(), "breakingPotions", true);
		breakingPotions.setComment("Set to false to disable vial potions");
		general.put("breakingPotions", breakingPotions);
		
		enable_potion_bag = configuration.get(general.getQualifiedName(), "enable_potion_bag", true);
		enable_potion_bag.setComment("Set to false to disable the potion bag");
		general.put("enable_potion_bag", enable_potion_bag);
		
		useNewVials = configuration.get(general.getQualifiedName(), "useNewVials", true);
		useNewVials.setComment("Set to false to use old, instant vials");
		general.put("useNewVials", useNewVials);
		
		
		//Compat
		respectSolegnolias = configuration.get(compat.getQualifiedName(), "respectSolegnolias", true);
		respectSolegnolias.setComment("Set to true to prevent the magnetism potion from working around solegnolias from Botania (Might have a negative impact on performance)");
		compat.put("respectSolegnolias", respectSolegnolias);
		
		loadAsCovensPlugin = configuration.get(compat.getQualifiedName(), "loadAsCovensPlugin", true);
		loadAsCovensPlugin.setComment("When set to true and Covens is installed, use this mod as a simple plugin to add potion effects");
		compat.put("loadAsCovensPlugin", loadAsCovensPlugin);
		
//		loadBotaniaPotions = configuration.get(compat.getQualifiedName(), "loadBotaniaPotions", true);
//		loadBotaniaPotions.setComment("Set to false to prevent botania potions from loading as normal potions");
//		compat.put("loadBotaniaPotions", loadBotaniaPotions);
//		
//		addBotaniaRecipesAutomatically = configuration.get(compat.getQualifiedName(), "addBotaniaRecipesAutomatically", false);
//		addBotaniaRecipesAutomatically.setComment("If \"loadBotaniaPotion\" is set to true, this will add recipes for them. You can enable this if you don't want to add custom recipes");
//		compat.put("addBotaniaRecipesAutomatically", addBotaniaRecipesAutomatically);
//		
//		loadRusticPotions = configuration.get(compat.getQualifiedName(), "loadRusticPotions", true);
//		loadRusticPotions.setComment("Set to false to prevent rustic potions from loading as normal potions");
//		compat.put("loadRusticPotions", loadRusticPotions);
//		
//		addRusticRecipesAutomatically = configuration.get(compat.getQualifiedName(), "addRusticRecipesAutomatically", false);
//		addRusticRecipesAutomatically.setComment("If \"loadRusticPotions\" is set to true, this will add recipes for them. You can enable this if you don't want to add custom recipes");
//		compat.put("addRusticRecipesAutomatically", addRusticRecipesAutomatically);
		
		//Visual
		log_potion_types = configuration.get(visual.getQualifiedName(), "log_potion_types", false);
		log_potion_types.setComment("Set to true to log all the potions in your instance with their potion-type ID. Used to create custom recipes");
		visual.put("log_potion_types", log_potion_types);

		rainbowCheatDeath = configuration.get(visual.getQualifiedName(), "rainbowCheatDeath", true);
		rainbowCheatDeath.setComment("Set to false to set static color for potion of cheat death");
		visual.put("rainbowCheatDeath", rainbowCheatDeath);

		useCustomParticles = configuration.get(visual.getQualifiedName(), "useCustomParticles", true);
		useCustomParticles.setComment("Set to false to disable custom particles");
		visual.put("useCustomParticles", useCustomParticles);

		removeGlowingEffect = configuration.get(visual.getQualifiedName(), "removeGlowingEffect", true);
		removeGlowingEffect.setComment("Set to true to remove the glowing effect from vanilla potions and all those deriving from them");
		visual.put("removeGlowingEffect", removeGlowingEffect);

		addSeparateTab = configuration.get(visual.getQualifiedName(), "addSeparateTab", true);
		addSeparateTab.setComment("Set to false to move all Extra Alchemy Items to the Vanilla Creative Alchemy Tab");
		visual.put("addSeparateTab", addSeparateTab);
		
		//Tooltips
		
		showBadJoke = configuration.get(tooltips.getQualifiedName(), "showBadJoke", true);
		showBadJoke.setComment("Set to false to hide the bad joke in the potion tooltip");
		showBadJoke.setRequiresMcRestart(false);
		tooltips.put("showBadJoke", showBadJoke);
		
		useJEITooltipWrapping = configuration.get(tooltips.getQualifiedName(), "useJEITooltipWrapping", true);
		useJEITooltipWrapping.setComment("If set to true, lets JEI do the tooltip wrapping instead of fixed one when JEI is installed");
		useJEITooltipWrapping.setRequiresMcRestart(false);
		tooltips.put("useJEITooltipWrapping", useJEITooltipWrapping);
		
		descriptionMode = configuration.get(tooltips.getQualifiedName(), "descriptionMode", DescriptionToggleMode[1]);
		descriptionMode.setComment("Choose what to toggle potion descriptions with");
		descriptionMode.setRequiresMcRestart(false);
		descriptionMode.setValidValues(DescriptionToggleMode);
		tooltips.put("descriptionMode", descriptionMode);
		
		//Pots
		p_cheatDeath = configuration.get(potions.getQualifiedName(), "p_cheatDeath", true);
		potions.put("p_cheatDeath", p_cheatDeath);
		
		p_charged_level1 = configuration.get(potions.getQualifiedName(), "p_charged_level1", true);
		potions.put("p_charged_level1", p_charged_level1);
		
		p_charged_level2 = configuration.get(potions.getQualifiedName(), "p_charged_level2", true);
		potions.put("p_charged_level2", p_charged_level2);
		
		p_combustion = configuration.get(potions.getQualifiedName(), "p_combustion", true);
		potions.put("p_combustion", p_combustion);
		
		p_concentration = configuration.get(potions.getQualifiedName(), "p_concentration", true);
		potions.put("p_concentration", p_concentration);
		
		p_crumbling = configuration.get(potions.getQualifiedName(), "p_crumbling", true);
		potions.put("p_crumbling", p_crumbling);
		
		p_detection = configuration.get(potions.getQualifiedName(), "p_detection", true);
		potions.put("p_detection", p_detection);
		
		p_dislocation = configuration.get(potions.getQualifiedName(), "p_dislocation", true);
		potions.put("p_dislocation", p_dislocation);
		
		p_freezing = configuration.get(potions.getQualifiedName(), "p_freezing", true);
		potions.put("p_freezing", p_freezing);
		
		p_fuse = configuration.get(potions.getQualifiedName(), "p_fuse", true);
		potions.put("p_fuse", p_fuse);
		
		p_gravity = configuration.get(potions.getQualifiedName(), "p_gravity", true);
		potions.put("p_gravity", p_gravity);
		
		p_hurry = configuration.get(potions.getQualifiedName(), "p_hurry", true);
		potions.put("p_hurry", p_hurry);
		
		p_learning = configuration.get(potions.getQualifiedName(), "p_learning", true);
		potions.put("p_learning", p_learning);
		
		p_leech = configuration.get(potions.getQualifiedName(), "p_leech", true);
		potions.put("p_leech", p_leech);
		
		p_magnetism = configuration.get(potions.getQualifiedName(), "p_magnetism", true);
		potions.put("p_magnetism", p_magnetism);
		
		p_pacifism = configuration.get(potions.getQualifiedName(), "p_pacifism", true);
		potions.put("p_pacifism", p_pacifism);
		
		p_photosynthesis = configuration.get(potions.getQualifiedName(), "p_photosynthesis", true);
		potions.put("p_photosynthesis", p_photosynthesis);
		
		p_piper = configuration.get(potions.getQualifiedName(), "p_piper", true);
		potions.put("p_piper", p_piper);
		
		p_recall = configuration.get(potions.getQualifiedName(), "p_recall", true);
		potions.put("p_recall", p_recall);
		
		p_reincarnation = configuration.get(potions.getQualifiedName(), "p_reincarnation", true);
		potions.put("p_reincarnation", p_reincarnation);
		
		p_return = configuration.get(potions.getQualifiedName(), "p_return", true);
		potions.put("p_return", p_return);
		
		p_sails = configuration.get(potions.getQualifiedName(), "p_sails", true);
		potions.put("p_sails", p_sails);
		
		p_sinking = configuration.get(potions.getQualifiedName(), "p_sinking", true);
		potions.put("p_sinking", p_sinking);
		
		p_beheading = configuration.get(potions.getQualifiedName(), "p_beheading", true);
		potions.put("p_beheading", p_beheading);
		
		p_dispel = configuration.get(potions.getQualifiedName(), "p_dispel", true);
		potions.put("p_dispel", p_dispel);
		
		for (String name:c_names) {
			configuration.get(potions.getQualifiedName(), name, true).setComment("Toggle Potion");
		}
	}
	
	public static void save() {
		configuration.save();
	}
	
	public static long getConfigSignature() {
		long status = 0;
		
		if (allowPotionCombining.getBoolean()) status|=1;
		if (allowPotionSplitting.getBoolean()) status|=2;
		if (breakingPotions.getBoolean()) status|=4;
		if (enable_potion_bag.getBoolean()) status|=8;
		if (useNewVials.getBoolean()) status|=16;
		if (respectSolegnolias.getBoolean()) status|=32;
		if (p_charged_level1.getBoolean()) status|=64;
		if (p_charged_level2.getBoolean()) status|=128;
		if (p_cheatDeath.getBoolean()) status|=256;
		if (p_combustion.getBoolean()) status|=512;
		if (p_concentration.getBoolean()) status|=1024;
		if (p_crumbling.getBoolean()) status|=2048;
		if (p_detection.getBoolean()) status|=4096;
		if (p_dislocation.getBoolean()) status|=8192;
		if (p_freezing.getBoolean()) status|=(1<<14);
		if (p_fuse.getBoolean()) status|=(1<<15);
		if (p_gravity.getBoolean()) status|=(1<<16);
		if (p_hurry.getBoolean()) status|=(1<<17);
		if (p_learning.getBoolean()) status|=(1<<18);
		if (p_leech.getBoolean()) status|=(1<<19);
		if (p_magnetism.getBoolean()) status|=(1<<20);
		if (p_pacifism.getBoolean()) status|=(1<<21);
		if (p_photosynthesis.getBoolean()) status|=(1<<22);
		if (p_piper.getBoolean()) status|=(1<<23);
		if (p_recall.getBoolean()) status|=(1<<24);
		if (p_reincarnation.getBoolean()) status|=(1<<25);
		if (p_return.getBoolean()) status|=(1<<26);
		if (p_sails.getBoolean()) status|=(1<<27);
		if (p_sinking.getBoolean()) status|=(1<<28);
		if (p_beheading.getBoolean()) status|=(1<<29);
		if (p_dispel.getBoolean()) status|=(1<<30);
//		if (loadBotaniaPotions.getBoolean()) status|=(1<<31);
//		if (loadRusticPotions.getBoolean()) status|=(1<<32);
		return status;
	}
	
	public static String getDifferences(long compare) {
		long current = getConfigSignature();
		String conflicts = "";
		int diffs = 0;
		for (int i=0;i<c_names.length;i++) {
			if (((current>>i)&1)!=((compare>>i)&1)) {
				conflicts+=(c_names[i]+"\n");
				diffs++;
				if (diffs>=5) {
					conflicts+="[...]";
					break;
				}
			}
		}
		return conflicts.trim();
	}
	
	private static String[] c_names = new String[] {
			"allowPotionCombining",
			"allowPotionSplitting",
			"breakingPotions",
			"enable_potion_bag",
			"useNewVials",
			"respectSolegnolias",
			"p_charged_level1",
			"p_charged_level2",
			"p_cheatDeath",
			"p_combustion",
			"p_concentration",
			"p_crumbling",
			"p_detection",
			"p_dislocation",
			"p_freezing",
			"p_fuse",
			"p_gravity",
			"p_hurry",
			"p_learning",
			"p_leech",
			"p_magnetism",
			"p_pacifism",
			"p_photosynthesis",
			"p_piper",
			"p_recall",
			"p_reincarnation",
			"p_return",
			"p_sails",
			"p_sinking",
			"p_beheading",
			"p_dispel"
//			"loadBotaniaPotions",
//			"loadRusticPotions"
	};
	
	public static class ConfigHandler {
		
		@SubscribeEvent
		public void onConfigChanged(ConfigChangedEvent evt) {
			if (evt.getModID().equals(Reference.MID)) {
				Config.save();
			}
		}
		
	}
	
}
