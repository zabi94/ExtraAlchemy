package zabi.minecraft.extraalchemy.compat;

import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class CCACompatBridge implements ItemComponentInitializer {
	
	@Override
	public void registerItemComponentFactories(ItemComponentFactoryRegistry registry) {
		if (FabricLoader.getInstance().isModLoaded("curios")) {
			CuriosRings.registerItemComponentFactories(registry);
		}
	}

}
