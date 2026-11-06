package dev.zabi94.extraalchemy.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.entity.vehicle.boat.AbstractBoat;


@Mixin(AbstractBoat.class)
public interface AccessorBoatEntity {

	@Accessor("inputUp")
	public boolean extraalchemy_isPressingForward();
	
}
