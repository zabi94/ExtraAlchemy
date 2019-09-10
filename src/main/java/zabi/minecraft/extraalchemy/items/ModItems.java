package zabi.minecraft.extraalchemy.items;

import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import zabi.minecraft.extraalchemy.ExtraAlchemy;
import zabi.minecraft.extraalchemy.lib.Reference;

@Mod.EventBusSubscriber
@ObjectHolder(Reference.MID)
public class ModItems {
	public static final Item breakable_potion = null;
	public static final Item vial_break = null;
	public static final Item potion_bag = null;
	public static final Item modified_potion = null;
	public static final Item potion_ring = null;
	public static final Item empty_ring = null;
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onModelRegistration(ModelRegistryEvent evt) {
		ExtraAlchemy.proxy.registerItemModel(modified_potion);
		ExtraAlchemy.proxy.registerItemModel(potion_bag);
		ExtraAlchemy.proxy.registerItemModel(vial_break);
		ExtraAlchemy.proxy.registerItemModel(breakable_potion);
		ExtraAlchemy.proxy.registerItemModel(potion_ring);
		ExtraAlchemy.proxy.registerItemModel(empty_ring);
	}
	
	@SubscribeEvent
	public static void onItemRegistration(RegistryEvent.Register<Item> evt) {
		IForgeRegistry<Item> reg = evt.getRegistry();
		reg.register(new ItemPotionVial());
		reg.register(new ItemEmptyPotionVial());
		reg.register(new ItemPotionBag());
		reg.register(new ItemModifiedDrinkablePotion());
		reg.register(new ItemPotionRing());
		reg.register(new ItemEmptyPotionRing());
	}
	
}
