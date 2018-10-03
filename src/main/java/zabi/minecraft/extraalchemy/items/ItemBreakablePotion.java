package zabi.minecraft.extraalchemy.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.extraalchemy.ExtraAlchemy;
import zabi.minecraft.extraalchemy.ModConfig;
import zabi.minecraft.extraalchemy.lib.Reference;

public class ItemBreakablePotion extends ItemPotion {
	
	protected ItemBreakablePotion() {
        this.setMaxStackSize(16);
        if (ModConfig.options.addSeparateTab) this.setCreativeTab(ExtraAlchemy.TAB);
        else this.setCreativeTab(CreativeTabs.BREWING);
        this.setRegistryName( new ResourceLocation(Reference.MID, "breakable_potion"));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public String getItemStackDisplayName(ItemStack stack) {
        return I18n.format(PotionUtils.getPotionFromItem(stack).getNamePrefixed("potion.effect."))+" "+I18n.format("item.breakable");
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
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
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		if (this.isInCreativeTab(tab)) {
			for (PotionType potiontype : PotionType.REGISTRY) {
				list.add(PotionUtils.addPotionToItemStack(new ItemStack(this), potiontype));
			}
		}
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand){
		if (!world.isRemote) {
			ArrayList<PotionEffect> list = new ArrayList<PotionEffect>();
			PotionUtils.addCustomPotionEffectToList(player.getHeldItem(hand).getTagCompound(), list);
            for (PotionEffect potioneffect : list) {
            	player.addPotionEffect(new PotionEffect(potioneffect));
            }
        } else {
        	Random rand = new Random();
        	player.swingArm(hand);
        	player.renderBrokenItemStack(player.getHeldItem(hand));
        	world.playSound(player.posX, player.posY, player.posZ, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 0.8F, 1f+rand.nextFloat(), false);
        }
		if (player != null && !player.capabilities.isCreativeMode) {
			player.getHeldItem(hand).setCount(player.getHeldItem(hand).getCount()-1);
        }
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
	
}
