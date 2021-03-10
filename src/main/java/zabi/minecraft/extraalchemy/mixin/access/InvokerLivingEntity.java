package zabi.minecraft.extraalchemy.mixin.access;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

@Mixin(LivingEntity.class)
public interface InvokerLivingEntity {

	@Environment(EnvType.CLIENT)
	@Invoker("spawnItemParticles")
	public abstract void spawnParticles(ItemStack stack, int count);
	
}
