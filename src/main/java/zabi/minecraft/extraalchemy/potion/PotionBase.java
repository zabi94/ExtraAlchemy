package zabi.minecraft.extraalchemy.potion;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import zabi.minecraft.extraalchemy.lib.Reference;

@Mod.EventBusSubscriber
public abstract class PotionBase extends Potion {

	private static final ArrayList<Potion> recorder = new ArrayList<Potion>();
	public static final ResourceLocation EXTRA_EFFECTS_ALT = new ResourceLocation(Reference.MID, "textures/icons/potions_alt.png");
	
	public PotionBase(boolean isBadEffectIn, int liquidColorIn, String name) {
		super(isBadEffectIn, liquidColorIn);
		this.setPotionName("effect."+name);
		if (!isBadEffectIn) this.setBeneficial();
		this.setRegistryName(new ResourceLocation(Reference.MID, "effect."+name));
		recorder.add(this);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getStatusIconIndex() {
		Minecraft.getMinecraft().renderEngine.bindTexture(EXTRA_EFFECTS_ALT);
		return super.getStatusIconIndex();
	}
	
	@SubscribeEvent
	public static void register(RegistryEvent.Register<Potion> evt) {
		IForgeRegistry<Potion> reg = evt.getRegistry();
		for (Potion p:recorder) reg.register(p);
	}
	
}
