package zabi.minecraft.extraalchemy.mixin.access;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.vehicle.BoatEntity;

@Mixin(BoatEntity.class)
public interface AccessorBoatEntity {

	@Accessor("pressingForward")
	public boolean extraalchemy_isPressingForward();
	
}
