package dev.zabi94.extraalchemy.mobEffect.effects;

import dev.zabi94.extraalchemy.mobEffect.ModStatusEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayer.RespawnConfig;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;

public class ReturnStatusEffect extends ModStatusEffect {

	public ReturnStatusEffect(MobEffectCategory type, int color, boolean isInstant) {
		super(type, color, isInstant);
	}

	@Override
	public void applyInstantaneousEffect(ServerLevel world, Entity effectEntity, Entity attacker, LivingEntity target, int amplifier, double proximity) {
		if (!(target instanceof ServerPlayer player)) return;
		
		RespawnConfig respawn = player.getRespawnConfig();
		if (respawn == null) return;
		
		BlockPos respawnPos = respawn.respawnData().pos();
		respawnPos = target.adjustSpawnLocation(player.level(), respawnPos);
		TeleportTransition teleportTarget = new TeleportTransition(player.level(), Vec3.atLowerCornerOf(respawnPos), Vec3.ZERO, player.getYRot(), player.getXRot(), TeleportTransition.DO_NOTHING);
		player.teleport(teleportTarget);
	}

}
