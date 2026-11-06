package dev.zabi94.extraalchemy.utils;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class DimensionalPosition {
	
	public static final Codec<DimensionalPosition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Vec3.CODEC.fieldOf("pos").forGetter(DimensionalPosition::getPos),
			Identifier.CODEC.fieldOf("world").forGetter(DimensionalPosition::getWorldId)
		).apply(instance, DimensionalPosition::new));
	
	private final Vec3 pos;
	private final Identifier world;
	
	public DimensionalPosition(Vec3 pos, Identifier world) {
		this.pos = pos;
		this.world = world;
	}
	
	public DimensionalPosition(double x, double y, double z, Identifier world) {
		this.pos = new Vec3(x,y,z);
		this.world = world;
	}
	
	public DimensionalPosition(double x, double y, double z, Level world) {
		this(x, y, z, world.dimension().identifier());
	}
	
	public DimensionalPosition(Entity entity) {
		this(entity.getX(), entity.getY(), entity.getZ(), entity.level());
	}

	public double getX() {
		return pos.x;
	}

	public double getY() {
		return pos.y;
	}

	public double getZ() {
		return pos.z;
	}
	
	public Vec3 getPos() {
		return pos;
	}

	public Identifier getWorldId() {
		return world;
	}
	
	public Level getWorld(MinecraftServer server) {
		return server.getLevel(ResourceKey.create(Registries.DIMENSION, world));
	}

	@ExpectPlatform 
	public static DimensionalPosition getRecallPosition(Entity entity) {
		throw new AssertionError();
	}

	@ExpectPlatform 
	public static void setRecallPosition(Entity entity, DimensionalPosition position) {
		throw new AssertionError();
	}

	@ExpectPlatform 
	public static void unsetRecallPosition(Entity entity) {
		throw new AssertionError();
	}
}
