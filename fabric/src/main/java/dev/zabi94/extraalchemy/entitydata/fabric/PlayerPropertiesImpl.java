package dev.zabi94.extraalchemy.entitydata.fabric;

import java.util.Objects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import dev.zabi94.extraalchemy.entitydata.ModEntityDataFabric;
import dev.zabi94.extraalchemy.entitydata.PlayerProperties;
import net.minecraft.world.entity.player.Player;

public class PlayerPropertiesImpl extends PlayerProperties {

	public PlayerPropertiesImpl(boolean magnetism, float xp) {
        super(magnetism, xp);
    }
	
	public static final Codec<PlayerProperties> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.BOOL.fieldOf("magnetismEnabled").forGetter(PlayerProperties::isMagnetismEnabled),
			Codec.FLOAT.fieldOf("xp_reserve").forGetter(PlayerProperties::getXpReserve)
		).apply(instance, PlayerPropertiesImpl::new));


	public static PlayerProperties of(Player player) {
		if (Objects.nonNull(player)) {
			return player.getAttachedOrCreate(ModEntityDataFabric.PLAYER_PROPERTIES);
		} else {
			throw new NullPointerException("PlayerProperties can't be read from null players");
		}
	}
    
}
