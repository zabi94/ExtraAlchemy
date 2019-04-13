package zabi.minecraft.extraalchemy;

import net.fabricmc.api.ModInitializer;
import zabi.minecraft.extraalchemy.config.ModConfig;
import zabi.minecraft.extraalchemy.statuseffect.ModEffectRegistry;
import zabi.minecraft.extraalchemy.utils.LibMod;
import zabi.minecraft.extraalchemy.utils.Log;

public class ExtraAlchemy implements ModInitializer {

	@Override
	public void onInitialize() {
		Log.i(LibMod.MOD_ID+" started!");
		ModConfig.init();
		ModEffectRegistry.init();
	}

}
