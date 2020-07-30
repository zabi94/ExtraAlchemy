package zabi.minecraft.extraalchemy.screen.potion_bag;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import zabi.minecraft.extraalchemy.utils.LibMod;

public class PotionBagScreen extends HandledScreen<PotionBagScreenHandler> {

	private static final Identifier TEXTURE = LibMod.id("textures/gui/potion_bag_gui.png");

	private final PotionBagScreenHandler handler;

	public PotionBagScreen(Text title, PotionBagScreenHandler handler, PlayerInventory pinv) {
		super(handler, pinv, title);
		this.handler = handler;
	}

	@Override
	protected void init() {
		this.backgroundHeight = 221;
		this.backgroundWidth = 176;
		super.init();
	}

	@Override
	public PotionBagScreenHandler getScreenHandler() {
		return handler;
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		this.renderBackground(matrices);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.client.getTextureManager().bindTexture(TEXTURE);
		int i = (this.width - this.backgroundWidth) / 2;
		int j = (this.height - this.backgroundHeight) / 2;
		this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
	}
	
	@Override
	protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
		super.drawForeground(matrices, mouseX, mouseY);
		this.drawMouseoverTooltip(matrices, mouseX-this.x, mouseY-this.y);
	}

}
