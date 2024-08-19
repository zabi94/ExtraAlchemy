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
import net.minecraft.world.World;
import zabi.minecraft.extraalchemy.entitydata.EntityProperties;
import zabi.minecraft.extraalchemy.statuseffect.ModStatusEffect;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity implements EntityProperties {
	
	@Shadow private boolean effectsChanged;
	
	protected MixinLivingEntity(EntityType<? extends LivingEntity> type, World world) {
		super(type, world);
	}
	
	@Inject(at = @At("TAIL"), method = "onStatusEffectRemoved")
	public void ea_onStatusEffectRemoved(StatusEffectInstance effect, CallbackInfo cb) {
		if (effect.getEffectType() instanceof ModStatusEffect mse) {
			mse.onEffectRemoved((LivingEntity) (Object) this);
		}
	}
	
	@Override
	public void markEffectsDirty() {
		effectsChanged = true;
	}
	
}
