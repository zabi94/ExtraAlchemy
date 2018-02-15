package zabi.minecraft.extraalchemy.client;

import java.util.List;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.extraalchemy.ModConfig;
import zabi.minecraft.extraalchemy.ModConfig.Mode;
import zabi.minecraft.extraalchemy.integration.ModIDs;
import zabi.minecraft.extraalchemy.items.ModItems;
import zabi.minecraft.extraalchemy.lib.Reference;
import zabi.minecraft.extraalchemy.potion.PotionTypeBase;
import zabi.minecraft.extraalchemy.potion.PotionTypeCompat;

@SideOnly(Side.CLIENT)
@SuppressWarnings("deprecation")
public class PotionDescriptionTooltipHandler {

	@SubscribeEvent
	public void onTooltipEvent(ItemTooltipEvent evt) {
		List<String> toolTip = evt.getToolTip();
		if (evt.getItemStack().getItem() == ModItems.supporter_medal) return;

		if (evt.getItemStack().hasTagCompound()) { 
			if (evt.getItemStack().getTagCompound().hasKey("splitresult")) {
				toolTip.add(I18n.format("item.byproduct").replace("%", ""+(evt.getItemStack().getTagCompound().getInteger("splitresult"))));
			}
		}
		
		PotionType pt = PotionUtils.getPotionFromItem(evt.getItemStack());
		
		if (pt instanceof PotionTypeBase) {
			
			String potName = ((PotionTypeBase)pt).getPotion().getName();
			String textRaw = I18n.format("description."+potName);
			String badJoke = I18n.format("funny."+potName);
			
			toolTip.add("");
			toolTip.add(ChatFormatting.GOLD+I18n.format("tooltip.credit", Reference.NAME));	//Added by Extra Alchemy
			
			if (ModConfig.client.showBadJoke) toolTip.add(ChatFormatting.GREEN+ChatFormatting.ITALIC.toString()+I18n.format(badJoke));	//Bad Joke
			
			if (shouldShowHint(evt.getFlags().isAdvanced())) {			//Description or...
				toolTip.add("");
				toolTip.add(textRaw);
				
																			//... Press * to show description
			} else if (!ModConfig.client.descriptionMode.name().equals("NONE")) toolTip.add(I18n.format("tooltip.togglef3."+ModConfig.client.descriptionMode.name()));
		} else if (pt instanceof PotionTypeCompat) {
			toolTip.add("");
			toolTip.add(ChatFormatting.GOLD+String.format(I18n.format("tooltip.credit"), ((PotionTypeCompat)pt).getMod() ));
			toolTip.add(ChatFormatting.GRAY+I18n.format("tooltip.loaded.credit"));
		} else if (!pt.getEffects().isEmpty()){
			String mid = pt.getRegistryName().getResourceDomain();
			String modName = ModIDs.getModName(mid);
			toolTip.add("");
			toolTip.add(ChatFormatting.GOLD+I18n.format("tooltip.credit", modName));
		} else if (evt.getItemStack().hasTagCompound() && evt.getItemStack().getTagCompound().hasKey("brewdata")) {//Covens compat
			NBTTagCompound tag = evt.getItemStack().getTagCompound().getCompoundTag("brewdata");
			if (!tag.getBoolean("spoiled") && tag.hasKey("pot0")) {
				toolTip.add("");
				if (!tag.hasKey("pot1")) {
					String prn = tag.getCompoundTag("pot0").getString("potion");
					String modName = ModIDs.getModName(prn.substring(0, prn.indexOf(':')));
					toolTip.add(ChatFormatting.GOLD+String.format(I18n.format("tooltip.credit"), modName));
				}
			}
		}
	}
	
	private static boolean shouldShowHint(boolean advTooltips) {
		return (advTooltips && ModConfig.client.descriptionMode==Mode.F3H) ||							//F3+H
				(GuiScreen.isAltKeyDown() && ModConfig.client.descriptionMode==Mode.ALT) ||			//ALT
				(GuiScreen.isCtrlKeyDown() && ModConfig.client.descriptionMode==Mode.CTRL) ||			//CTRL
				(GuiScreen.isShiftKeyDown() && ModConfig.client.descriptionMode==Mode.SHIFT);			//SHIFT
	}
}
