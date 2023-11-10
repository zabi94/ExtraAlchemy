package zabi.minecraft.extraalchemy.items;

import java.util.Iterator;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroup.DisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import zabi.minecraft.extraalchemy.config.ModConfig;
import zabi.minecraft.extraalchemy.crafting.PotionRingRecipe;
import zabi.minecraft.extraalchemy.utils.LibMod;
import zabi.minecraft.extraalchemy.utils.Log;
import zabi.minecraft.extraalchemy.utils.proxy.SidedProxy;

public class ItemSettings {
	
	public static ItemGroup EXTRA_ALCHEMY_GROUP;// = FabricItemGroupBuilder.create().icon().build();
	
	public static void init() {
		EXTRA_ALCHEMY_GROUP = FabricItemGroup.builder()
				.displayName(Text.translatable("itemGroup.extraalchemy.all"))
				.icon(() -> new ItemStack(ModItems.POTION_BAG))
				.entries(ItemSettings::getDefaultGroupItems)
				.build();
		
		Registry.register(Registries.ITEM_GROUP, LibMod.id("all"), EXTRA_ALCHEMY_GROUP);
	}
	
	private static void getDefaultGroupItems(DisplayContext ctx, ItemGroup.Entries entries) {
		entries.add(ModItems.EMPTY_RING);
		entries.add(ModItems.EMPTY_VIAL);
		entries.add(ModItems.POTION_BAG);
		
		//Adding filled ring recipes
		if (ModConfig.INSTANCE.enableRings) {
			
			SidedProxy.getProxy().getRecipeManager().ifPresent(rm -> {
				try{
					for (RecipeEntry<?> re:rm.values()) {
						Recipe<?> r = re.value();
						if (r instanceof PotionRingRecipe) {
							if (r.getResult(null) != null && r.getResult(null).getItem() != null && PotionUtil.getPotionEffects(r.getResult(null)).size() == 1) {
								entries.add(r.getResult(null));
							} else {
								Log.w("Ring recipe has an invalid output: "+re.id().toString());
							}
						}
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
			});
		}
		
		
		//Adding potion vials
		Iterator<Potion> iterator = Registries.POTION.iterator();
		while(iterator.hasNext()) {
			Potion potion = iterator.next();
			if (potion != Potions.EMPTY) {
				entries.add(PotionUtil.setPotion(new ItemStack(ModItems.POTION_VIAL), potion));
			}
		}
	}
	
}
