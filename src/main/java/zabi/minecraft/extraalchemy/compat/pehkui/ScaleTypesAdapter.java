package zabi.minecraft.extraalchemy.compat.pehkui;

import virtuoel.pehkui.api.ScaleType;

public class ScaleTypesAdapter {
	
	public static ScaleType BASE = null;
	
	public static void load() {
		try {
			BASE = (ScaleType) Class.forName("virtuoel.pehkui.api.ScaleTypes").getField("BASE").get(null);
		} catch (Exception e) {
			try {
				BASE = (ScaleType) Class.forName("virtuoel.pehkui.api.ScaleType").getField("BASE").get(null);
			} catch (Exception e1) {
				throw new RuntimeException(e1);
			}
		}
	}

}
