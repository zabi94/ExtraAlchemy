package zabi.minecraft.extraalchemy.config;

public class ConfigInstance {

	public boolean removeInventoryPotionShift;
	public boolean learningIncreasesExpOrbValue;
	public boolean enableVials;
	public boolean enableRings;
	public boolean enableBrewingStandFire;
	public boolean useAnchorChargesWithReturnPotion;
	public boolean allowRingsInInventoryWithThirdPartyMods;
	
	public Potions potions;
	
	public ConfigInstance() {
		removeInventoryPotionShift = true;
		learningIncreasesExpOrbValue = true;
		enableVials = true;
		enableRings = true;
		enableBrewingStandFire = true;
		useAnchorChargesWithReturnPotion = true;
		allowRingsInInventoryWithThirdPartyMods = false;
		potions = new Potions();
	}
	
	
	public class Potions {
		
		public boolean magnetism = true;
		public boolean photosynthesis = true;
		public boolean crumbling = true;
		public boolean fuse = true;
		public boolean recall = true;
		public boolean sails = true;
		public boolean returning = true;
		public boolean learning = true;
		public boolean concentration = true;
		public boolean gravity = true;
		public boolean combustion = true;
		public boolean pacifism = true;
		public boolean shrinking = true;
		public boolean growing = true;
		public boolean detection = true;
		public boolean piper = true;
	}
	
}
