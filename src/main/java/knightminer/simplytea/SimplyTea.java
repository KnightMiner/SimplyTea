package knightminer.simplytea;

import javafx.scene.effect.Effect;
import knightminer.simplytea.core.Config;
import knightminer.simplytea.data.SimplyTags;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import java.util.HashSet;
import java.util.Set;

@Mod(SimplyTea.MOD_ID)
public class SimplyTea {
	public static final String MOD_ID = "simplytea";
	public static final ToolType SHEAR_TYPE = ToolType.get("shears");
	public static SimplyTea instance;
	private static final Set<Effect> TEA_EFFECTS = new HashSet<>();

	public SimplyTea() {
		instance = this;

		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.serverSpec);
		MinecraftForge.EVENT_BUS.register(this);
		SimplyTags.init();
	}
}
