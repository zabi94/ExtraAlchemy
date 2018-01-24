package zabi.minecraft.extraalchemy.potion.potion;

import java.util.ArrayList;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ITickable;
import zabi.minecraft.extraalchemy.potion.PotionBase;

public class PotionHurry extends PotionBase {
	
	public static ArrayList<String> blacklist = new ArrayList<String>();

	public PotionHurry(boolean isBadEffectIn, int liquidColorIn) {
		super(isBadEffectIn, liquidColorIn, "hurry");
		this.setIconIndex(5, 0);
	}

	@Override
	public void performEffect(EntityLivingBase e, int amp) {
		super.performEffect(e, amp);
		if (e instanceof EntityPlayer && ((EntityPlayer)e).isSpectator()) return;
		if (!e.getEntityWorld().isRemote && e instanceof EntityPlayer) {
			for (int x=-2;x<=2;x++) for (int y=-2;y<=2;y++) for (int z=-2;z<=2;z++) {
				if (e.getEntityWorld().getTileEntity(e.getPosition().add(x, y, z)) instanceof ITickable && !blacklist.contains(e.getEntityWorld().getTileEntity(e.getPosition().add(x, y, z)).getClass().toString())) {
					((ITickable) e.getEntityWorld().getTileEntity(e.getPosition().add(x, y, z))).update();
					((EntityPlayer)e).addExhaustion(0.8f);
				}
			}
		}
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		if (amplifier>1) amplifier=1;
		return duration % (3 - amplifier) == 0;
	}

}
