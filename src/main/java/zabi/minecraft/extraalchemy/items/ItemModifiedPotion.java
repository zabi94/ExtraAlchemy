package zabi.minecraft.extraalchemy.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.StatList;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.extraalchemy.lib.Reference;

public class ItemModifiedPotion extends ItemPotion {

	public ItemModifiedPotion() {
		this.setRegistryName(Reference.MID, "modified_potion");
		this.setTranslationKey("potion");
		this.setCreativeTab(null);
	}

	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		EntityPlayer entityplayer = entityLiving instanceof EntityPlayer ? (EntityPlayer)entityLiving : null;

		if (entityplayer == null || !entityplayer.capabilities.isCreativeMode) {
			stack.shrink(1);
		}

		if (entityplayer instanceof EntityPlayerMP) {
			CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP)entityplayer, stack);
		}

		if (!worldIn.isRemote) {
			ArrayList<PotionEffect> list = new ArrayList<PotionEffect>();
			PotionUtils.addCustomPotionEffectToList(stack.getTagCompound(), list);
			for (PotionEffect potioneffect : list) {
				if (potioneffect.getPotion().isInstant()) {
					potioneffect.getPotion().affectEntity(entityplayer, entityplayer, entityLiving, potioneffect.getAmplifier(), 1.0D);
				} else {
					entityLiving.addPotionEffect(new PotionEffect(potioneffect));
				}
			}
		}

		if (entityplayer != null) {
			entityplayer.addStat(StatList.getObjectUseStats(this));
		}

		if (entityplayer == null || !entityplayer.capabilities.isCreativeMode) {
			if (stack.isEmpty()) {
				return new ItemStack(Items.GLASS_BOTTLE);
			}

			if (entityplayer != null) {
				entityplayer.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE));
			}
		}

		return stack;
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		ArrayList<PotionEffect> list = new ArrayList<PotionEffect>();
		PotionUtils.addCustomPotionEffectToList(stack.getTagCompound(), list);
		List<Tuple<String, AttributeModifier>> list1 = Lists.<Tuple<String, AttributeModifier>>newArrayList();

		if (list.isEmpty()) {
			String s = I18n.format("effect.none").trim();
			tooltip.add(TextFormatting.GRAY + s);
		} else {
			for (PotionEffect potioneffect : list) {
				String s1 = I18n.format(potioneffect.getEffectName()).trim();
				Potion potion = potioneffect.getPotion();
				Map<IAttribute, AttributeModifier> map = potion.getAttributeModifierMap();

				if (!map.isEmpty()) {
					for (Entry<IAttribute, AttributeModifier> entry : map.entrySet()) {
						AttributeModifier attributemodifier = entry.getValue();
						AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), potion.getAttributeModifierAmount(potioneffect.getAmplifier(), attributemodifier), attributemodifier.getOperation());
						list1.add(new Tuple<String, AttributeModifier>(((IAttribute)entry.getKey()).getName(), attributemodifier1));
					}
				}

				if (potioneffect.getAmplifier() > 0) {
					s1 = s1 + " " + I18n.format("potion.potency." + potioneffect.getAmplifier()).trim();
				}

				if (potioneffect.getDuration() > 20) {
					s1 = s1 + " (" + Potion.getPotionDurationString(potioneffect, 1) + ")";
				}

				if (potion.isBadEffect()) {
					tooltip.add(TextFormatting.RED + s1);
				} else {
					tooltip.add(TextFormatting.BLUE + s1);
				}
			}
		}

		if (!list1.isEmpty()) {
			tooltip.add("");
			tooltip.add(TextFormatting.DARK_PURPLE + I18n.format("potion.whenDrank"));

			for (Tuple<String, AttributeModifier> tuple : list1) {
				AttributeModifier attributemodifier2 = tuple.getSecond();
				double d0 = attributemodifier2.getAmount();
				double d1;

				if (attributemodifier2.getOperation() != 1 && attributemodifier2.getOperation() != 2) {
					d1 = attributemodifier2.getAmount();
				} else {
					d1 = attributemodifier2.getAmount() * 100.0D;
				}

				if (d0 > 0.0D) {
					tooltip.add(TextFormatting.BLUE + I18n.format("attribute.modifier.plus." + attributemodifier2.getOperation(), ItemStack.DECIMALFORMAT.format(d1), I18n.format("attribute.name." + (String)tuple.getFirst())));
				} else if (d0 < 0.0D) {
					d1 = d1 * -1.0D;
					tooltip.add(TextFormatting.RED + I18n.format("attribute.modifier.take." + attributemodifier2.getOperation(), ItemStack.DECIMALFORMAT.format(d1), I18n.format("attribute.name." + (String)tuple.getFirst())));
				}
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public String getItemStackDisplayName(ItemStack stack) {
        return I18n.format(PotionUtils.getPotionFromItem(stack).getNamePrefixed("potion.effect."));
    }
	
}

