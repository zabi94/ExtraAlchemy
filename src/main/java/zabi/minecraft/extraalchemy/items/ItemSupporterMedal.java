package zabi.minecraft.extraalchemy.items;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.io.IOUtils;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zabi.minecraft.extraalchemy.lib.Log;
import zabi.minecraft.extraalchemy.lib.Reference;

public class ItemSupporterMedal extends Item {

	public static final ArrayList<String> contributorsUUIDs = new ArrayList<String>();

	public ItemSupporterMedal() {
		this.setMaxStackSize(1);
		this.setCreativeTab(null);
		this.setRegistryName(new ResourceLocation(Reference.MID, "supporter_medal"));
		MinecraftForge.EVENT_BUS.register(this);
		if (!"true".equals(System.getProperty("eanosupport"))) {
			Thread poller = new Thread(new ContributorsPoll());
			poller.setDaemon(true);
			poller.setName("Extra Alchemy Contributor Poll");
			poller.start();
		} else {
			Log.i("Contributors fetching disabled :'( ");
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		String name = (stack.getTagCompound()!=null?stack.getTagCompound().getString("supporter"):"cheater");
		if (name.equals("cheater")) return "This medal is for contributors only!";
		return "Thank you "+name+"!";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		String name = stack.getTagCompound()!=null?stack.getTagCompound().getString("supporter"):"cheater";
		if (name.equals("")) return;
		if (name.equals("cheater")) {
			tooltip.add("If you really want this medal support the mod!");
			tooltip.add("(Support means helping in ANY way, e.g. translating, donating, texturing...)");
			tooltip.add("https://minecraft.curseforge.com/projects/extra-alchemy");
		} else {
			if (!hasEffect(stack)) tooltip.add("Thanks "+name+" for supporting Extra Alchemy!");
			else tooltip.add(TextFormatting.RED+"Hello Master");
			if (!PotionUtils.getEffectsFromStack(stack).isEmpty()) tooltip.add("Right click to clear the color");
		}
	}

	@Override
	public int getEntityLifespan(ItemStack itemStack, World world) {
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean hasEffect(ItemStack stack) {
		String name = (stack.getTagCompound()!=null?stack.getTagCompound().getString("supporter"):"");
		if (name.equals("zabi94")) return true;
		return false;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand) {
		ItemStack itemStackIn = player.getHeldItem(hand);
		if (itemStackIn.hasTagCompound() && !PotionUtils.getEffectsFromStack(itemStackIn).isEmpty()) {
			itemStackIn.getTagCompound().removeTag("Potion");
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
		}
		return super.onItemRightClick(worldIn, player, hand);
	}

	private static final String GAVE_MEDAL_TAG = "gave_medal_ea";

	@SubscribeEvent
	public void onPlayerJoiningFirst(EntityJoinWorldEvent evt) {
		if (!evt.getWorld().isRemote && evt.getEntity() instanceof EntityPlayer) {
			EntityPlayer p = (EntityPlayer) evt.getEntity();
			if (!p.getTags().contains(GAVE_MEDAL_TAG) && contributorsUUIDs.contains(p.getUniqueID().toString().replace("-", ""))) {
				ItemStack medal = new ItemStack(ModItems.supporter_medal);
				medal.setTagCompound(new NBTTagCompound());
				medal.getTagCompound().setString("supporter", p.getDisplayNameString());
				EntityItem ent = new EntityItem(evt.getWorld(), evt.getEntity().posX,  evt.getEntity().posY+1,  evt.getEntity().posZ, medal);
				evt.getWorld().spawnEntity(ent);
				p.addTag(GAVE_MEDAL_TAG);
			}
		}
	}

	public static class ContributorsPoll implements Runnable {

		private static final String CONTRIBUTORS_URL = "http://zabi.altervista.org/minecraft/"+Reference.MID+"/ringraziamenti";

		@Override
		public void run() {
			InputStream in = null;
			try {
				in = new URL(CONTRIBUTORS_URL).openStream();
				List<String> contributorData = IOUtils.readLines(in, Charset.defaultCharset());
				contributorData.parallelStream()
				.filter(s -> !s.startsWith("#"))
				.forEach(s -> {
					ItemSupporterMedal.contributorsUUIDs.add(s);
					Log.d("Supporter UUID: "+s);
				});
				Log.i("Contributors updated! Found "+contributorData.size()/2+" contributors");
			} catch (IOException e) {
				Log.d("error during contributors poll: ");
				Log.d(e);
			} finally {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}

	}


}
