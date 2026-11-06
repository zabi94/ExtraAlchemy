package dev.zabi94.extraalchemy.entitydata;

import java.util.function.Supplier;

import dev.zabi94.extraalchemy.utils.DimensionalPosition;
import net.neoforged.neoforge.attachment.AttachmentType;

public class ModEntityDataNeoforge {
    
	public static Supplier<AttachmentType<PlayerProperties>> PLAYER_PROPERTIES;
	public static Supplier<AttachmentType<DimensionalPosition>> RECALL_POSITION;
}
