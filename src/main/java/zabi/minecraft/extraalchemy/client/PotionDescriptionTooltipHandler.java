package zabi.minecraft.extraalchemy.client;

import java.util.List;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.extraalchemy.ModConfig;
import zabi.minecraft.extraalchemy.ModConfig.Mode;
import zabi.minecraft.extraalchemy.integration.ModIDs;
import zabi.minecraft.extraalchemy.items.MinervaMedal;
import zabi.minecraft.extraalchemy.lib.Reference;
import zabi.minecraft.extraalchemy.potion.PotionTypeBase;

@SideOnly(Side.CLIENT)
public class PotionDescriptionTooltipHandler {

	@SubscribeEvent
	public void onTooltipEvent(ItemTooltipEvent evt) {
		List<String> toolTip = evt.getToolTip();
		if (evt.getItemStack().getItem() == MinervaMedal.medal) return;

		if (evt.getItemStack().hasTagCompound()) { 
			if (evt.getItemStack().getTagCompound().hasKey("splitresult")) {
				toolTip.add(I18n.format("item.byproduct").replace("%", ""+(evt.getItemStack().getTagCompound().getInteger("splitresult"))));
			}
		}

		PotionType pt = PotionUtils.getPotionFromItem(evt.getItemStack());

		if (pt instanceof PotionTypeBase && !((PotionTypeBase)pt).isArtificial()) {

			String potName = ((PotionTypeBase)pt).getPotion().getName();
			String textRaw = I18n.format("description."+potName);
			String badJoke = I18n.format("funny."+potName);

			addCredits(toolTip, Reference.NAME, evt.getItemStack());

			if (ModConfig.client.showBadJoke) toolTip.add(ChatFormatting.GREEN+ChatFormatting.ITALIC.toString()+I18n.format(badJoke));	//Bad Joke

			if (shouldShowHint(evt.getFlags().isAdvanced())) {
				toolTip.add("");
				toolTip.add(textRaw);
			} else if (!ModConfig.client.descriptionMode.name().equals("NONE")) toolTip.add(I18n.format("tooltip.togglef3."+ModConfig.client.descriptionMode.name()));
		} else if (!pt.getEffects().isEmpty()){
			addCredits(toolTip, ModIDs.getModName(pt.getRegistryName().getNamespace()), evt.getItemStack());
		}
	}

	private static boolean shouldShowHint(boolean advTooltips) {
		return (advTooltips && ModConfig.client.descriptionMode==Mode.F3H) ||
				(GuiScreen.isAltKeyDown() && ModConfig.client.descriptionMode==Mode.ALT) ||
				(GuiScreen.isCtrlKeyDown() && ModConfig.client.descriptionMode==Mode.CTRL) ||
				(GuiScreen.isShiftKeyDown() && ModConfig.client.descriptionMode==Mode.SHIFT);
	}

	private static void addCredits(List<String> toolTip, String modname, ItemStack stack) {
		if (!isVanillaItem(stack)) {
			toolTip.add("");
			toolTip.add(ChatFormatting.GOLD+I18n.format("tooltip.credit", modname));
		}
	}

	private static boolean isVanillaItem(ItemStack stack) {
		return stack.getItem() == Items.POTIONITEM || stack.getItem() == Items.LINGERING_POTION || stack.getItem() == Items.SPLASH_POTION || stack.getItem() == Items.TIPPED_ARROW;
	}
}
