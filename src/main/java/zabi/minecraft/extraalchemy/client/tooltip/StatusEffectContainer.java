package zabi.minecraft.extraalchemy.client.tooltip;

import java.util.List;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;

public interface StatusEffectContainer {
	
	static final StatusEffectContainer DEFAULT_CONTAINER = s -> PotionUtil.getPotionEffects(s);
	
	public List<StatusEffectInstance> getContainedEffects(ItemStack stack);
	
	default boolean hasEffects(ItemStack stack) {
		return this.getContainedEffects(stack).size() > 0;
	}
	
	public static StatusEffectContainer of(ItemStack stack) {
		if (stack.getItem() instanceof StatusEffectContainer sec) return sec;
		return DEFAULT_CONTAINER;
	}

}
