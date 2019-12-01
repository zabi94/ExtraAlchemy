package zabi.minecraft.extraalchemy.config;

public class ConfigInstance {

	public boolean removeInventoryPotionShift;
	public boolean learningIncreasesExpOrbValue;
	public Potions potions;
	
	public ConfigInstance() {
		removeInventoryPotionShift = true;
		learningIncreasesExpOrbValue = true;
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
		
	}
	
}
