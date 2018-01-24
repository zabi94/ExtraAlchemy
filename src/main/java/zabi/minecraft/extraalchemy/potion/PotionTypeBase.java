package zabi.minecraft.extraalchemy.potion;

import java.util.ArrayList;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import zabi.minecraft.extraalchemy.lib.Reference;

@Mod.EventBusSubscriber
public class PotionTypeBase extends PotionType {
	
	private static final ArrayList<PotionTypeBase> recorder = new ArrayList<PotionTypeBase>();
	
	private Potion type;
	
	public PotionTypeBase(Potion potion, int duration, int amplifier, String effectName) {
		super(potion.getName().substring(7), new PotionEffect(potion, duration, amplifier));
		this.setRegistryName(new ResourceLocation(Reference.MID, effectName));
		type=potion;
		recorder.add(this);
	}
	
	public Potion getPotion() {
		return type;
	}
	
	@SubscribeEvent
	public static void register(RegistryEvent.Register<PotionType> evt) {
		IForgeRegistry<PotionType> reg = evt.getRegistry();
		for (PotionTypeBase p:recorder) reg.register(p);
	}
}
