package zabi.minecraft.extraalchemy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.player.PlayerEntity;

@Mixin(PlayerEntity.class)
public interface PlayerProperties {
	
	@Accessor("magnetismEnabled")
	public abstract boolean isMagnetismEnabled();

	@Accessor("magnetismEnabled")
	public abstract void setMagnetismEnabled(boolean magnetismActive);
}
