package zabi.minecraft.extraalchemy.utils;

import net.minecraft.util.Identifier;

public class LibMod {

	public static final String MOD_NAME = "Extra Alchemy";
	public static final String MOD_ID = "extraalchemy";

	public static Identifier id(String name) {
		return Identifier.of(MOD_ID, name);
	}
	
}
