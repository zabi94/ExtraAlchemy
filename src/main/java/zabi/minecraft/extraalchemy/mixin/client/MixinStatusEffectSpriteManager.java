package zabi.minecraft.extraalchemy.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasHolder;
import net.minecraft.client.texture.StatusEffectSpriteManager;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import zabi.minecraft.extraalchemy.entitydata.PlayerProperties;
import zabi.minecraft.extraalchemy.statuseffect.ModEffectRegistry;

@Mixin(StatusEffectSpriteManager.class)
public abstract class MixinStatusEffectSpriteManager extends SpriteAtlasHolder  {

	public MixinStatusEffectSpriteManager(TextureManager textureManager, Identifier atlasId, Identifier identifier) {
		super(textureManager, atlasId, identifier);
	}
	
	@Inject(at = @At("HEAD"), cancellable = true, method = "getSprite")
	public void injectGetSprite(StatusEffect statusEffect, CallbackInfoReturnable<Sprite> cbinfo) {
		if (statusEffect == ModEffectRegistry.magnetism && !PlayerProperties.of(MinecraftClient.getInstance().player).isMagnetismEnabled()) {
			cbinfo.setReturnValue(this.getSprite(Registries.STATUS_EFFECT.getId(ModEffectRegistry.Utils.magnetism_disabled)));
		}
	}

}
