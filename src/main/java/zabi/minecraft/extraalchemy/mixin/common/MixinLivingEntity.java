package zabi.minecraft.extraalchemy.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import zabi.minecraft.extraalchemy.entitydata.EntityProperties;
import zabi.minecraft.extraalchemy.statuseffect.ModStatusEffect;
import zabi.minecraft.extraalchemy.utils.DimensionalPosition;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity implements EntityProperties {
	
	@Shadow private boolean effectsChanged;
	
	private DimensionalPosition recallPosition = null;

	protected MixinLivingEntity(EntityType<? extends LivingEntity> type, World world) {
		super(type, world);
	}
	
	@Inject(at = @At("TAIL"), method = "readCustomDataFromNbt")
	public void readNbt(NbtCompound tag, CallbackInfo cb) {
		if (tag.contains("recallPosition")) {
			recallPosition = DimensionalPosition.fromTag(tag.getCompound("recallPosition"));
		}
	}
	
	@Inject(at = @At("TAIL"), method = "writeCustomDataToNbt")
	public void writeNbt(NbtCompound tag, CallbackInfo cb) {
		if (recallPosition != null) {
			tag.put("recallPosition", recallPosition.toTag());
		}
	}
	
	@Inject(at = @At("TAIL"), method = "onStatusEffectRemoved")
	public void ea_onStatusEffectRemoved(StatusEffectInstance effect, CallbackInfo cb) {
		if (effect.getEffectType() instanceof ModStatusEffect mse) {
			mse.onEffectRemoved((LivingEntity) (Object) this);
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
		effectsChanged = true;
	}
	
}
