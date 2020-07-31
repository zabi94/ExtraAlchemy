package zabi.minecraft.extraalchemy.screen.potion_bag;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.ingame.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import zabi.minecraft.extraalchemy.utils.LibMod;

public class PotionBagScreen extends ContainerScreen<PotionBagContainer> {

	private static final Identifier TEXTURE = LibMod.id("textures/gui/potion_bag_gui.png");

	private final PotionBagContainer handler;

	public PotionBagScreen(Text title, PotionBagContainer handler, PlayerInventory pinv) {
		super(handler, pinv, title);
		this.handler = handler;
	}

	@Override
	protected void init() {
		this.containerHeight = 221;
		this.containerWidth = 176;
		super.init();
	}

	@Override
	public PotionBagContainer getContainer() {
		return handler;
	}

	@Override
	protected void drawBackground(float delta, int mouseX, int mouseY) {
		this.renderBackground();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(TEXTURE);
		int i = (this.width - this.containerWidth) / 2;
		int j = (this.height - this.containerHeight) / 2;
		this.blit(i, j, 0, 0, this.containerWidth, this.containerHeight);
	}
	
	@Override
	protected void drawForeground(int mouseX, int mouseY) {
		super.drawForeground(mouseX, mouseY);
		this.drawMouseoverTooltip(mouseX-this.x, mouseY-this.y);
	}

}
