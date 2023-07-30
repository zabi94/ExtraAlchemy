package zabi.minecraft.extraalchemy.statuseffect.effects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import zabi.minecraft.extraalchemy.config.ModConfig;
import zabi.minecraft.extraalchemy.statuseffect.ModStatusEffect;

public class ReturnStatusEffect extends ModStatusEffect {

	public ReturnStatusEffect(StatusEffectCategory type, int color, boolean isInstant) {
		super(type, color, isInstant);
	}

	@Override
	public void applyInstantEffect(Entity source, Entity attacker, LivingEntity target, int amplifier, double d) {
		if (target instanceof ServerPlayerEntity) {
			ServerPlayerEntity player = (ServerPlayerEntity) target;
			BlockPos respawnPos = player.getSpawnPointPosition();
			if (respawnPos != null) {
				PlayerEntity.findRespawnPosition((ServerWorld) target.getEntityWorld(), respawnPos, player.getYaw(), player.isSpawnForced(), !ModConfig.INSTANCE.useAnchorChargesWithReturnPotion).ifPresent(v3d -> {
					player.requestTeleport(v3d.x, v3d.y, v3d.z);
				});
			}
		}
	}

}
