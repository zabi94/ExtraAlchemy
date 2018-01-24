package zabi.minecraft.extraalchemy.gui.config;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import zabi.minecraft.extraalchemy.lib.Config;
import zabi.minecraft.extraalchemy.lib.Reference;

public class GuiConfigEA extends GuiConfig {
	
	public GuiConfigEA(GuiScreen parentScreen) {
		super(	parentScreen, 
				getConfigElements(), 
				Reference.MID, 
				null, 
				false, 
				false, 
				Reference.NAME, 
				null);
	}

	private static List<IConfigElement> getConfigElements() {
		ArrayList<IConfigElement> list = new ArrayList<IConfigElement>();
		list.add(new ConfigElement(Config.general));
		list.add(new ConfigElement(Config.visual));
		return list;
	}
}
