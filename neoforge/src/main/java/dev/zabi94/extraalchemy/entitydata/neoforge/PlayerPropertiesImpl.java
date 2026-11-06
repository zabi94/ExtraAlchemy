package dev.zabi94.extraalchemy.entitydata.neoforge;

import java.util.Objects;

import dev.zabi94.extraalchemy.entitydata.PlayerProperties;
import net.minecraft.world.entity.player.Player;

public class PlayerPropertiesImpl extends PlayerProperties {

	public PlayerPropertiesImpl(boolean magnetism, float xp) {
        super(magnetism, xp);
    }


	public static PlayerProperties of(Player player) {
		if (Objects.nonNull(player)) {
			return player.getData(ModEntityDataNeoforge.PLAYER_PROPERTIES);
		} else {
			throw new NullPointerException("PlayerProperties can't be read from null players");
		}
	}
    
}
