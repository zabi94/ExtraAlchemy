package zabi.minecraft.extraalchemy.client.tooltip;

import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.StatusEffectSpriteManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import zabi.minecraft.extraalchemy.config.ModConfig;

public class PotionTooltipComponent implements TooltipComponent {
	
	private static final int ICONS_PER_ROW = 3;
	private static final int TEXTURE_SIZE = 16;
	private static final int TEXTURE_SPACING = 2;
	
	private final List<StatusEffectInstance> effects; 
	
	public PotionTooltipComponent(StatusEffectContainer container, ItemStack stack) {
		this.effects = container.getContainedEffects(stack);
	}
	
	@Override
	public int getHeight() {
		if (!ModConfig.INSTANCE.showIconsInTooltips) return 0;
		return ((effects.size() / ICONS_PER_ROW) + 1) * (TEXTURE_SIZE + TEXTURE_SPACING) + TEXTURE_SPACING * 2;
	}	

	@Override
	public int getWidth(TextRenderer var1) {
		if (!ModConfig.INSTANCE.showIconsInTooltips) return 0;
		return (ICONS_PER_ROW * (TEXTURE_SIZE + TEXTURE_SPACING)) + TEXTURE_SPACING;
	}
	
	@Override
	public void drawItems(TextRenderer textRenderer, int x, int y, MatrixStack matrices, ItemRenderer itemRenderer, int z) {
		if (!ModConfig.INSTANCE.showIconsInTooltips) return;
		MinecraftClient mc = MinecraftClient.getInstance();
		StatusEffectSpriteManager statusEffectSpriteManager = mc.getStatusEffectSpriteManager();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		for (int i = 0; i < effects.size(); i++) {
			StatusEffectInstance eff = effects.get(i);
			Sprite icon = statusEffectSpriteManager.getSprite(eff.getEffectType());
			int level = 1 + eff.getAmplifier();
			int column = i % ICONS_PER_ROW;
			int row = i / ICONS_PER_ROW;
			int dx = x + (column * (TEXTURE_SIZE + TEXTURE_SPACING));
			int dy = y + (row * (TEXTURE_SIZE + TEXTURE_SPACING));
			this.draw(matrices, dx, dy, z, icon);
			if (level > 1) {
				Text txt = Text.translatable("enchantment.level."+level).formatted(eff.getEffectType().getCategory().getFormatting());
				float tx = dx + TEXTURE_SIZE - textRenderer.getWidth(txt)/2;
				float ty = dy + TEXTURE_SIZE - textRenderer.fontHeight/2;
				matrices.translate(0, 0, 400);
				textRenderer.drawWithShadow(matrices, txt, tx, ty, 0xFFFFFFFF);
				matrices.translate(0, 0, -400);
			}
		}
	}
	
    private void draw(MatrixStack matrices, int x, int y, int z, Sprite icon) {
        RenderSystem.setShaderTexture(0, icon.getAtlasId());
        DrawableHelper.drawSprite(matrices, x, y, z, TEXTURE_SIZE, TEXTURE_SIZE, icon);
    }

}
