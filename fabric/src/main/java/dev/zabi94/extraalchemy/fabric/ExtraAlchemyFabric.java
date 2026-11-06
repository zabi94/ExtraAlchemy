package dev.zabi94.extraalchemy.fabric;

import dev.zabi94.extraalchemy.ExtraAlchemy;
import net.fabricmc.api.ModInitializer;


public final class ExtraAlchemyFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ExtraAlchemy.init();
    }
}
