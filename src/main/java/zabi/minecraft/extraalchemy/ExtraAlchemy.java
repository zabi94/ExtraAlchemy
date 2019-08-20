package zabi.minecraft.extraalchemy;

import java.io.File;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import zabi.minecraft.extraalchemy.ModConfig.ChangeListener;
import zabi.minecraft.extraalchemy.blocks.BrewingStandFire;
import zabi.minecraft.extraalchemy.capability.MagnetismStatus;
import zabi.minecraft.extraalchemy.gui.GuiHandler;
import zabi.minecraft.extraalchemy.integration.BotaniaHandler;
import zabi.minecraft.extraalchemy.items.TabExtraAlchemy;
import zabi.minecraft.extraalchemy.lib.Log;
import zabi.minecraft.extraalchemy.lib.Reference;
import zabi.minecraft.extraalchemy.network.NetworkModRegistry;
import zabi.minecraft.extraalchemy.potion.PotionReference;
import zabi.minecraft.extraalchemy.potion.potion.PotionBeheading;
import zabi.minecraft.extraalchemy.potion.potion.PotionCheatDeath;
import zabi.minecraft.extraalchemy.potion.potion.PotionCombustion;
import zabi.minecraft.extraalchemy.potion.potion.PotionHurry;
import zabi.minecraft.extraalchemy.potion.potion.PotionLeech;
import zabi.minecraft.extraalchemy.potion.potion.PotionMagnetism;
import zabi.minecraft.extraalchemy.potion.potion.PotionPacifism;
import zabi.minecraft.extraalchemy.potion.potion.PotionPain;
import zabi.minecraft.extraalchemy.potion.potion.PotionReincarnation;
import zabi.minecraft.extraalchemy.proxy.Proxy;
import zabi.minecraft.extraalchemy.recipes.Recipes;
import zabi.minecraft.extraalchemy.recipes.brew.BrewingStandBlocker;
import zabi.minecraft.extraalchemy.recipes.crafting.StickyPotionRecipeHandler;
import zabi.minecraft.minerva.common.capability.SimpleCapability;

@Mod(
		name=Reference.NAME, 
		modid=Reference.MID, 
		version=Reference.VERSION, 
		dependencies="required-after:minerva@[1.0.13,);"
				+ "after:botania", 
		acceptedMinecraftVersions="[1.12,1.13)")
public class ExtraAlchemy {

	public static TabExtraAlchemy TAB;
	
	public static SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MID);
	
	@SidedProxy(modId=Reference.MID, clientSide=Reference.PROXY_CLIENT, serverSide=Reference.PROXY_SERVER)
	public static Proxy proxy;
	
	public static String recipesfile;
	
	@Instance
	public static ExtraAlchemy instance;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		PotionReference.INSTANCE.getClass(); //Load static class
		proxy.registerEventHandler();
		MinecraftForge.EVENT_BUS.register(new PotionPacifism.PacifismHandler());
		MinecraftForge.EVENT_BUS.register(new PotionMagnetism.MagnetismHandler());
		MinecraftForge.EVENT_BUS.register(new PotionCheatDeath.PotionCheatDeathHandler());
		MinecraftForge.EVENT_BUS.register(new PotionReincarnation.PotionReincarnationHandler());
		MinecraftForge.EVENT_BUS.register(new PotionCombustion.PotionCombustionHandler());
		MinecraftForge.EVENT_BUS.register(new PotionLeech.PotionLeechHandler());
		MinecraftForge.EVENT_BUS.register(new PotionBeheading.PotionBeheadingHandler());
		MinecraftForge.EVENT_BUS.register(new PotionPain.PotionPainHandler());
		MinecraftForge.EVENT_BUS.register(new BrewingStandBlocker());
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
		SimpleCapability.preInit(MagnetismStatus.class);
		if (ModConfig.options.useFireUndernathBrewingStand) {
			MinecraftForge.EVENT_BUS.register(new BrewingStandFire());
		}
		Log.i("Registering Network Protocol");
		NetworkModRegistry.registerMessages(network);
		
		proxy.registerItemDescriptions();
		recipesfile = event.getSuggestedConfigurationFile().getParent()+File.separatorChar+"extra_alchemy_recipes.cfg";
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		BotaniaHandler.checkLoadBotania();
		Recipes.registerRecipes();
		ExtraAlchemy.proxy.registerColorHandler();
		if (ModConfig.options.addSeparateTab) {
			TAB = new TabExtraAlchemy();
		}
		SimpleCapability.init(MagnetismStatus.class, Reference.MID, MagnetismStatus.CAPABILITY, MagnetismStatus.DEFAULT);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new ChangeListener());
		if (ModConfig.options.log_potion_types) {
			Log.i("\n\n\n-----------------------------v- LOGGING POTIONS -v-----------------------------\n\n\n");
			PotionType.REGISTRY.forEach(pt -> {
				Log.i(pt.getRegistryName().toString());
			});
			Log.i("\n\n\n-----------------------------^- LOGGING POTIONS -^-----------------------------\n\n\n");			
		}
	}
	
	@EventHandler
	public void fingerprintError(FMLFingerprintViolationEvent evt) {
		Log.w("");
		Log.w("");
		Log.w("");
		Log.w("Fingerprint on Extra Alchemy doesn't match");
		Log.w("This may be due to the mod file being modified illegally");
		Log.w("Make sure to only download mod from a reliable source, like");
		Log.w("https://minecraft.curseforge.com/");
		Log.w("");
		Log.w("");
		Log.w("");
	}

	@EventHandler
	public void receiveIMC(FMLInterModComms.IMCEvent event) {
		for (final FMLInterModComms.IMCMessage imcMessage : event.getMessages()) {
			if (imcMessage.key.equalsIgnoreCase("sticky-blacklist")) {
				if (imcMessage.isStringMessage()) {
					StickyPotionRecipeHandler.potionBlacklist.add(imcMessage.getStringValue());
					Log.i(imcMessage.getStringValue()+" has been added to blacklist");
				} else if (imcMessage.isItemStackMessage()) {
					ItemStack is = imcMessage.getItemStackValue();
					List<PotionEffect> listaEffetti = PotionUtils.getEffectsFromStack(is);
					if (listaEffetti.isEmpty()) {
						Log.i(imcMessage.getSender()+" tried to blacklist a potion, but the itemstack has no effects");
						continue;
					}
					if (listaEffetti.size()>1) {
						Log.i(imcMessage.getSender()+" tried to blacklist a potion, but the itemstack has more than one effect");
						continue;
					}
					StickyPotionRecipeHandler.potionBlacklist.add(listaEffetti.get(0).getPotion().getName());
					Log.i(listaEffetti.get(0).getPotion().getName()+" has been added to blacklist");
				} else {
					Log.i(imcMessage.getSender()+" tried to blacklist a potion, but only String and Itemstack messages are supported");
				}
			} else
			
			if (imcMessage.key.equalsIgnoreCase("damage-blacklist")) {
				if (imcMessage.isStringMessage()) {
					PotionCheatDeath.blacklist.add(imcMessage.getStringValue());
					Log.i(imcMessage.getStringValue()+" has been added to damage blacklist");
				} else {
					Log.i(imcMessage.getSender()+" tried to blacklist a damage source, but it is not a string. Use damageSourceObj.damageType");
					continue;
				}
			} else
			
			if (imcMessage.key.equalsIgnoreCase("hurry-blacklist")) {
				if (imcMessage.isStringMessage()) {
					PotionHurry.blacklist.add(imcMessage.getStringValue());
					Log.i(imcMessage.getStringValue()+" has been added to hurry blacklist");
				} else {
					Log.i(imcMessage.getSender()+" tried to blacklist an ITicking entity, but it is not a string. Use TileEntityClass.getName()");
					continue;
				}
			}
		}
	}
}
