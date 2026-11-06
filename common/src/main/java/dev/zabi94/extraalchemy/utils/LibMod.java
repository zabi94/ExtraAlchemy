package dev.zabi94.extraalchemy.utils;

import net.minecraft.resources.Identifier;

public class LibMod {

	public static final String MOD_NAME = "Extra Alchemy";
	public static final String MOD_ID = "extraalchemy";

	public static Identifier id(String name) {
		return Identifier.fromNamespaceAndPath(MOD_ID, name);
	}
	
}
