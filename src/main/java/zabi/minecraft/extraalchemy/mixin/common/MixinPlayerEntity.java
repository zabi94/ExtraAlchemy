package zabi.minecraft.extraalchemy.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import zabi.minecraft.extraalchemy.entitydata.DataTrackers;
import zabi.minecraft.extraalchemy.entitydata.PlayerProperties;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity implements PlayerProperties {

	protected MixinPlayerEntity(EntityType<? extends LivingEntity> type, World world) {
		super(type, world);
	}
	
	@Inject(at = @At("TAIL"), method = "readCustomDataFromNbt")
	public void readNbt(NbtCompound tag, CallbackInfo cb) {
		dataTracker.set(DataTrackers.MAGNET_TRACKER, tag.getBoolean("magnetismEnabled"));
	}
	
	@Inject(at = @At("TAIL"), method = "writeCustomDataToNbt")
	public void writeNbt(NbtCompound tag, CallbackInfo cb) {
		tag.putBoolean("magnetismEnabled", dataTracker.get(DataTrackers.MAGNET_TRACKER));
	}
	
	@Inject(at = @At("TAIL"), method = "initDataTracker")
	public void initTracker(CallbackInfo cb) {
		this.dataTracker.startTracking(DataTrackers.MAGNET_TRACKER, true);
	}
	
	@Override
	public boolean isMagnetismEnabled() {
		return dataTracker.get(DataTrackers.MAGNET_TRACKER);
	}
	
	@Override
	public void setMagnetismEnabled(boolean magnetismActive) {
		dataTracker.set(DataTrackers.MAGNET_TRACKER, magnetismActive);
	}
	
}
