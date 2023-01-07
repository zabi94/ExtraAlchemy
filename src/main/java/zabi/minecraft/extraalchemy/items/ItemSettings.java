package zabi.minecraft.extraalchemy.items;

import java.util.Iterator;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.registry.Registries;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import zabi.minecraft.extraalchemy.config.ModConfig;
import zabi.minecraft.extraalchemy.utils.LibMod;
import zabi.minecraft.extraalchemy.utils.Log;
import zabi.minecraft.extraalchemy.utils.proxy.SidedProxy;

public class ItemSettings {
	
	public static ItemGroup EXTRA_ALCHEMY_GROUP;// = FabricItemGroupBuilder.create().icon().build();
	
	public static void init() {
		EXTRA_ALCHEMY_GROUP = FabricItemGroup.builder(new Identifier(LibMod.MOD_ID, "all"))
				.displayName(Text.translatable("itemGroup.extraalchemy.all"))
				.icon(() -> new ItemStack(ModItems.POTION_BAG))
				.entries(ItemSettings::getDefaultGroupItems)
				.build();
	}
	
	private static void getDefaultGroupItems(FeatureSet features, ItemGroup.Entries entries, boolean enabled) {
		entries.add(ModItems.EMPTY_RING);
		entries.add(ModItems.EMPTY_VIAL);
		entries.add(ModItems.POTION_BAG);
		
		//Adding filled ring recipes
		try{
			if (ModConfig.INSTANCE.enableRings) {
				
				RecipeManager rm = SidedProxy.getProxy().getRecipeManager().orElseThrow();
				
				for (Recipe<?> r:rm.values()) {
					if (r.getOutput().getItem().equals(ModItems.POTION_RING)) {
						if (r.getOutput() != null && r.getOutput().getItem() != null) {
							entries.add(r.getOutput());
						} else {
							Log.w("Ring recipe has an invalid output: "+r.getId().toString());
						}
					}
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
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
