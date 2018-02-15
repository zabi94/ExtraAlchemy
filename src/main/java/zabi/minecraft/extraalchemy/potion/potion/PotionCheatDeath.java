package zabi.minecraft.extraalchemy.potion.potion;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import zabi.minecraft.extraalchemy.ExtraAlchemy;
import zabi.minecraft.extraalchemy.ModConfig;
import zabi.minecraft.extraalchemy.lib.Log;
import zabi.minecraft.extraalchemy.potion.PotionBase;
import zabi.minecraft.extraalchemy.potion.PotionReference;

public class PotionCheatDeath extends PotionBase {
	protected static List<ItemStack> noItems = ImmutableList.copyOf(new ArrayList<ItemStack>());
	private static DamageSource DEATH_REVENGE = new DamageSource("death_revenge") {

		@Override
		public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {
			return new TextComponentTranslation("death.attack." + this.damageType + ".player", new Object[] {entityLivingBaseIn.getDisplayName(), entityLivingBaseIn.getDisplayName()});
		}
		
	};
	
	public static ArrayList<String> blacklist = new ArrayList<String>();
	
	static {
		blacklist.add(DEATH_REVENGE.damageType);
		blacklist.add("bloodMagic");
	}
	
	@Override
	public int getLiquidColor() {
		if (ModConfig.client.rainbowCheatDeath) return ExtraAlchemy.proxy.getRainbow(super.getLiquidColor());
		return super.getLiquidColor();
	}

	public PotionCheatDeath(boolean isBadEffectIn, int liquidColorIn) {
		super(isBadEffectIn, liquidColorIn, "cheatDeath_"+(isBadEffectIn?"active":"quiescent"));
		this.setIconIndex(isBadEffect()?6:7, 0);
	}

	@Override
	public void performEffect(EntityLivingBase e, int amp) {
		super.performEffect(e, amp);
		if (e instanceof EntityPlayer && ((EntityPlayer)e).isSpectator()) return;
		if (!e.getEntityWorld().isRemote && !e.isDead) {
			int duration = e.getActivePotionEffect(PotionReference.INSTANCE.CHEAT_DEATH_POTION_ACTIVE).getDuration();
			if (ModConfig.options.cheatDeathRandom) {
				double probability = 2d/200d;
				if (duration<200) {
					probability = 1d/20d;
				} else if (duration < 400) {
					probability = 1d/80d;
				}
//				double probability = 1d/((duration*5)-2d);
				double rand = e.getRNG().nextDouble();
				Log.i(probability+" "+rand);
				if (rand<probability) {
					e.attackEntityFrom(DEATH_REVENGE, Integer.MAX_VALUE);
					Log.d("Applying death at: "+duration+" ticks remaining. r"+rand+"p"+probability);
				}
			} else {
				e.attackEntityFrom(DEATH_REVENGE, Integer.MAX_VALUE);
			}
		}
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return this.isBadEffect() && ((ModConfig.options.cheatDeathRandom && duration%10==0) || duration==1);
	}

	public static class PotionCheatDeathHandler {

		@SubscribeEvent(receiveCanceled=false, priority=EventPriority.HIGH)
		public void onLivingDeath(LivingDeathEvent evt) {
			if (blacklist.contains(evt.getSource().damageType)) {
				Log.d("Damage in blacklist, ignoring cheat death protection");
				return;
			}
			if (evt.getEntityLiving().getActivePotionEffect(PotionReference.INSTANCE.CHEAT_DEATH_POTION_ACTIVE)==null && evt.getEntityLiving().getActivePotionEffect(PotionReference.INSTANCE.CHEAT_DEATH_POTION)!=null && !evt.getSource().canHarmInCreative()) {
				evt.setCanceled(true);
				Log.d("Prevented death, good effect to bad");
				evt.getEntityLiving().setHealth((evt.getEntityLiving().getMaxHealth()/2)+1);
				evt.getEntityLiving().addPotionEffect(new PotionEffect(PotionReference.INSTANCE.CHEAT_DEATH_POTION_ACTIVE, evt.getEntityLiving().getActivePotionEffect(PotionReference.INSTANCE.CHEAT_DEATH_POTION).getDuration()/2, 0) {
					@Override
					public java.util.List<net.minecraft.item.ItemStack> getCurativeItems() {
						return noItems;
					}
				});
				evt.getEntityLiving().removePotionEffect(PotionReference.INSTANCE.CHEAT_DEATH_POTION);
				return;
			}
			
			if (evt.getEntityLiving().getActivePotionEffect(PotionReference.INSTANCE.CHEAT_DEATH_POTION)==null && evt.getEntityLiving().getActivePotionEffect(PotionReference.INSTANCE.CHEAT_DEATH_POTION_ACTIVE)!=null && evt.getSource()!=DEATH_REVENGE && !evt.getSource().canHarmInCreative()) {
				evt.getEntityLiving().setHealth((evt.getEntityLiving().getMaxHealth()/5)+1);
				evt.setCanceled(true);
				Log.d("Prevented death, bad effect");
			}
		}
		
		@SubscribeEvent
		public void onLivingDrops(LivingDropsEvent evt) {
			if (ModConfig.options.hardcoreCheatDeath && evt.getEntityLiving().getActivePotionEffect(PotionReference.INSTANCE.CHEAT_DEATH_POTION_ACTIVE)!=null) {
				evt.setCanceled(true);
				Log.d("Stopped items from dropping");
			}
		}
	}

}
