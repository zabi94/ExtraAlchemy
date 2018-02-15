package zabi.minecraft.extraalchemy.integration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.Loader;
import zabi.minecraft.extraalchemy.ModConfig;
import zabi.minecraft.extraalchemy.lib.Log;

public class BotaniaHandler {
	
	private static Method hasSolegnolia;
	public static String botaniaID = null;
	
//	public static Potion soulCross, featherFeet, emptiness, bloodthirst, allure, clear;
//	public static PotionTypeCompat pt_soulCross, pt_featherFeet, pt_emptiness, pt_bloodthirst, pt_allure, pt_clear;
	
	public static void checkLoadBotania() {
		if (Loader.isModLoaded(ModIDs.botania)) botaniaID = ModIDs.botania;
		else if (Loader.isModLoaded(ModIDs.botania.toLowerCase())) botaniaID = ModIDs.botania.toLowerCase();
		if (botaniaID!=null) {
			Log.i("Botania detected, loaiding compatibility");
			loadSolegnoliaCompat();
//			loadBotaniaPotions();
		}
	}
	

//	private static void loadBotaniaPotions() {
//		if (Config.loadBotaniaPotions.getBoolean()) {
//			try {
//				soulCross = Potion.REGISTRY.getObject(new ResourceLocation(botaniaID, "soulCross"));
//				featherFeet = Potion.REGISTRY.getObject(new ResourceLocation(botaniaID, "featherFeet"));
//				emptiness = Potion.REGISTRY.getObject(new ResourceLocation(botaniaID, "emptiness"));
//				bloodthirst = Potion.REGISTRY.getObject(new ResourceLocation(botaniaID, "bloodthirst"));
//				allure = Potion.REGISTRY.getObject(new ResourceLocation(botaniaID, "allure"));
//				clear = Potion.REGISTRY.getObject(new ResourceLocation(botaniaID, "clear"));
//				
//				pt_soulCross = new PotionTypeCompat(soulCross, 1800, 0, "soulCross", "Botania");
//				pt_featherFeet = new PotionTypeCompat(featherFeet, 1800, 0, "featherFeet", "Botania");
//				pt_emptiness = new PotionTypeCompat(emptiness, 7200, 0, "emptiness", "Botania");
//				pt_bloodthirst = new PotionTypeCompat(bloodthirst, 7200, 0, "bloodthirst", "Botania");
//				pt_allure = new PotionTypeCompat(allure, 4800, 0, "allure", "Botania");
//				pt_clear = new PotionTypeCompat(clear, 0, 0, "clear", "Botania");
//				
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}


	public static boolean isSolegnoliaAround(Entity i) {
		try {
			if (hasSolegnolia != null && (boolean) hasSolegnolia.invoke(null, i)) return true;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			Log.e(e1);
			Log.w("Failed to check for solegnolias around. Removing integration completely");
			hasSolegnolia=null;
		}
		return false;
	}
	
	private static void loadSolegnoliaCompat() {
		if (ModConfig.options.respectSolegnolias) try {
			Log.i("Loading Botania Solegnolia integration");
			hasSolegnolia = Class.forName("vazkii.botania.common.block.subtile.functional.SubTileSolegnolia").getMethod("hasSolegnoliaAround", Entity.class);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException e) {
			Log.w("Failed to load Solegnolia integration");
			hasSolegnolia=null;
		}
	}
	
}
