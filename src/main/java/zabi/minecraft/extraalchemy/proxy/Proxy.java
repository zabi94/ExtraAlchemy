package zabi.minecraft.extraalchemy.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;

public abstract class Proxy {
	public abstract void updatePlayerPotion(EntityPlayer e, PotionEffect fx);
	public void registerItemDescriptions() {}
	public void registerColorHandler() {}
	public void registerItemModel(Item i) {}
	public abstract void playDispelSound();
	
	public abstract void registerEventHandler();
	public EntityPlayer getSP() {
		return null;
	}
	
	public int getRainbow(int defaultColor) {
		return defaultColor;
	}

	public boolean isShiftingInInv() {
		return false;
	}
}
