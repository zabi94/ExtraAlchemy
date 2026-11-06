package dev.zabi94.extraalchemy.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;

public class PotionUtilities {

	public static void cloneEffectsToStack(ItemStack from, ItemStack to) {
		if (!from.has(DataComponents.POTION_CONTENTS)) {
			throw new IllegalArgumentException("Stack doesn't have potion contents");
		}
		
		PotionContents old_potion_data = from.get(DataComponents.POTION_CONTENTS);
		PotionContents new_potion_data = new PotionContents(old_potion_data.potion().orElseThrow());
		for (MobEffectInstance sei: old_potion_data.customEffects()) {
			new_potion_data = new_potion_data.withEffectAdded(sei);
		}
		to.set(DataComponents.POTION_CONTENTS, new_potion_data);		
	}
	
	public static void setStackPotion(ItemStack item, Potion potion) {
		PotionContents comp = new PotionContents(BuiltInRegistries.POTION.wrapAsHolder(potion));
		item.set(DataComponents.POTION_CONTENTS, comp);
	}
	
	public static boolean hasPotionEffects(ItemStack stack) {
		if (!stack.has(DataComponents.POTION_CONTENTS)) {
			return false;
		}
		PotionContents pcc = stack.get(DataComponents.POTION_CONTENTS);
		return pcc.hasEffects();
	}
	
	public static MobEffectInstance getFirstEffect(ItemStack stack) {
		if (!stack.has(DataComponents.POTION_CONTENTS)) {
			throw new IllegalArgumentException("Stack doesn't have potion contents");
		}
		return stack.get(DataComponents.POTION_CONTENTS).getAllEffects().iterator().next();
	}
	
	public static List<MobEffectInstance> getEffects(ItemStack stack) {
		if (!stack.has(DataComponents.POTION_CONTENTS)) {
			return List.of();
		}
		ArrayList<MobEffectInstance> result = new ArrayList<>();
		Iterator<MobEffectInstance> i = stack.get(DataComponents.POTION_CONTENTS).getAllEffects().iterator();
		while (i.hasNext()) {
			result.add(i.next());
		}
		return result;
	}
	
	public static boolean isSingleEffect(ItemStack stack) {
		
		if (!stack.has(DataComponents.POTION_CONTENTS)) {
			return false;
		}
		
		int count = 0;
		
		Iterator<MobEffectInstance> it = stack.get(DataComponents.POTION_CONTENTS).getAllEffects().iterator();
		
		while (it.hasNext()) {
			it.next();
			count++;
			if (count > 1) return false;
		}
		
		return count == 1;
	}
	
}
