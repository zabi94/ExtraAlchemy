package zabi.minecraft.extraalchemy.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;

public class PotionUtilities {

	public static void cloneEffectsToStack(ItemStack from, ItemStack to) {
		if (!from.contains(DataComponentTypes.POTION_CONTENTS)) {
			throw new IllegalArgumentException("Stack doesn't have potion contents");
		}
		
		PotionContentsComponent old_potion_data = from.get(DataComponentTypes.POTION_CONTENTS);
		PotionContentsComponent new_potion_data = new PotionContentsComponent(old_potion_data.potion().orElseThrow());
		for (StatusEffectInstance sei: old_potion_data.customEffects()) {
			new_potion_data = new_potion_data.with(sei);
		}
		to.set(DataComponentTypes.POTION_CONTENTS, new_potion_data);		
	}
	
	public static void setStackPotion(ItemStack item, Potion potion) {
		PotionContentsComponent comp = new PotionContentsComponent(Registries.POTION.getEntry(potion));
		item.set(DataComponentTypes.POTION_CONTENTS, comp);
	}
	
	public static boolean hasPotionEffects(ItemStack stack) {
		if (!stack.contains(DataComponentTypes.POTION_CONTENTS)) {
			return false;
		}
		PotionContentsComponent pcc = stack.get(DataComponentTypes.POTION_CONTENTS);
		return pcc.hasEffects();
	}
	
	public static StatusEffectInstance getFirstEffect(ItemStack stack) {
		if (!stack.contains(DataComponentTypes.POTION_CONTENTS)) {
			throw new IllegalArgumentException("Stack doesn't have potion contents");
		}
		return stack.get(DataComponentTypes.POTION_CONTENTS).getEffects().iterator().next();
	}
	
	public static List<StatusEffectInstance> getEffects(ItemStack stack) {
		if (!stack.contains(DataComponentTypes.POTION_CONTENTS)) {
			return List.of();
		}
		ArrayList<StatusEffectInstance> result = new ArrayList<>();
		Iterator<StatusEffectInstance> i = stack.get(DataComponentTypes.POTION_CONTENTS).getEffects().iterator();
		while (i.hasNext()) {
			result.add(i.next());
		}
		return result;
	}
	
	public static boolean isSingleEffect(ItemStack stack) {
		
		if (!stack.contains(DataComponentTypes.POTION_CONTENTS)) {
			return false;
		}
		
		int count = 0;
		
		Iterator<StatusEffectInstance> it = stack.get(DataComponentTypes.POTION_CONTENTS).getEffects().iterator();
		
		while (it.hasNext()) {
			it.next();
			count++;
			if (count > 1) return false;
		}
		
		return count == 1;
	}
	
}
