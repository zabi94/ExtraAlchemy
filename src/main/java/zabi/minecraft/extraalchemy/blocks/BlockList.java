package zabi.minecraft.extraalchemy.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import zabi.minecraft.extraalchemy.lib.Reference;

@Mod.EventBusSubscriber
public class BlockList {	
	
	public static final BlockEncasingIce ENCASING_ICE = new BlockEncasingIce(Material.ICE);
	
	@SubscribeEvent
	public static void register(RegistryEvent.Register<Block> evt) {
		ENCASING_ICE.setRegistryName(Reference.MID, "encasing_ice");
		evt.getRegistry().register(ENCASING_ICE);
	}

}
