package zabi.minecraft.extraalchemy.utils;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;

public class DimensionalPosition {
	
	private final double x, y, z;
	private final int dim;
	
	public DimensionalPosition(double x, double y, double z, int dim) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.dim = dim;
	}
	
	public DimensionalPosition(Entity entity) {
		this(entity.getPos().x, entity.getPos().y, entity.getPos().z, entity.getEntityWorld().getDimension().getType().getRawId());
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

	public int getDim() {
		return dim;
	}
	
	public static DimensionalPosition fromTag(CompoundTag tag) {
		return new DimensionalPosition(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z"), tag.getInt("dim"));
	}
	
	public CompoundTag toTag() {
		CompoundTag tag = new CompoundTag();
		tag.putDouble("x", x);
		tag.putDouble("y", y);
		tag.putDouble("z", z);
		tag.putInt("dim", dim);
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
		sb.append(dim);
		sb.append("]");
		return sb.toString();
	}
}
