package zabi.minecraft.extraalchemy.asm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.Name;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
import zabi.minecraft.extraalchemy.lib.Log;

@Name("Extra Alchemy ASM")
@TransformerExclusions({
		"zabi.minecraft.extraalchemy.asm"
	})
public class ExtraAlchemyLoadingPlugin implements IFMLLoadingPlugin {
	
	private static boolean foundConfig = false; 
	
	@Override
	public String[] getASMTransformerClass() {
			return new String[] {
					"zabi.minecraft.extraalchemy.asm.PotionTransformer"
			};
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		Log.i("Preloading config in ASM Plugin");
		String mcloc = ((File)data.get("mcLocation")).toString();
		File cfg = new File(mcloc+File.separatorChar+"config"+File.separatorChar+"extraalchemy.cfg");

		if (cfg.exists() && cfg.isFile()) {

			BufferedReader in = null;

			try {
				in = new BufferedReader(new FileReader(cfg));
				in.lines().map(s -> s.trim().toLowerCase()).filter(s -> s.startsWith("b:removeglowingeffect")).map(s -> s.replace(" ", "")).forEach(s->{
					Log.i("Found valid config string: "+s);
					if (s.endsWith("false")) PotionTransformer.removeGlowingEffect = false;
					Log.i("Potions should "+(PotionTransformer.removeGlowingEffect?"not ":"")+"glow");
					foundConfig = true;
				});
				
				if (!foundConfig) {
					Log.w("Couldn't find config for potion glow. Defaulting to true");
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			Log.i("No config file found, assuming first installation");
		}
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

}
