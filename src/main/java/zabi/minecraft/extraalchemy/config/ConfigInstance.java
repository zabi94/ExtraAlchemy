package zabi.minecraft.extraalchemy.config;

public class ConfigInstance {

	public boolean removeInventoryPotionShift;
	public Potions potions;
	
	public ConfigInstance() {
		removeInventoryPotionShift = true;
		potions = new Potions();
	}
	
	
	public class Potions {
		
		public boolean magnetism = true;
		public boolean photosynthesis = true;
		
	}
	
}
