package dev.zabi94.extraalchemy.utils.fabric;

import dev.zabi94.extraalchemy.entitydata.ModEntityDataFabric;
import dev.zabi94.extraalchemy.utils.DimensionalPosition;
import net.minecraft.world.entity.Entity;

public class DimensionalPositionImpl {

	public static DimensionalPosition getRecallPosition(Entity entity) {
        return entity.getAttachedOrElse(ModEntityDataFabric.RECALL_POSITION, null);
	}

	public static void setRecallPosition(Entity entity, DimensionalPosition position) {
        entity.setAttached(ModEntityDataFabric.RECALL_POSITION, position);
	}

	public static void unsetRecallPosition(Entity entity) {
        entity.removeAttached(ModEntityDataFabric.RECALL_POSITION);
	}
    
}
