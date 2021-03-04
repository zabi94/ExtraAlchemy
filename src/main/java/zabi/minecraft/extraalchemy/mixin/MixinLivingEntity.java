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
	
	@Shadow private boolean effectsChanged;
	
	private DimensionalPosition recallPosition = null;
	
	private int last_growth_level = 0;
	private int last_shrink_level = 0;

	protected MixinLivingEntity(EntityType<? extends LivingEntity> type, World world) {
		super(type, world);
	}
	
	@Inject(at = @At("TAIL"), method = "readCustomDataFromTag")
	public void readNbt(CompoundTag tag, CallbackInfo cb) {
		if (tag.contains("recallPosition")) {
			recallPosition = DimensionalPosition.fromTag(tag.getCompound("recallPosition"));
		}
		if (tag.contains("last_growth_level")) {
			last_growth_level = tag.getInt("last_growth_level");
		}
		if (tag.contains("last_shrink_level")) {
			last_shrink_level = tag.getInt("last_shrink_level");
		}
	}
	
	@Inject(at = @At("TAIL"), method = "writeCustomDataToTag")
	public void writeNbt(CompoundTag tag, CallbackInfo cb) {
		if (recallPosition != null) {
			tag.put("recallPosition", recallPosition.toTag());
		}
		tag.putInt("last_growth_level", last_growth_level);
		tag.putInt("last_shrink_level", last_shrink_level);
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
	
	@Override
	public int getLastGrowthAmplifier() {
		return last_growth_level;
	}
	
	@Override
	public void setLastGrowthAmplifier(int mult) {
		last_growth_level = mult;
	}
	
	@Override
	public int getLastShrinkAmplifier() {
		return last_shrink_level;
	}

	@Override
	public void setLastShrinkAmplifier(int mult) {
		last_shrink_level = mult;
	}
}
