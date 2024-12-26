package zabi.minecraft.extraalchemy.entitydata;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import zabi.minecraft.extraalchemy.utils.DimensionalPosition;
import zabi.minecraft.extraalchemy.utils.LibMod;

public class ModEntityData {
	
	public static AttachmentType<PlayerProperties> PLAYER_PROPERTIES;
	public static AttachmentType<DimensionalPosition> RECALL_POSITION;
	
	public static void init() {
		
		PLAYER_PROPERTIES = AttachmentRegistry.create(LibMod.id("player_extended_data"), builder -> {
			builder.copyOnDeath()
				.initializer(() -> new PlayerProperties(true, 0))
				.persistent(PlayerProperties.CODEC);
		});
			
		RECALL_POSITION = AttachmentRegistry.<DimensionalPosition>create(LibMod.id("entity_recall_position"), builder -> {
			builder.persistent(DimensionalPosition.CODEC);
		});
		
	}

}
