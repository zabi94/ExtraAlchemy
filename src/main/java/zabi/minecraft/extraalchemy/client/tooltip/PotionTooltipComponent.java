package zabi.minecraft.extraalchemy.client.tooltip;

import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.StatusEffectSpriteManager;
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
	public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
		if (!ModConfig.INSTANCE.showIconsInTooltips) return;
		MinecraftClient mc = MinecraftClient.getInstance();
		context.getMatrices().push();
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
			this.draw(context, dx, dy, 0, icon);
			if (level > 1) {
				Text txt = Text.translatable("enchantment.level."+level).formatted(eff.getEffectType().getCategory().getFormatting());
				int tx = dx + TEXTURE_SIZE - textRenderer.getWidth(txt)/2;
				int ty = dy + TEXTURE_SIZE - textRenderer.fontHeight/2;
				context.getMatrices().translate(0, 0, 400);
				context.drawTextWithShadow(textRenderer, txt, tx, ty, 0xFFFFFFFF);
				context.getMatrices().translate(0, 0, -400);
			}
		}
		context.getMatrices().pop();
	}
	
    private void draw(DrawContext context, int x, int y, int z, Sprite icon) {
        RenderSystem.setShaderTexture(0, icon.getAtlasId());
        context.drawSprite(x, y, z, 18, 18, icon);
    }

}
