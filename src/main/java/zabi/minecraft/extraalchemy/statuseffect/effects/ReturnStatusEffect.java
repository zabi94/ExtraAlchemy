package zabi.minecraft.extraalchemy.statuseffect.effects;

import java.util.Optional;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import zabi.minecraft.extraalchemy.statuseffect.ModStatusEffect;

public class ReturnStatusEffect extends ModStatusEffect {

	public ReturnStatusEffect(StatusEffectCategory type, int color, boolean isInstant) {
		super(type, color, isInstant);
	}

	@Override
	public void applyInstantEffect(Entity source, Entity attacker, LivingEntity target, int amplifier, double d) {
		if (target instanceof ServerPlayerEntity player) {
			BlockPos respawnPos = player.getSpawnPointPosition();
			
			if (respawnPos == null) return;
			
			Optional<ServerPlayerEntity.RespawnPos> pos = ServerPlayerEntity.findRespawnPosition(player.getServerWorld(), respawnPos, 0, false, true);
			if (pos.isPresent()) {
				target.getWorldSpawnPos(player.getServerWorld(), respawnPos);
				TeleportTarget teleportTarget = new TeleportTarget(player.getServerWorld(), Vec3d.of(respawnPos), Vec3d.ZERO, player.getYaw(), player.getPitch(), TeleportTarget.NO_OP);
				player.teleportTo(teleportTarget);
			}
		}
	}

}
