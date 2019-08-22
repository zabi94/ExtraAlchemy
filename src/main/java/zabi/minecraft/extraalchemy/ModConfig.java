package zabi.minecraft.extraalchemy;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import zabi.minecraft.extraalchemy.lib.Reference;

@Config(modid = Reference.MID)
public class ModConfig {
	
	@Config.RequiresMcRestart
	@Config.LangKey("extraalchemy.config.options")
	public static Options options = new Options();
	
	@Config.RequiresMcRestart
	@Config.LangKey("extraalchemy.config.potions")
	public static Potions potions = new Potions();
	
	@Config.LangKey("extraalchemy.config.client")
	public static Client client = new Client();
	
	public static enum Mode {
		F3H, SHIFT, CTRL, ALT, NONE
	}
	
	public static class Options {
		@Config.Comment("Set to true to prevent the magnetism potion from working around solegnolias from Botania (Might have a negative impact on performance)")
		public boolean respectSolegnolias = true;
		@Config.Comment("If set to true, this allows the creation of longer potions (and tipped arrows)")
		public boolean allowPotionCombining = true;
		@Config.Comment("If set to true, this allows the creation of shorter potions (and tipped arrows)")
		public boolean allowPotionSplitting = true;
		@Config.Comment("Set to false to disable hardcore cheat death")
		public boolean hardcoreCheatDeath = true;
		@Config.Comment("Set to false to disable random cheat death killing and set it on timer=0")
		public boolean cheatDeathRandom = true;
		@Config.Comment("Set to false to disable the recipe for potion bags")
		public boolean enable_potion_bag = true;
		@Config.Comment("Set to false to disable the recipe of vial potions")
		public boolean breakingPotions = true;
		@Config.Comment("If set to true a brewing stand will run not only on blaze powder but also a fire below will suffice")
		public boolean useFireUndernathBrewingStand = true;
		@Config.Comment("Set to true to log all the potions in your instance with their potion-type ID. Used to create custom recipes")
		public boolean log_potion_types = false;
		@Config.Comment("Set to false to move all Extra Alchemy Items to the Vanilla Creative Alchemy Tab")
		public boolean addSeparateTab = true;
		@Config.Comment("Set to false to disable the 10%XP increase for the potion of learning to prevent XP showers-like mechanic from giving infinite XP")
		public boolean learningBoostsXP = true;
	}
	
	public static class Client {

		@Config.Comment("Choose what to toggle potion descriptions with")
		public Mode descriptionMode = Mode.SHIFT;
		@Config.Comment("Set to false to hide the bad joke in the potion tooltip")
		public boolean showBadJoke = true;
		@Config.Comment("Set to false to set static color for potion of cheat death")
		public boolean rainbowCheatDeath = true;
		
	}
	
	
	public static class Potions {
		@Config.LangKey("potion.effect.push")
		public boolean p_push = true;
		@Config.LangKey("potion.effect.cheatDeath_quiescent")
		public boolean p_cheatDeath = true;
		@Config.LangKey("potion.effect.combustion")
		public boolean p_combustion = true;
		@Config.LangKey("potion.effect.concentration")
		public boolean p_concentration = true;
		@Config.LangKey("potion.effect.crumbling")
		public boolean p_crumbling = true;
		@Config.LangKey("potion.effect.dislocation")
		public boolean p_dislocation = true;
		@Config.LangKey("potion.effect.freezing")
		public boolean p_freezing = true;
		@Config.LangKey("potion.effect.fuse")
		public boolean p_fuse = true;
		@Config.LangKey("potion.effect.hurry")
		public boolean p_hurry = true;
		@Config.LangKey("potion.effect.learning")
		public boolean p_learning = true;
		@Config.LangKey("potion.effect.magnetism")
		public boolean p_magnetism = true;
		@Config.LangKey("potion.effect.pacifism")
		public boolean p_pacifism = true;
		@Config.LangKey("potion.effect.photosynthesis")
		public boolean p_photosynthesis = true;
		@Config.LangKey("potion.effect.piper")
		public boolean p_piper = true;
		@Config.LangKey("potion.effect.recall")
		public boolean p_recall = true;
		@Config.LangKey("potion.effect.reincarnation")
		public boolean p_reincarnation = true;
		@Config.LangKey("potion.effect.return")
		public boolean p_return = true;
		@Config.LangKey("potion.effect.sinking")
		public boolean p_sinking = true;
		@Config.LangKey("potion.effect.gravity")
		public boolean p_gravity = true;
		@Config.LangKey("potion.effect.leech")
		public boolean p_leech = true;
		@Config.LangKey("potion.effect.sails")
		public boolean p_sails = true;
		@Config.LangKey("potion.effect.charged")
		public boolean p_charged_level1 = true;
		@Config.LangKey("potion.effect.charged2")
		public boolean p_charged_level2 = true;
		@Config.LangKey("potion.effect.beheading")
		public boolean p_beheading = true;
		@Config.LangKey("potion.effect.dispel")
		public boolean p_dispel = true;
		@Config.LangKey("potion.effect.pain")
		public boolean p_pain = true;
		@Config.LangKey("potion.effect.pull")
		public boolean p_pull = true;
	}
	
	public static class ChangeListener {
		@SubscribeEvent
		public void onChanged(ConfigChangedEvent evt) {
			if (evt.getModID().equals(Reference.MID)) {
				ConfigManager.sync(Reference.MID, Type.INSTANCE);
			}
		}
	}
	
	public static long getConfigSignature() {
		long status = 0;
		
		if (options.allowPotionCombining) status|=1;
		if (options.allowPotionSplitting) status|=(1<<1);
		if (options.breakingPotions) status|=(1<<2);
		if (options.enable_potion_bag) status|=(1<<3);
		if (options.respectSolegnolias) status|=(1<<5);
		if (potions.p_charged_level1) status|=(1<<6);
		if (potions.p_charged_level2) status|=(1<<7);
		if (potions.p_cheatDeath) status|=(1<<8);
		if (potions.p_combustion) status|=(1<<9);
		if (potions.p_concentration) status|=(1<<10);
		if (potions.p_crumbling) status|=(1<<11);
		if (potions.p_dispel) status|=(1<<12);
		if (potions.p_dislocation) status|=(1<<13);
		if (potions.p_freezing) status|=(1<<14);
		if (potions.p_fuse) status|=(1<<15);
		if (potions.p_gravity) status|=(1<<16);
		if (potions.p_hurry) status|=(1<<17);
		if (potions.p_learning) status|=(1<<18);
		if (potions.p_leech) status|=(1<<19);
		if (potions.p_magnetism) status|=(1<<20);
		if (potions.p_pacifism) status|=(1<<21);
		if (potions.p_photosynthesis) status|=(1<<22);
		if (potions.p_piper) status|=(1<<23);
		if (potions.p_recall) status|=(1<<24);
		if (potions.p_reincarnation) status|=(1<<25);
		if (potions.p_return) status|=(1<<26);
		if (potions.p_sails) status|=(1<<27);
		if (potions.p_sinking) status|=(1<<28);
		if (potions.p_beheading) status|=(1<<29);
		if (potions.p_pain) status |= (1<<30);
		if (potions.p_push) status |= (1<<31);
		if (potions.p_pull) status |= (1<<32);
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
			"p_dispel",
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
			"p_pain",
			"p_push",
			"p_pull"
	};
	
}
