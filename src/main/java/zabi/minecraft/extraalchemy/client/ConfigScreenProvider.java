package zabi.minecraft.extraalchemy.client;

import java.lang.reflect.Field;
import java.util.function.Supplier;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import zabi.minecraft.extraalchemy.config.ConfigInstance;
import zabi.minecraft.extraalchemy.config.ModConfig;
import zabi.minecraft.extraalchemy.utils.LibMod;

public class ConfigScreenProvider implements ModMenuApi {
	
	private static final Supplier<String> SERVER_SIDE = () -> new TranslatableText("extraalchemy.config.serverside").formatted(Formatting.RED, Formatting.BOLD).asFormattedString();
	private static final Supplier<String> CLIENT_SIDE = () ->  new TranslatableText("extraalchemy.config.clientside").formatted(Formatting.AQUA, Formatting.BOLD).asFormattedString();
	
	public static ConfigBuilder builder() {
		
		ConfigBuilder configBuilder = ConfigBuilder.create()
				.setTitle("extraalchemy.mod_name")
				.setEditable(true)
				.setSavingRunnable(() -> ModConfig.writeJson());
		
		ConfigCategory general = configBuilder.getOrCreateCategory("extraalchemy.config.general");
		ConfigCategory potions = configBuilder.getOrCreateCategory("extraalchemy.config.potions");
		
		general.addEntry(configBuilder.entryBuilder()
				.startBooleanToggle("extraalchemy.config.general.enable_learning_boost", ModConfig.INSTANCE.learningIncreasesExpOrbValue)
					.setDefaultValue(true)
					.setTooltip(
							new TranslatableText("extraalchemy.config.general.enable_learning_boost.tooltip1").asFormattedString(),
							new TranslatableText("extraalchemy.config.general.enable_learning_boost.tooltip2").asFormattedString(), 
							SERVER_SIDE.get()
					)
					.setSaveConsumer(val -> {ModConfig.INSTANCE.learningIncreasesExpOrbValue = val;})
					.build()
		);
		
		general.addEntry(configBuilder.entryBuilder()
				.startBooleanToggle("extraalchemy.config.general.disable_inventory_shift", ModConfig.INSTANCE.removeInventoryPotionShift)
					.setDefaultValue(true)
					.setTooltip(
							new TranslatableText("extraalchemy.config.general.disable_inventory_shift.tooltip1").asFormattedString(), 
							new TranslatableText("extraalchemy.config.general.disable_inventory_shift.tooltip2").asFormattedString(),
							CLIENT_SIDE.get()
					)
					.setSaveConsumer(val -> {ModConfig.INSTANCE.removeInventoryPotionShift = val;})
					.build()
		);
		
		general.addEntry(configBuilder.entryBuilder()
				.startBooleanToggle("extraalchemy.config.general.enable_vials" , ModConfig.INSTANCE.enableVials)
					.setDefaultValue(true)
					.setTooltip(
							new TranslatableText("extraalchemy.config.general.enable_vials.tooltip1").asFormattedString(), 
							new TranslatableText("extraalchemy.config.general.enable_vials.tooltip2").asFormattedString(),
							SERVER_SIDE.get()
					)
					.setSaveConsumer(val -> {ModConfig.INSTANCE.enableVials = val;})
					.build()
		);
		
		general.addEntry(configBuilder.entryBuilder()
				.startBooleanToggle("extraalchemy.config.general.enable_rings" , ModConfig.INSTANCE.enableRings)
					.setDefaultValue(true)
					.setTooltip(
							new TranslatableText("extraalchemy.config.general.enable_rings.tooltip1").asFormattedString(), 
							new TranslatableText("extraalchemy.config.general.enable_rings.tooltip2").asFormattedString(),
							SERVER_SIDE.get()
					)
					.setSaveConsumer(val -> {ModConfig.INSTANCE.enableRings = val;})
					.build()
		);
		
		general.addEntry(configBuilder.entryBuilder()
				.startBooleanToggle("extraalchemy.config.general.enable_brewing_stand_fire" , ModConfig.INSTANCE.enableBrewingStandFire)
					.setDefaultValue(true)
					.setTooltip(
							new TranslatableText("extraalchemy.config.general.enable_brewing_stand_fire.tooltip1").asFormattedString(), 
							new TranslatableText("extraalchemy.config.general.enable_brewing_stand_fire.tooltip2").asFormattedString(),
							SERVER_SIDE.get()
					)
					.setSaveConsumer(val -> {ModConfig.INSTANCE.enableBrewingStandFire = val;})
					.build()
		);
		
		try {
			for (Field f:ConfigInstance.Potions.class.getDeclaredFields()) {
				String name = f.getName();
				if (f.getType().isAssignableFrom(boolean.class)) {
					potions.addEntry(configBuilder.entryBuilder()
						.startBooleanToggle("item.minecraft.potion.effect."+name, f.getBoolean(ModConfig.INSTANCE.potions))
							.setDefaultValue(true)
							.requireRestart()
							.setTooltip(
									new TranslatableText("extraalchemy.config.potion.tooltip1", new TranslatableText("item.minecraft.potion.effect."+name)).asFormattedString(), 
									new TranslatableText("extraalchemy.config.potion.tooltip2", new TranslatableText("item.minecraft.potion.effect."+name)).asFormattedString(),
									SERVER_SIDE.get()
							)
							.setSaveConsumer(val -> {try {
								f.setBoolean(ModConfig.INSTANCE.potions, val);
							} catch (Exception e) {
								e.printStackTrace();
							}})
							.build()
							
					);
				}
			}
		} catch (IllegalAccessException iae) {
			iae.printStackTrace();
		}
		return configBuilder;
	}
	
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> {
			return builder().setParentScreen(parent).build();
		};
	}

	@Override
	public String getModId() {
		return LibMod.MOD_ID;
	}

}
