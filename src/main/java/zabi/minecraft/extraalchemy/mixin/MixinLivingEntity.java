package zabi.minecraft.extraalchemy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;
import zabi.minecraft.extraalchemy.entitydata.EntityProperties;
import zabi.minecraft.extraalchemy.utils.DimensionalPosition;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity implements EntityProperties {
	
	@Shadow private boolean field_6285;
	
	private DimensionalPosition recallPosition = null;

	protected MixinLivingEntity(EntityType<? extends LivingEntity> type, World world) {
		super(type, world);
	}
	
	@Inject(at = @At("TAIL"), method = "readCustomDataFromTag")
	public void readNbt(CompoundTag tag, CallbackInfo cb) {
		if (tag.containsKey("recallPosition")) {
			recallPosition = DimensionalPosition.fromTag(tag.getCompound("recallPosition"));
		}
	}
	
	@Inject(at = @At("TAIL"), method = "writeCustomDataToTag")
	public void writeNbt(CompoundTag tag, CallbackInfo cb) {
		if (recallPosition != null) {
			tag.put("recallPosition", recallPosition.toTag());
		}
	}

	@Override
	public DimensionalPosition getRecallPosition() {
		return recallPosition;
	}

	@Override
	public void setRecallData(DimensionalPosition pos) {
		recallPosition = pos;
	}
	
	@Override
	public void markEffectsDirty() {
		field_6285 = true;
	}

}
