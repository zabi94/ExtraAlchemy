package zabi.minecraft.extraalchemy.integration;

import java.util.HashMap;

import net.minecraftforge.fml.common.Loader;

public class ModIDs {
	public static final String botania = "Botania";
	public static final String jei = "JEI";
	
	private static final HashMap<String,String> modNames = new HashMap<String,String>();
	
	static {
		modNames.put("extrautils2", "Extra Utilities 2");
		modNames.put("botania", "Botania");
	}
	
	public static String getModName(String modid) {
		if (modNames.containsKey(modid)) return modNames.get(modid);
		if (Loader.isModLoaded(modid)) return Loader.instance().getIndexedModList().get(modid).getName();
		return modid;
	}
}
