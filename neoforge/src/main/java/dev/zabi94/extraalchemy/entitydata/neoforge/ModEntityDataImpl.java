package dev.zabi94.extraalchemy.entitydata.neoforge;

import dev.architectury.platform.hooks.EventBusesHooks;
import dev.zabi94.extraalchemy.entitydata.PlayerProperties;
import dev.zabi94.extraalchemy.utils.DimensionalPosition;
import dev.zabi94.extraalchemy.utils.LibMod;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModEntityDataImpl {

	private static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, LibMod.MOD_ID);
	
	public static void init() {
		
		ModEntityDataNeoforge.RECALL_POSITION = ATTACHMENTS.register(
			"entity_recall_position",
			() -> AttachmentType.builder(() -> new DimensionalPosition(null, null))
				.build()
		);

		ModEntityDataNeoforge.PLAYER_PROPERTIES = ATTACHMENTS.register(
			"player_extended_data",
			() -> AttachmentType.builder(() -> new PlayerProperties(true, 0))
				.serialize(PlayerProperties.CODEC.fieldOf("player_properties"))
				.build()
		);

		ATTACHMENTS.register(EventBusesHooks.getModEventBus(LibMod.MOD_ID).orElseThrow());
		
	}
    
}
