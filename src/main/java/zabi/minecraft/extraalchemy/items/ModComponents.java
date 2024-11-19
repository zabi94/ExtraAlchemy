package zabi.minecraft.extraalchemy.items;

import com.mojang.serialization.codecs.PrimitiveCodec;

import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import zabi.minecraft.extraalchemy.items.PotionRingItem.PotionRingData;
import zabi.minecraft.extraalchemy.utils.LibMod;

public class ModComponents {

	public static final ComponentType<Boolean> DISABLED = ComponentType.<Boolean>builder()
			.codec(PrimitiveCodec.BOOL)
			.packetCodec(PacketCodecs.BOOL)
			.build();

	public static final ComponentType<PotionRingItem.PotionRingData> POTION_RING_DATA = ComponentType.<PotionRingItem.PotionRingData>builder()
			.codec(PotionRingData.CODEC)
//			.packetCodec(PacketCodecs.BOOL)
			.build();
	
	public static final ComponentType<PotionBagItem.SelectionMode> SELECTION_MODE = ComponentType.<PotionBagItem.SelectionMode>builder()
			.codec(PotionBagItem.SelectionMode.CODEC)
			.build();
	
	public static void register() {
		Registry.register(Registries.DATA_COMPONENT_TYPE, LibMod.id("component_enable"), DISABLED);
		Registry.register(Registries.DATA_COMPONENT_TYPE, LibMod.id("potion_ring_data"), POTION_RING_DATA);
		Registry.register(Registries.DATA_COMPONENT_TYPE, LibMod.id("bag_selection_mode"), SELECTION_MODE);
	}
	
}
