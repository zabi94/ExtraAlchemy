package dev.zabi94.extraalchemy.entitydata.fabric;

import dev.zabi94.extraalchemy.entitydata.ModEntityDataFabric;
import dev.zabi94.extraalchemy.entitydata.PlayerProperties;
import dev.zabi94.extraalchemy.utils.DimensionalPosition;
import dev.zabi94.extraalchemy.utils.LibMod;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;

public class ModEntityDataImpl {
	
	public static void init() {
		
		ModEntityDataFabric.PLAYER_PROPERTIES = AttachmentRegistry.create(LibMod.id("player_extended_data"), builder -> {
			builder.copyOnDeath()
				.initializer(() -> new PlayerProperties(true, 0))
				.persistent(PlayerPropertiesImpl.CODEC);
		});
			
		ModEntityDataFabric.RECALL_POSITION = AttachmentRegistry.<DimensionalPosition>create(LibMod.id("entity_recall_position"), builder -> {
			builder.persistent(DimensionalPosition.CODEC);
		});
		
	}
    
}
