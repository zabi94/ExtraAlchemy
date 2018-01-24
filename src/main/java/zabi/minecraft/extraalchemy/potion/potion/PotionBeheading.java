package zabi.minecraft.extraalchemy.potion.potion;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSkull;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import zabi.minecraft.extraalchemy.potion.PotionBase;
import zabi.minecraft.extraalchemy.potion.PotionReference;

public class PotionBeheading extends PotionBase {

	public PotionBeheading(boolean isBadEffectIn, int liquidColorIn) {
		super(isBadEffectIn, liquidColorIn, "beheading");
		this.setIconIndex(3, 2);
	}
	
	@Override
	public boolean isReady(int duration, int amplifier) {
		return false;
	}
	
	public static class PotionBeheadingHandler {
		
		@SubscribeEvent
		public void onEntityKilled(LivingDropsEvent evt) {
			if (evt.getEntityLiving().getEntityWorld().isRemote) return;
			PotionEffect pe = evt.getEntityLiving().getActivePotionEffect(PotionReference.INSTANCE.BEHEADING); 
			if (pe!=null) {
				if (pe.getAmplifier()>0 || Math.random()>0.7) {
					ItemStack stack = getHead(evt.getEntityLiving());
					if (!stack.isEmpty()) {
						
						for (EntityItem ei:evt.getDrops()) if (Block.getBlockFromItem(ei.getItem().getItem()) instanceof BlockSkull) return; //If the head would naturally drop/drop from another mod effect, don't drop yours
						EntityItem eni = new EntityItem(evt.getEntityLiving().getEntityWorld(), evt.getEntityLiving().posX, evt.getEntityLiving().posY, evt.getEntityLiving().posZ, stack);
						eni.setDefaultPickupDelay();
						eni.lifespan = 60;
						evt.getDrops().add(eni);
					}
				}
			}
		}
		
		  private ItemStack getHead(EntityLivingBase entity) {
			    if(entity instanceof EntitySkeleton) {
			      return new ItemStack(Items.SKULL, 1, 0);
			    } else if(entity instanceof EntityWitherSkeleton) {
			      return new ItemStack(Items.SKULL, 1, 1);
			    } else if(entity instanceof EntityZombie) {
			      return new ItemStack(Items.SKULL, 1, 2);
			    } else if(entity instanceof EntityCreeper) {
			      return new ItemStack(Items.SKULL, 1, 4);
			    } else if(entity instanceof EntityPlayer) {
			      ItemStack head = new ItemStack(Items.SKULL, 1, 3);
			      NBTTagCompound nametag = new NBTTagCompound();
			      nametag.setString("SkullOwner", entity.getDisplayName().getFormattedText());
			      head.setTagCompound(nametag);
			      return head;
			    }
			    return ItemStack.EMPTY;
			  }
		
	}

}
