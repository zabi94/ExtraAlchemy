package zabi.minecraft.extraalchemy.potion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;

public abstract class PotionInstant extends PotionBase {

	public PotionInstant(boolean isBadEffectIn, int liquidColorIn, String name) {
		super(isBadEffectIn, liquidColorIn, name);
	}

	@Override
	public void affectEntity(Entity source, Entity indirectSource, EntityLivingBase e, int amp, double health) {
		this.applyInstantEffect(e, amp);
	}
	
	@Override
	public void applyAttributesModifiersToEntity(EntityLivingBase e, AbstractAttributeMap attributeMapIn, int amp) {
		this.applyInstantEffect(e, amp);
	}

	@Override
	public boolean isInstant() {
		return true;
	}

	protected abstract void applyInstantEffect(EntityLivingBase e, int amp);
	

}
