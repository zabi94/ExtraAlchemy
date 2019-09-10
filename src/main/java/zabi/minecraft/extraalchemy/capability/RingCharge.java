package zabi.minecraft.extraalchemy.capability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import zabi.minecraft.minerva.common.capability.SimpleCapability;

public class RingCharge extends SimpleCapability {
	
	@CapabilityInject(RingCharge.class)
	public static final Capability<RingCharge> CAPABILITY = null;

	public static final RingCharge DEFAULT = new RingCharge();
	
	public int charges = 0;

	@Override
	public SimpleCapability getNewInstance() {
		return new RingCharge();
	}

	@Override
	public boolean isRelevantFor(Entity entity) {
		return entity instanceof EntityPlayer;
	}

}
