package zabi.minecraft.extraalchemy.entitydata;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import zabi.minecraft.extraalchemy.utils.DimensionalPosition;
import zabi.minecraft.extraalchemy.utils.LibMod;

public class ModEntityData {
	
	public static AttachmentType<PlayerProperties> PLAYER_PROPERTIES;
	public static AttachmentType<DimensionalPosition> RECALL_POSITION;
	
	public static void init() {
		PLAYER_PROPERTIES = AttachmentRegistry.<PlayerProperties>builder()
			.copyOnDeath()
			.initializer(() -> new PlayerProperties(true, 0))
			.persistent(PlayerProperties.CODEC)
			.buildAndRegister(LibMod.id("player_extended_data"));
			
		RECALL_POSITION = AttachmentRegistry.<DimensionalPosition>builder()
			.persistent(DimensionalPosition.CODEC)
			.buildAndRegister(LibMod.id("entity_recall_position"));
		
	}

}
