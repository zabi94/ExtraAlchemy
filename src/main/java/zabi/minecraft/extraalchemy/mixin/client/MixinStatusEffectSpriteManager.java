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
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import zabi.minecraft.extraalchemy.entitydata.PlayerProperties;
import zabi.minecraft.extraalchemy.statuseffect.ModEffectRegistry;

@Mixin(StatusEffectSpriteManager.class)
public abstract class MixinStatusEffectSpriteManager extends SpriteAtlasHolder  {

	public MixinStatusEffectSpriteManager(TextureManager textureManager, Identifier identifier, String string) {
		super(textureManager, identifier, string);
	}
	
	@Inject(at = @At("HEAD"), cancellable = true, method = "getSprite")
	public void injectGetSprite(StatusEffect statusEffect, CallbackInfoReturnable<Sprite> cbinfo) {
		if (statusEffect == ModEffectRegistry.magnetism && !PlayerProperties.of(MinecraftClient.getInstance().player).isMagnetismEnabled()) {
			cbinfo.setReturnValue(this.getSprite(Registry.STATUS_EFFECT.getId(ModEffectRegistry.Utils.magnetism_disabled)));
		}
	}

}
