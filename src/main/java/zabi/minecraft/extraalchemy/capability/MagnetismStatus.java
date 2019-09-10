package zabi.minecraft.extraalchemy.capability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.extraalchemy.potion.PotionReference;
import zabi.minecraft.extraalchemy.potion.potion.PotionMagnetism;
import zabi.minecraft.minerva.common.capability.SimpleCapability;

public class MagnetismStatus extends SimpleCapability {
	
	@CapabilityInject(MagnetismStatus.class)
	public static final Capability<MagnetismStatus> CAPABILITY = null;
	public static final MagnetismStatus DEFAULT = new MagnetismStatus();
	
	public boolean magnetActive = true;
	
	public void toggle() {
		setActive(!magnetActive);
	}
	
	public void setActive(boolean active) {
		magnetActive = active;
		if (active) {
			markDirty((byte) 1);
		} else {
			markDirty((byte) 2);
		}
	}

	@Override
	public boolean isRelevantFor(Entity object) {
		return object instanceof EntityPlayer;
	}

	@Override
	public SimpleCapability getNewInstance() {
		return new MagnetismStatus();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onSyncMessage(byte mode) {
		if (mode != 0) {
			((PotionMagnetism) PotionReference.INSTANCE.MAGNETISM).setIconActive(mode == 1);
		}
	}
	
	@Override
	public boolean shouldSyncToPlayersAround() {
		return false;
	}

}
