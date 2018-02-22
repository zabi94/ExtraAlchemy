package zabi.minecraft.extraalchemy.gui.container.window;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import zabi.minecraft.extraalchemy.gui.container.ContainerPotionBag;
import zabi.minecraft.extraalchemy.lib.Reference;

public class GuiPotionBag extends GuiContainer {

	private final static ResourceLocation texture = new ResourceLocation(Reference.MID, "textures/gui/potion_bag_gui.png");
	private ContainerPotionBag cnt;
	
	public GuiPotionBag(Container cont) {
		super(cont);
		cnt=(ContainerPotionBag) cont;
		this.ySize = 220;
		this.xSize = 176;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float pTicks, int mouseX, int mouseY) {
		drawDefaultBackground();
		mc.getTextureManager().bindTexture(texture);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		String name = cnt.getBagName();
		int size = this.fontRenderer.getStringWidth(name);
		if (size+16>xSize) {
			name = name.substring(0, 25)+"...";
			size = this.fontRenderer.getStringWidth(name);
		}
		int left = 8+(this.xSize - 16 - size)/2;
		this.fontRenderer.drawString(name, this.guiLeft+left, this.guiTop+77, 3216909);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		renderHoveredToolTip(mouseX-guiLeft, mouseY-guiTop);
	}
	
	
	
}
