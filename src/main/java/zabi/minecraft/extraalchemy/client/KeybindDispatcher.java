package zabi.minecraft.extraalchemy.client;

import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.InputUtil.Type;
import net.minecraft.util.Identifier;
import zabi.minecraft.extraalchemy.utils.LibMod;

public class KeybindDispatcher {
	
	public static final FabricKeyBinding MAGNETISM_TOGGLE = FabricKeyBinding.Builder.create(new Identifier(LibMod.MOD_ID, "magnetism_toggle"), Type.KEYSYM, InputUtil.fromName("key.keyboard.n").getKeyCode(), LibMod.MOD_NAME).build();
	
	public void init() {
		KeyBindingRegistry.INSTANCE.register(MAGNETISM_TOGGLE);
	}
}
