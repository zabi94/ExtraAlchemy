package zabi.minecraft.extraalchemy.potion;

import zabi.minecraft.extraalchemy.potion.potion.PotionBeheading;
import zabi.minecraft.extraalchemy.potion.potion.PotionCheatDeath;
import zabi.minecraft.extraalchemy.potion.potion.PotionCombustion;
import zabi.minecraft.extraalchemy.potion.potion.PotionConcentration;
import zabi.minecraft.extraalchemy.potion.potion.PotionCrumbling;
import zabi.minecraft.extraalchemy.potion.potion.PotionDislocation;
import zabi.minecraft.extraalchemy.potion.potion.PotionDispel;
import zabi.minecraft.extraalchemy.potion.potion.PotionFreezing;
import zabi.minecraft.extraalchemy.potion.potion.PotionFuse;
import zabi.minecraft.extraalchemy.potion.potion.PotionGravity;
import zabi.minecraft.extraalchemy.potion.potion.PotionHurry;
import zabi.minecraft.extraalchemy.potion.potion.PotionLearning;
import zabi.minecraft.extraalchemy.potion.potion.PotionLeech;
import zabi.minecraft.extraalchemy.potion.potion.PotionMagnetism;
import zabi.minecraft.extraalchemy.potion.potion.PotionPacifism;
import zabi.minecraft.extraalchemy.potion.potion.PotionPain;
import zabi.minecraft.extraalchemy.potion.potion.PotionPhotosynthesis;
import zabi.minecraft.extraalchemy.potion.potion.PotionPiper;
import zabi.minecraft.extraalchemy.potion.potion.PotionRecall;
import zabi.minecraft.extraalchemy.potion.potion.PotionReincarnation;
import zabi.minecraft.extraalchemy.potion.potion.PotionReturn;
import zabi.minecraft.extraalchemy.potion.potion.PotionSails;
import zabi.minecraft.extraalchemy.potion.potion.PotionSinking;

public class PotionReference {
	
	public static final PotionReference INSTANCE = new PotionReference();
	
	public PotionBase FUSE = new PotionFuse(true, 16711680);
	public PotionTypeBase TYPE_FUSE_NORMAL = new PotionTypeBase(FUSE, 20*20, 0, "fuse_normal");
	public PotionTypeBase TYPE_FUSE_STRONG = new PotionTypeBase(FUSE, 20*30, 1, "fuse_strong");
	public PotionTypeBase TYPE_FUSE_QUICK = new PotionTypeBase(FUSE, 20*10, 0, "fuse_quick");


	public PotionBase RECALL = new PotionRecall(false, 16773632);
	public PotionTypeBase TYPE_RECALL_NORMAL = new PotionTypeBase(RECALL, 20*40, 0, "recall_normal");
	public PotionTypeBase TYPE_RECALL_STRONG = new PotionTypeBase(RECALL, 20*30, 1, "recall_strong");
	public PotionTypeBase TYPE_RECALL_LONG = new PotionTypeBase(RECALL, 20*80, 0, "recall_long");
	
	public PotionBase SINKING = new PotionSinking(true, 49061);
	public PotionTypeBase TYPE_SINKING_NORMAL = new PotionTypeBase(SINKING, 20*30, 0, "sinking_normal");
	public PotionTypeBase TYPE_SINKING_STRONG = new PotionTypeBase(SINKING, 20*15, 1, "sinking_strong");
	public PotionTypeBase TYPE_SINKING_LONG = new PotionTypeBase(SINKING, 20*40, 0, "sinking_long");
	
	public PotionBase FREEZING = new PotionFreezing(true, 12640752);
	public PotionTypeBase TYPE_FREEZING = new PotionTypeBase(FREEZING, 0, 0, "freezing");
	
	public PotionBase DISLOCATION = new PotionDislocation(false, 9658805);
	public PotionTypeBase TYPE_DISLOCATION_NORMAL = new PotionTypeBase(DISLOCATION, 20*15, 0, "dislocation_normal");
	public PotionTypeBase TYPE_DISLOCATION_STRONG = new PotionTypeBase(DISLOCATION, 20*10, 1, "dislocation_strong");
	public PotionTypeBase TYPE_DISLOCATION_LONG = new PotionTypeBase(DISLOCATION, 20*30, 0, "dislocation_long");
	
	public PotionBase MAGNETISM = new PotionMagnetism(false, 13092807);
	public PotionTypeBase TYPE_MAGNETISM_NORMAL = new PotionTypeBase(MAGNETISM, 20*180, 0, "magnetism_normal");
	public PotionTypeBase TYPE_MAGNETISM_STRONG = new PotionTypeBase(MAGNETISM, 20*120, 1, "magnetism_strong");
	public PotionTypeBase TYPE_MAGNETISM_LONG = new PotionTypeBase(MAGNETISM, 20*240, 0, "magnetism_long");
	
	public PotionBase PIPER = new PotionPiper(false, 16768927);
	public PotionTypeBase TYPE_PIPER_NORMAL = new PotionTypeBase(PIPER, 20*120, 0, "pyper_normal");
	public PotionTypeBase TYPE_PIPER_STRONG = new PotionTypeBase(PIPER, 20*100, 1, "pyper_strong");
	public PotionTypeBase TYPE_PIPER_LONG = new PotionTypeBase(PIPER, 20*240, 0, "pyper_long");
	
	public PotionBase PACIFISM = new PotionPacifism(false, 16711655);
	public PotionTypeBase TYPE_PACIFISM_NORMAL = new PotionTypeBase(PACIFISM, 20*25, 0, "pacifism_normal");
	public PotionTypeBase TYPE_PACIFISM_STRONG = new PotionTypeBase(PACIFISM, 20*15, 1, "pacifism_strong");
	public PotionTypeBase TYPE_PACIFISM_LONG = new PotionTypeBase(PACIFISM, 20*40, 0, "pacifism_long");
	
	public PotionBase CRUMBLING = new PotionCrumbling(false, 10587992);
	public PotionTypeBase TYPE_CRUMBLING_NORMAL = new PotionTypeBase(CRUMBLING, 20*60, 0, "crumbling_normal");
	public PotionTypeBase TYPE_CRUMBLING_STRONG = new PotionTypeBase(CRUMBLING, 20*40, 1, "crumbling_strong");
	public PotionTypeBase TYPE_CRUMBLING_LONG = new PotionTypeBase(CRUMBLING, 20*100, 0, "crumbling_long");
	
	public PotionBase CONCENTRATION = new PotionConcentration(false, 13157861);
	public PotionTypeBase TYPE_CONCENTRATION = new PotionTypeBase(CONCENTRATION, 0, 0, "concentration_normal");
	
	public PotionBase RETURN = new PotionReturn(false, 13653381);
	public PotionTypeBase TYPE_RETURN = new PotionTypeBase(RETURN, 0, 0, "return_normal");
	
	public PotionBase PHOTOSYNTHESIS = new PotionPhotosynthesis(false, 47872);
	public PotionTypeBase TYPE_PHOTOSYNTHESIS_NORMAL = new PotionTypeBase(PHOTOSYNTHESIS, 20*80, 0, "photosynthesis_normal");
	public PotionTypeBase TYPE_PHOTOSYNTHESIS_STRONG = new PotionTypeBase(PHOTOSYNTHESIS, 20*60, 1, "photosynthesis_strong");
	public PotionTypeBase TYPE_PHOTOSYNTHESIS_LONG = new PotionTypeBase(PHOTOSYNTHESIS, 20*100, 0, "photosynthesis_long");
	
	public PotionBase HURRY = new PotionHurry(false, 9291327);
	public PotionTypeBase TYPE_HURRY_NORMAL = new PotionTypeBase(HURRY, 20*40, 0, "hurry_normal");
	public PotionTypeBase TYPE_HURRY_STRONG = new PotionTypeBase(HURRY, 20*30, 1, "hurry_strong");
	public PotionTypeBase TYPE_HURRY_LONG = new PotionTypeBase(HURRY, 20*60, 0, "hurry_long");
	
	public PotionBase CHEAT_DEATH_POTION = new PotionCheatDeath(false, 16051799);
	public PotionBase CHEAT_DEATH_POTION_ACTIVE = new PotionCheatDeath(true, 0);
	public PotionTypeBase TYPE_CHEAT_DEATH = new PotionTypeBase(CHEAT_DEATH_POTION, 20*60*7, 0, "cheat_death_normal");
	
	public PotionBase CHARGED = new PotionBase(false, 7667860, "charged") {
		@Override
		public boolean isInstant() {
			return true;
		}
	};
	public PotionTypeBase TYPE_CHARGED = new PotionTypeBase(CHARGED, 0, 0, "charged_normal");
	
	public PotionBase CHARGED2 = new PotionBase(false, 16711899, "charged2") {
		@Override
		public boolean isInstant() {
			return true;
		}
	};
	public PotionTypeBase TYPE_CHARGED2 = new PotionTypeBase(CHARGED2, 0, 0, "charged2_normal");
	
	public PotionBase REINCARNATION = new PotionReincarnation(false, 13893582);
	public PotionTypeBase TYPE_REINCARNATION_NORMAL = new PotionTypeBase(REINCARNATION, 20*60*8, 0, "reincarnation_normal");
	public PotionTypeBase TYPE_REINCARNATION_STRONG = new PotionTypeBase(REINCARNATION, 20*60*5, 1, "reincarnation_strong");
	public PotionTypeBase TYPE_REINCARNATION_LONG = new PotionTypeBase(REINCARNATION, 20*60*10, 0, "reincarnation_long");
	
	public PotionBase COMBUSTION = new PotionCombustion(true, 16344321);
	public PotionTypeBase TYPE_COMBUSTION_NORMAL = new PotionTypeBase(COMBUSTION, 20*30, 0, "combustion_normal");
	public PotionTypeBase TYPE_COMBUSTION_STRONG = new PotionTypeBase(COMBUSTION, 20*20, 1, "combustion_strong");
	public PotionTypeBase TYPE_COMBUSTION_LONG = new PotionTypeBase(COMBUSTION, 20*50, 0, "combustion_long");
	
	public PotionBase LEARNING = new PotionLearning(false, 14024505);
	public PotionTypeBase TYPE_LEARNING_NORMAL = new PotionTypeBase(LEARNING, 20*60*4, 0, "learning_normal");
	public PotionTypeBase TYPE_LEARNING_STRONG = new PotionTypeBase(LEARNING, 20*60*2, 1, "learning_strong");
	public PotionTypeBase TYPE_LEARNING_LONG = new PotionTypeBase(LEARNING, 20*60*6, 0, "learning_long");
	
	public PotionBase GRAVITY = new PotionGravity(true, 8477961);
	public PotionTypeBase TYPE_GRAVITY_NORMAL = new PotionTypeBase(GRAVITY, 20*60, 0, "gravity_normal");
	public PotionTypeBase TYPE_GRAVITY_LONG = new PotionTypeBase(GRAVITY, 20*60*2, 0, "gravity_long");
	public PotionTypeBase TYPE_GRAVITY_STRONG = new PotionTypeBase(GRAVITY, 20*30, 1, "gravity_strong");
	
	public PotionBase LEECH = new PotionLeech(false, 4079166);
	public PotionTypeBase TYPE_LEECH_NORMAL = new PotionTypeBase(LEECH, 20*45, 0, "leech_normal");
	public PotionTypeBase TYPE_LEECH_LONG = new PotionTypeBase(LEECH, 20*60, 0, "leech_long");
	public PotionTypeBase TYPE_LEECH_STRONG = new PotionTypeBase(LEECH, 20*20, 1, "leech_strong");
	
	public PotionBase SAILS = new PotionSails(false, 10213631);
	public PotionTypeBase TYPE_SAILS_NORMAL = new PotionTypeBase(SAILS, 20*60*2, 0, "sails_normal");
	public PotionTypeBase TYPE_SAILS_LONG = new PotionTypeBase(SAILS, 20*60*4, 0, "sails_long");
	public PotionTypeBase TYPE_SAILS_STRONG = new PotionTypeBase(SAILS, 20*50, 1, "sails_strong");
	
	public PotionBase BEHEADING = new PotionBeheading(true, 0x8d5f07);
	public PotionTypeBase TYPE_BEHEADING_NORMAL = new PotionTypeBase(BEHEADING, 20*8, 0, "beheading_normal");
	public PotionTypeBase TYPE_BEHEADING_LONG = new PotionTypeBase(BEHEADING, 20*12, 0, "beheading_long");
	public PotionTypeBase TYPE_BEHEADING_STRONG = new PotionTypeBase(BEHEADING, 20*4, 1, "beheading_strong");
	
	public PotionBase DISPEL = new PotionDispel(true, 0x105955);
	public PotionTypeBase TYPE_DISPEL = new PotionTypeBase(DISPEL, 0, 0, "dispel_normal");
	
	public PotionBase PAIN = new PotionPain(true, 0x4d4213);
	public PotionTypeBase TYPE_PAIN_NORMAL = new PotionTypeBase(PAIN, 20*150, 0, "pain_normal");
	public PotionTypeBase TYPE_PAIN_LONG = new PotionTypeBase(PAIN, 20*60*5, 0, "pain_long");
	public PotionTypeBase TYPE_PAIN_STRONG = new PotionTypeBase(PAIN, 20*120, 1, "pain_strong");
}
