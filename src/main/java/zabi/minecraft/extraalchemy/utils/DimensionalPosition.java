package zabi.minecraft.extraalchemy.utils;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.entity.Entity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class DimensionalPosition {
	
	public static final Codec<DimensionalPosition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Vec3d.CODEC.fieldOf("pos").forGetter(DimensionalPosition::getPos),
			Identifier.CODEC.fieldOf("world").forGetter(DimensionalPosition::getWorldId)
		).apply(instance, DimensionalPosition::new));
	
	private final Vec3d pos;
	private final Identifier world;
	
	public DimensionalPosition(Vec3d pos, Identifier world) {
		this.pos = pos;
		this.world = world;
	}
	
	public DimensionalPosition(double x, double y, double z, Identifier world) {
		this.pos = new Vec3d(x,y,z);
		this.world = world;
	}
	
	public DimensionalPosition(double x, double y, double z, World world) {
		this(x, y, z, world.getRegistryKey().getValue());
	}
	
	public DimensionalPosition(Entity entity) {
		this(entity.getPos().x, entity.getPos().y, entity.getPos().z, entity.getEntityWorld());
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
	
	public Vec3d getPos() {
		return pos;
	}

	public Identifier getWorldId() {
		return world;
	}
	
	public World getWorld(MinecraftServer server) {
		return server.getWorld(RegistryKey.of(RegistryKeys.WORLD, world));
	}
}
