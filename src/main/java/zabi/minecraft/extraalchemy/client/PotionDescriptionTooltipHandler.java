package zabi.minecraft.extraalchemy.client;

import java.util.HashSet;
import java.util.List;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.extraalchemy.ModConfig;
import zabi.minecraft.extraalchemy.ModConfig.Mode;
import zabi.minecraft.extraalchemy.gui.container.window.GuiPotionBag;
import zabi.minecraft.extraalchemy.integration.ModIDs;
import zabi.minecraft.extraalchemy.items.MinervaMedal;
import zabi.minecraft.extraalchemy.lib.Reference;
import zabi.minecraft.extraalchemy.potion.PotionTypeBase;

@SideOnly(Side.CLIENT)
public class PotionDescriptionTooltipHandler {
	
	private static final HashSet<String> FORBIDDEN_BAG_POT_TYPES = new HashSet<>();
	
	static {
		FORBIDDEN_BAG_POT_TYPES.add("botania:brewVial");
		FORBIDDEN_BAG_POT_TYPES.add("botania:brewFlask");
		FORBIDDEN_BAG_POT_TYPES.add("botania:bloodPendant");
		FORBIDDEN_BAG_POT_TYPES.add("covens:brew_phial_drink");
		FORBIDDEN_BAG_POT_TYPES.add("covens:brew_phial_splash");
		FORBIDDEN_BAG_POT_TYPES.add("covens:brew_phial_linger");
		FORBIDDEN_BAG_POT_TYPES.add("rustic:elixir");
		FORBIDDEN_BAG_POT_TYPES.add("minecraft:lingering_potion");
		FORBIDDEN_BAG_POT_TYPES.add("minecraft:splash_potion");
	}

	@SubscribeEvent
	public void onTooltipEvent(ItemTooltipEvent evt) {
		List<String> toolTip = evt.getToolTip();
		if (evt.getItemStack().getItem() == MinervaMedal.medal) return;
		addByproductString(evt, toolTip);
		PotionType pt = PotionUtils.getPotionFromItem(evt.getItemStack());
		if (pt instanceof PotionTypeBase && !((PotionTypeBase)pt).isArtificial()) {
			displayModOwnTooltip(evt, toolTip, pt);
		} else if (!pt.getEffects().isEmpty()) {
			addCredits(toolTip, ModIDs.getModName(pt.getRegistryName().getNamespace()), evt.getItemStack());
		}
		showHintForPotionBag(evt);
	}

	private void showHintForPotionBag(ItemTooltipEvent evt) {
		if (Minecraft.getMinecraft().currentScreen instanceof GuiPotionBag) {
			if (FORBIDDEN_BAG_POT_TYPES.contains(evt.getItemStack().getItem().getRegistryName().toString())) {
				evt.getToolTip().add(TextFormatting.RED+I18n.format("tooltip.no_potion_bag"));
			}
		}
	}

	private void displayModOwnTooltip(ItemTooltipEvent evt, List<String> toolTip, PotionType pt) {
		String potName = ((PotionTypeBase)pt).getPotion().getName();
		addCredits(toolTip, Reference.NAME, evt.getItemStack());
		addBadJoke(toolTip, potName);
		if (shouldShowHint(evt.getFlags().isAdvanced())) {
			String textRaw = I18n.format("description."+potName);
			toolTip.add("");
			toolTip.add(textRaw);
		} else if (!ModConfig.client.descriptionMode.name().equals("NONE")) {
			toolTip.add(I18n.format("tooltip.togglef3."+ModConfig.client.descriptionMode.name()));
		}
	}

	private void addBadJoke(List<String> toolTip, String potName) {
		if (ModConfig.client.showBadJoke) {
			String badJoke = I18n.format("funny."+potName);
			toolTip.add(ChatFormatting.GREEN+ChatFormatting.ITALIC.toString()+I18n.format(badJoke));	//Bad Joke
		}
	}

	private void addByproductString(ItemTooltipEvent evt, List<String> toolTip) {
		if (evt.getItemStack().hasTagCompound()) { 
			if (evt.getItemStack().getTagCompound().hasKey("splitresult")) {
				toolTip.add(I18n.format("item.byproduct").replace("%", ""+(evt.getItemStack().getTagCompound().getInteger("splitresult"))));
			}
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
