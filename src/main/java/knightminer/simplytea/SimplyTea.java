package knightminer.simplytea;

import knightminer.simplytea.core.Config;
import knightminer.simplytea.data.SimplyTags;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(SimplyTea.MOD_ID)
public class SimplyTea {
	public static final String MOD_ID = "simplytea";
	public static SimplyTea instance;

	public SimplyTea() {
		instance = this;

		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.serverSpec);
		MinecraftForge.EVENT_BUS.register(this);
		
		SimplyTags.init();
	}
}
