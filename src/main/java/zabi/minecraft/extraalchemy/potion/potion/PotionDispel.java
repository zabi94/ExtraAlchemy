package zabi.minecraft.extraalchemy.potion.potion;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import zabi.minecraft.extraalchemy.ExtraAlchemy;
import zabi.minecraft.extraalchemy.network.packets.PacketDispelSuccess;
import zabi.minecraft.extraalchemy.potion.PotionInstant;

public class PotionDispel extends PotionInstant {
	
	public PotionDispel(boolean isBadEffectIn, int liquidColorIn) {
		super(isBadEffectIn, liquidColorIn, "dispel");
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}

	@Override
	public void applyInstantEffect(EntityLivingBase e, int amp) {
		if (e instanceof EntityPlayer && ((EntityPlayer)e).isSpectator()) return;
		if (e.getEntityWorld().isRemote) return;
		
		if (e instanceof EntityPlayer) {
			EntityPlayer p = (EntityPlayer) e;
			p.inventory.mainInventory.stream().forEach(is -> removeEnchants(is,p));
			p.inventory.armorInventory.stream().forEach(is -> removeEnchants(is,p));
			p.inventory.offHandInventory.stream().forEach(is -> removeEnchants(is,p));
		} else {
			Iterator<ItemStack> it = e.getEquipmentAndArmor().iterator();
			while (it.hasNext()) {
				ItemStack is = it.next();
				removeEnchants(is,e);
			}
		}
	}

	private void removeEnchants(ItemStack is, EntityLivingBase e) {
		boolean flag = false;
		if (!is.isEmpty()) {
			Map<Enchantment,Integer> enchs = EnchantmentHelper.getEnchantments(is);
			Map<Enchantment,Integer> newEnchs = new HashMap<Enchantment,Integer>();
			for (Enchantment en:enchs.keySet()) {
				if (Math.random()>0.92) {
					flag=true;
					if (enchs.get(en)>1) {
						newEnchs.put(en, enchs.get(en)-1);
					}
				} else {
					newEnchs.put(en, enchs.get(en));
				}
			}
			EnchantmentHelper.setEnchantments(newEnchs, is);
		}
		
		if (flag && e!=null) {
			ExtraAlchemy.network.sendToAllAround(new PacketDispelSuccess(), new TargetPoint(e.dimension, e.posX, e.posY, e.posZ, 10));
		}
		
	}
}
