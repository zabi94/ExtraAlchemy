package dev.zabi94.extraalchemy;

import dev.zabi94.extraalchemy.config.ModConfig;
import dev.zabi94.extraalchemy.entitydata.ModEntityData;
import dev.zabi94.extraalchemy.mobEffect.ModEffectRegistry;
import dev.zabi94.extraalchemy.potion.ModPotionRegistry;

public final class ExtraAlchemy {

    public static void init() {
        ModConfig.init();
        ModEffectRegistry.registerAll();
        ModPotionRegistry.registerAll();
        ModEntityData.init();
    }
}
