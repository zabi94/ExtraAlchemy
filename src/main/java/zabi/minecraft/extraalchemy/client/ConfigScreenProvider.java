package zabi.minecraft.extraalchemy.client;

import java.lang.reflect.Field;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.util.Formatting;
import zabi.minecraft.extraalchemy.config.ConfigInstance;
import zabi.minecraft.extraalchemy.config.ModConfig;
import zabi.minecraft.extraalchemy.utils.LibMod;

public class ConfigScreenProvider implements ModMenuApi {
	
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
							I18n.translate("extraalchemy.config.general.enable_learning_boost.tooltip1"), 
							I18n.translate("extraalchemy.config.general.enable_learning_boost.tooltip2"), 
							Formatting.RED.toString()+Formatting.BOLD+I18n.translate("extraalchemy.config.serverside")
					)
					.setSaveConsumer(val -> {ModConfig.INSTANCE.learningIncreasesExpOrbValue = val;})
					.build()
		);
		
		general.addEntry(configBuilder.entryBuilder()
				.startBooleanToggle("extraalchemy.config.general.disable_inventory_shift", ModConfig.INSTANCE.removeInventoryPotionShift)
					.setDefaultValue(true)
					.setTooltip(
							I18n.translate("extraalchemy.config.general.disable_inventory_shift.tooltip1"), 
							I18n.translate("extraalchemy.config.general.disable_inventory_shift.tooltip2"), 
							Formatting.AQUA.toString()+Formatting.BOLD+I18n.translate("extraalchemy.config.clientside")
					) 
					.setSaveConsumer(val -> {ModConfig.INSTANCE.removeInventoryPotionShift = val;})
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
									I18n.translate("extraalchemy.config.potion.tooltip1"), 
									I18n.translate("extraalchemy.config.potion.tooltip2"), 
									Formatting.RED.toString()+Formatting.BOLD+I18n.translate("extraalchemy.config.serverside")
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
