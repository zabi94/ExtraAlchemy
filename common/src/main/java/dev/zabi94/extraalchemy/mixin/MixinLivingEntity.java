package dev.zabi94.extraalchemy.mixin;

import java.util.Collection;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.zabi94.extraalchemy.entitydata.EntityProperties;
import dev.zabi94.extraalchemy.mobEffect.ModStatusEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;


@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity implements EntityProperties {
	
	@Shadow private boolean effectsDirty;
	
	protected MixinLivingEntity(EntityType<? extends LivingEntity> type, Level world) {
		super(type, world);
	}
	
	@Inject(at = @At("TAIL"), method = "onEffectsRemoved")
	public void ea_onEffectsRemoved(final Collection<MobEffectInstance> effects, CallbackInfo cb) {
		for (MobEffectInstance effect: effects) {
			if (effect.getEffect().value() instanceof ModStatusEffect mse) {
				mse.onEffectRemoved((LivingEntity) (Object) this);
			}
		}
	}
	
	@Override
	public void markEffectsDirty() {
		effectsDirty = true;
	}
	
}
