package zabi.minecraft.extraalchemy.proxy;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import zabi.minecraft.extraalchemy.client.ModKeybinds;
import zabi.minecraft.extraalchemy.client.PotionDescriptionTooltipHandler;
import zabi.minecraft.extraalchemy.items.ModItems;

public class ClientProxy extends Proxy {
	
	@Override
	public void updatePlayerPotion(EntityPlayer e, PotionEffect fx) {
		e.addPotionEffect(new PotionEffect(fx));
	}

	@Override
	public void registerEventHandler() {
		MinecraftForge.EVENT_BUS.register(new ModKeybinds());
	}
	

	@Override
	public void registerItemDescriptions() {
		MinecraftForge.EVENT_BUS.register(new PotionDescriptionTooltipHandler());		
	}

	@Override
	public void registerColorHandler() {
		ItemColors colors = Minecraft.getMinecraft().getItemColors();

		colors.registerItemColorHandler(new IItemColor() {
			@Override
			public int colorMultiplier(ItemStack stack, int tintIndex) {
				return tintIndex == 0 ? PotionUtils.getPotionColorFromEffectList(PotionUtils.getEffectsFromStack(stack)) : -1;
			}
		}, new Item[] {ModItems.breakable_potion, ModItems.modified_potion});

	}
	
	@Override
	public void registerItemModel(Item i) {
		ModelResourceLocation mrl = new ModelResourceLocation(new ResourceLocation(i.getRegistryName().toString()), "inventory");
		ModelLoader.setCustomModelResourceLocation(i, 0, mrl);		
	}

	@Override
	public int getRainbow(int defaultColor) {
		if (Minecraft.getMinecraft().world!=null && Minecraft.getMinecraft().player!=null) {
			return Color.HSBtoRGB(Minecraft.getMinecraft().world.getTotalWorldTime() % 180 / 180f, 1f, 1f);
		} else return defaultColor;
	}

	@Override
	public boolean isShiftingInInv() {
		return GuiScreen.isShiftKeyDown();
	}

	@Override
	public void playDispelSound() {
		EntityPlayer player = Minecraft.getMinecraft().player;
		player.getEntityWorld().playSound(player.posX, player.posY, player.posZ, SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.elder_guardian.curse")), SoundCategory.AMBIENT, 1.5F, 8F, false);
	}

	@Override
	public EntityPlayer getSP() {
		return Minecraft.getMinecraft().player;
	}
	
}
