package zabi.minecraft.extraalchemy.mixin.access;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.ExperienceOrbEntity;

@Mixin(ExperienceOrbEntity.class)
public interface AccessorExperienceOrbEntity {

	@Accessor("amount")
	public void extraalchemy_setAmount(int amt);
	
}
