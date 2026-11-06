package dev.zabi94.extraalchemy.neoforge;

import net.neoforged.fml.common.Mod;
import dev.zabi94.extraalchemy.ExtraAlchemy;
import dev.zabi94.extraalchemy.utils.LibMod;

@Mod(LibMod.MOD_ID)
public final class ExtraAlchemyNeoForge {
    public ExtraAlchemyNeoForge() {
        ExtraAlchemy.init();
    }
}
