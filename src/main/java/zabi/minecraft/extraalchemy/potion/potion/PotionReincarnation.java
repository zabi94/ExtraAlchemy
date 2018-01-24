package zabi.minecraft.extraalchemy.potion.potion;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import zabi.minecraft.extraalchemy.ExtraAlchemy;
import zabi.minecraft.extraalchemy.potion.PotionBase;
import zabi.minecraft.extraalchemy.potion.PotionReference;

public class PotionReincarnation extends PotionBase {

	public PotionReincarnation(boolean isBadEffectIn, int liquidColorIn) {
		super(isBadEffectIn, liquidColorIn, "reincarnation");
		this.setIconIndex(4, 1);
	}
	
	public static class PotionReincarnationHandler {
		
		@SubscribeEvent
		public void onReincarnation(PlayerEvent.Clone evt) {
			if (evt.isWasDeath() && evt.getOriginal().getActivePotionEffect(PotionReference.INSTANCE.REINCARNATION)!=null) {
				final boolean strong = evt.getOriginal().getActivePotionEffect(PotionReference.INSTANCE.REINCARNATION).getAmplifier()>0;
				evt.getOriginal().getActivePotionEffects().stream()
				
				.filter(e -> e.getPotion()!=PotionReference.INSTANCE.REINCARNATION || strong) //reincarna se stessa solo se strong
				.filter(e -> !(strong && e.getPotion().isBadEffect())) //se è strong elimina gli effetti cattivi
				.filter(e -> !(e.getPotion() instanceof PotionCheatDeath)) //Non reincarna mai cheat death
				
				.forEach(e -> addEffect(evt.getEntityPlayer(),e));
			}
		}
		
		private void addEffect(EntityPlayer p, PotionEffect e) {
			p.addPotionEffect(e);
		}
		
		@SubscribeEvent
		public void onRespawn(PlayerRespawnEvent evt) {
			evt.player.getActivePotionEffects().stream().forEach(pe -> ExtraAlchemy.proxy.updatePlayerPotion(evt.player, pe));
		}
	}

}
