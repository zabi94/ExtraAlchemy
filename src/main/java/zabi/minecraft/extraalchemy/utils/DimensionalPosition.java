package zabi.minecraft.extraalchemy.utils;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class DimensionalPosition {
	
	private final double x, y, z;
	private final Identifier world;
	
	public DimensionalPosition(double x, double y, double z, Identifier world) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.world = world;
	}
	
	public DimensionalPosition(double x, double y, double z, World world) {
		this(x, y, z, world.getRegistryKey().getValue());
	}
	
	public DimensionalPosition(Entity entity) {
		this(entity.getPos().x, entity.getPos().y, entity.getPos().z, entity.getEntityWorld());
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public Identifier getWorldId() {
		return world;
	}
	
	public World getWorld(MinecraftServer server) {
		return server.getWorld(RegistryKey.of(RegistryKeys.WORLD, world));
	}
	
	public static DimensionalPosition fromTag(NbtCompound tag) {
		return new DimensionalPosition(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z"), new Identifier(tag.getString("world")));
	}
	
	public NbtCompound toTag() {
		NbtCompound tag = new NbtCompound();
		tag.putDouble("x", x);
		tag.putDouble("y", y);
		tag.putDouble("z", z);
		tag.putString("world", world.toString());
		return tag;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("DPos[");
		sb.append(x);
		sb.append(", ");
		sb.append(y);
		sb.append(", ");
		sb.append(z);
		sb.append(" @ ");
		sb.append(world);
		sb.append("]");
		return sb.toString();
	}
}
