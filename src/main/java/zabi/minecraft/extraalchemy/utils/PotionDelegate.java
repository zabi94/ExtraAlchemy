package zabi.minecraft.extraalchemy.utils;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.text.Text;

public class PotionDelegate {

	private ItemStack stack;
	
	public PotionDelegate(ItemStack stack) {
		this.stack = stack;
	}

	public List<StatusEffectInstance> getEffects() {
		return PotionUtil.getPotionEffects(stack);
	}
	
	public void addToTooltip(List<Text> tooltip) {
		PotionUtil.buildTooltip(getEffects(), tooltip, 1, 20);
	}
	
	public NbtCompound serialize() {
		return stack.writeNbt(new NbtCompound());
	}
	
	public static Optional<PotionDelegate> deserialize(NbtCompound nbt) {
		ItemStack stack = ItemStack.fromNbt(nbt);
		if (stack.isEmpty()) return Optional.empty();
		return Optional.of(new PotionDelegate(stack));
	}
	
	public boolean match(ItemStack other) {
		if (other.isEmpty()) return false;
		return this.equals(new PotionDelegate(other));
	}
	
	public boolean isEmpty() {
		if (stack.isEmpty()) return true;
		return getEffects().isEmpty();
	}
	
	public String getTranslationKey() {
		
		Potion mainPotion = PotionUtil.getPotion(stack);
		if (mainPotion != null && !mainPotion.equals(Potions.EMPTY)) {
			return mainPotion.finishTranslationKey("item.minecraft.potion.effect.");
		}
		
		var effects = getEffects();
		if (!effects.isEmpty()) {
			return stack.getTranslationKey();
//			return effects.get(0).getTranslationKey();
		}
		
		return "extraalchemy.potion_delegate.no_name";
	}
	
	public ItemStack getStack() {
		if (stack.isEmpty()) return ItemStack.EMPTY;
		return stack.copyWithCount(1);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		PotionDelegate other = (PotionDelegate) obj;
		var ownEffects = this.getEffects();
		var otherEffects = other.getEffects();
		
		Comparator<StatusEffectInstance> comparator = Comparator.comparingInt(i -> i.hashCode());
		ownEffects.sort(comparator);
		otherEffects.sort(comparator);
		
		return Objects.equals(ownEffects, otherEffects);
	}
	
	
	
}
