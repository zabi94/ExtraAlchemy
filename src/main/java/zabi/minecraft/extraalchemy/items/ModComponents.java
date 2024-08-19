package zabi.minecraft.extraalchemy.items;

import com.mojang.serialization.codecs.PrimitiveCodec;

import net.minecraft.component.DataComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import zabi.minecraft.extraalchemy.items.PotionRingItem.PotionRingData;
import zabi.minecraft.extraalchemy.utils.LibMod;

public class ModComponents {

	public static final DataComponentType<Boolean> DISABLED = DataComponentType.<Boolean>builder()
			.codec(PrimitiveCodec.BOOL)
			.packetCodec(PacketCodecs.BOOL)
			.build();

	public static final DataComponentType<PotionRingItem.PotionRingData> POTION_RING_DATA = DataComponentType.<PotionRingItem.PotionRingData>builder()
			.codec(PotionRingData.CODEC)
//			.packetCodec(PacketCodecs.BOOL)
			.build();
	
	public static final DataComponentType<PotionBagItem.SelectionMode> SELECTION_MODE = DataComponentType.<PotionBagItem.SelectionMode>builder()
			.codec(PotionBagItem.SelectionMode.CODEC)
			.build();
	
	public static void register() {
		Registry.register(Registries.DATA_COMPONENT_TYPE, LibMod.id("component_enable"), DISABLED);
		Registry.register(Registries.DATA_COMPONENT_TYPE, LibMod.id("potion_ring_data"), POTION_RING_DATA);
		Registry.register(Registries.DATA_COMPONENT_TYPE, LibMod.id("bag_selection_mode"), SELECTION_MODE);
	}
	
}
