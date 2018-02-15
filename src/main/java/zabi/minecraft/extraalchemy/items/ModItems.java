package zabi.minecraft.extraalchemy.items;

import java.util.ArrayList;
import java.util.List;

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
import zabi.minecraft.extraalchemy.ModConfig;
import zabi.minecraft.extraalchemy.lib.Reference;

@Mod.EventBusSubscriber
@ObjectHolder(Reference.MID)
public class ModItems {
	
	static List<Item> items = new ArrayList<Item>(4);

	public static final Item breakable_potion = null;
	public static final Item supporter_medal = null;
	public static final Item vial_break = null;
	public static final Item potion_bag = null;
	public static final Item modified_potion = null;
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onModelRegistration(ModelRegistryEvent evt) {
		items.add(breakable_potion);
		items.add(vial_break);
		items.add(potion_bag);
		items.add(modified_potion);
		for (Item i:items) {
			ExtraAlchemy.proxy.registerItemModel(i);
		}
	}
	
	@SubscribeEvent
	public static void onItemRegistration(RegistryEvent.Register<Item> evt) {
		IForgeRegistry<Item> reg = evt.getRegistry();
		if (ModConfig.options.useNewVials) reg.register(new ItemBreakablePotionNew());
		else reg.register(new ItemBreakablePotion());
		reg.registerAll(
				new ItemVial(),
				new ItemPotionBag(),
				new ItemModifiedPotion()
				);
	}
	
}
