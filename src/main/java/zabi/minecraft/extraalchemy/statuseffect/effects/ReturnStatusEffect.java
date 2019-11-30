package zabi.minecraft.extraalchemy.statuseffect.effects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import zabi.minecraft.extraalchemy.statuseffect.ModStatusEffect;

public class ReturnStatusEffect extends ModStatusEffect {

	public ReturnStatusEffect(StatusEffectType type, int color, boolean isInstant) {
		super(type, color, isInstant);
	}

	@Override
	public void applyInstantEffect(Entity source, Entity attacker, LivingEntity target, int amplifier, double d) {
		if (target instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) target;
			BlockPos respawnPos = player.getSpawnPosition();
			if (respawnPos != null) {
				PlayerEntity.method_7288(target.world, respawnPos, true).ifPresent(v3d -> {
					player.requestTeleport(v3d.x, v3d.y, v3d.z);
				});
			}
		}
	}

}
