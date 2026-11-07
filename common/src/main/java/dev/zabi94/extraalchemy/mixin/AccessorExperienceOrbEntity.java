package dev.zabi94.extraalchemy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.entity.ExperienceOrb;


@Mixin(ExperienceOrb.class)
public interface AccessorExperienceOrbEntity {

	@Accessor("count")
	public void extraalchemy_setAmount(int amt);
	
}
