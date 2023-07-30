package zabi.minecraft.extraalchemy.screen.potion_bag;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
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
		MinecraftClient mc = MinecraftClient.getInstance();
		this.titleX = (this.backgroundWidth - mc.textRenderer.getWidth(this.title))/2;
		super.init();
	}

	@Override
	public PotionBagScreenHandler getScreenHandler() {
		return handler;
	}

	@Override
	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
		this.renderBackground(context);
		RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		int i = (this.width - this.backgroundWidth) / 2;
		int j = (this.height - this.backgroundHeight) / 2;
		context.drawTexture(TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
	}
	
	@Override
	protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
		context.drawText(this.textRenderer, this.title, this.titleX, this.titleY, 4210752, false);
		this.drawMouseoverTooltip(context, mouseX-this.x, mouseY-this.y);
	}
	
	@Override
	protected void onMouseClick(Slot slot, int invSlot, int clickData, SlotActionType actionType) {
		if (actionType != SlotActionType.SWAP) {
			super.onMouseClick(slot, invSlot, clickData, actionType);
		}
	}

}
