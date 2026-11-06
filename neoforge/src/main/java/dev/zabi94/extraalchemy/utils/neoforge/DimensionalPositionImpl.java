package dev.zabi94.extraalchemy.utils.neoforge;

import dev.zabi94.extraalchemy.entitydata.neoforge.ModEntityDataNeoforge;
import dev.zabi94.extraalchemy.utils.DimensionalPosition;
import net.minecraft.world.entity.Entity;

public class DimensionalPositionImpl {

	public static DimensionalPosition getRecallPosition(Entity entity) {
		return entity.getData(ModEntityDataNeoforge.RECALL_POSITION);
	}

	public static void setRecallPosition(Entity entity, DimensionalPosition position) {
		entity.setData(ModEntityDataNeoforge.RECALL_POSITION, position);
	}

	public static void unsetRecallPosition(Entity entity) {
		entity.removeData(ModEntityDataNeoforge.RECALL_POSITION);
	}
    
}
