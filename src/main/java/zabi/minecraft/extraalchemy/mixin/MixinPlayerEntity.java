package zabi.minecraft.extraalchemy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity {

	private boolean magnetismEnabled = true;
	
	protected MixinPlayerEntity(EntityType<? extends LivingEntity> type, World world) {
		super(type, world);
	}
	
	@Inject(at = @At("TAIL"), method = "readCustomDataFromTag")
	public void readNbt(CompoundTag tag, CallbackInfo cb) {
		magnetismEnabled = tag.getBoolean("magnetismEnabled");
	}
	
	@Inject(at = @At("TAIL"), method = "writeCustomDataToTag")
	public void writeNbt(CompoundTag tag, CallbackInfo cb) {
		tag.putBoolean("magnetismEnabled", magnetismEnabled);
	}
	
}
