package dev.zabi94.extraalchemy.entitydata;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.player.Player;

public class PlayerProperties {
	
	public static final Codec<PlayerProperties> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.BOOL.fieldOf("magnetismEnabled").forGetter(PlayerProperties::isMagnetismEnabled),
			Codec.FLOAT.fieldOf("xp_reserve").forGetter(PlayerProperties::getXpReserve)
		).apply(instance, PlayerProperties::new));
	
	private boolean magnetismEnabled = true;
	private float xp_reserve = 0f;

	public PlayerProperties(boolean magnetism, float xp) {
		this.magnetismEnabled = magnetism;
		this.xp_reserve = xp;
	}
	
	public boolean isMagnetismEnabled() {
		return magnetismEnabled;
	}

	public void setMagnetismEnabled(boolean magnetismActive) {
		this.magnetismEnabled = magnetismActive;
	}
	
	public float getXpReserve() {
		return xp_reserve;
	}
	
	public int calculateXPDue(float xp) {
		
		int returnable = (int) (xp + xp_reserve);
		float storable = xp + xp_reserve - returnable;
		xp_reserve = storable;
		
		return returnable;
	}

	@ExpectPlatform 
	public static PlayerProperties of(Player player) {
		throw new AssertionError();
	}

}
