package zabi.minecraft.extraalchemy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.ExperienceOrbEntity;

@Mixin(ExperienceOrbEntity.class)
public interface AccessorExperienceOrbEntity {

	@Accessor("amount")
	public void setAmount(int amt);
	
}
