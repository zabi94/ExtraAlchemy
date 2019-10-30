package zabi.minecraft.extraalchemy.potion.potion;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import zabi.minecraft.extraalchemy.capability.MagnetismStatus;
import zabi.minecraft.extraalchemy.integration.BotaniaHandler;
import zabi.minecraft.extraalchemy.lib.Reference;
import zabi.minecraft.extraalchemy.potion.NoncontinuousEffect;
import zabi.minecraft.extraalchemy.potion.PotionBase;
import zabi.minecraft.extraalchemy.potion.PotionReference;

public class PotionMagnetism extends PotionBase implements NoncontinuousEffect {
	
	public PotionMagnetism(boolean isBadEffectIn, int liquidColorIn) {
		super(isBadEffectIn, liquidColorIn, "magnetism");
		this.setIconIndex(3, 1);
	}

	@Override
	public void performEffect(EntityLivingBase e, int amp) {
		super.performEffect(e, amp);
		if (e instanceof EntityPlayer && ((EntityPlayer)e).isSpectator()) return;
		if (!e.getEntityWorld().isRemote && e instanceof EntityPlayer) {
			int radius = 4 + 4*amp;
			List<EntityItem> list = e.getEntityWorld().getEntitiesWithinAABB(EntityItem.class, e.getEntityBoundingBox().grow(radius, radius, radius));
			list.stream().forEach(i -> attract(i,(EntityPlayer) e,amp));
		}
	}

	private void attract(EntityItem i, EntityPlayer p, int amp) {
		if (!p.getCapability(MagnetismStatus.CAPABILITY, null).magnetActive) {
			return;
		}
		if (BotaniaHandler.isSolegnoliaAround(i)) {
			return;
		}
		if (MagnetismHandler.isSneakingRequired(p, i) == p.isSneaking()) {
			if (amp>0) {
				i.setNoPickupDelay();
			}
			i.onCollideWithPlayer(p);
		}
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}
	
	public void setIconActive(boolean status) {
		if (status) {
			this.setIconIndex(3, 1);
		} else {
			this.setIconIndex(4, 2);
		}
	}
	
	public static class MagnetismHandler {
		
		private static final int DELAY_TICKS = 20*5;

		@SubscribeEvent
		public void onPlayerDropping (ItemTossEvent evt) {
			if (evt.getPlayer().getActivePotionEffect(PotionReference.INSTANCE.MAGNETISM)!=null) {
				evt.getEntityItem().getTags().add(Reference.MID+":NoPickup");
			}
		}

		public static boolean isSneakingRequired(EntityPlayer player, EntityItem item) {
			if (item.getTags().contains(Reference.MID+":NoPickup") && item.ticksExisted<(DELAY_TICKS)) return true;
			return false;
		}
	}

	@Override
	public boolean isEffectActive(EntityLivingBase player) {
		return player.getCapability(MagnetismStatus.CAPABILITY, null).magnetActive;
	}
}
