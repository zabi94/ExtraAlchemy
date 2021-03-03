package zabi.minecraft.extraalchemy.compat;

import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosComponent;
import top.theillusivec4.curios.api.SlotTypeInfo.BuildScheme;
import top.theillusivec4.curios.api.SlotTypePreset;
import top.theillusivec4.curios.api.type.component.ICurio;
import zabi.minecraft.extraalchemy.items.ModItems;
import zabi.minecraft.extraalchemy.items.PotionRingItem;
import zabi.minecraft.extraalchemy.utils.Log;

public class CuriosRings {

	public static void registerItemComponentFactories(ItemComponentFactoryRegistry registry) {
		Log.i("Registering CCA compatibility");
		Registry.ITEM.getKey(ModItems.POTION_RING).ifPresent(id -> {
			registry.registerFor(id.getValue(), CuriosComponent.ITEM, stack -> new RingCurio(stack));
			Log.i("Potion ring component registered");
		});
		
	}
	
	public static void init() {
		CuriosApi.enqueueSlotType(BuildScheme.REGISTER, SlotTypePreset.RING.getInfoBuilder().build());
		Log.i("Curios slot type registered");
	}
	
	public static class RingCurio implements ICurio {
		
		private final ItemStack stack;
		
		public RingCurio(ItemStack in) {
			stack = in;
		}
		
		@Override
		public boolean canRightClickEquip() {
			return false;
		}
		
		@Override
		public void curioTick(String identifier, int index, LivingEntity livingEntity) {
			PotionRingItem.onTick(stack, livingEntity.world, livingEntity);
		}
	}

}
