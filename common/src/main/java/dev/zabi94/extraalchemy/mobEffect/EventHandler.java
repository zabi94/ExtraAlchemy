package dev.zabi94.extraalchemy.mobEffect;

import dev.architectury.event.events.common.EntityEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class EventHandler {
    
    public static void register() {
        EntityEvent.LIVING_DAMAGE_POST.register((entity,damageSource,originalDamage,amount,blocked) -> {

            if (blocked) return;

            MobEffectInstance sei = entity.getEffect(ModEffectRegistry.pacifism.asHolder());
            if (sei != null && damageSource.getEntity() instanceof LivingEntity attacker && !damageSource.isCreativePlayer()) {
                MobEffectInstance slowness = new MobEffectInstance(MobEffects.SLOWNESS, 200, sei.getAmplifier(), false, true, true);
                MobEffectInstance weakness = new MobEffectInstance(MobEffects.WEAKNESS, 200, sei.getAmplifier(), false, true, true);
                attacker.addEffect(slowness);
                attacker.addEffect(weakness);
            }
        });
    }

}
