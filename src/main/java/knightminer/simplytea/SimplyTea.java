package knightminer.simplytea;

import knightminer.simplytea.core.Config;
import knightminer.simplytea.core.Registration;
import knightminer.simplytea.data.SimplyTags;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SimplyTea.MOD_ID)
public class SimplyTea {
	public static final String MOD_ID = "simplytea";

	public SimplyTea() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.serverSpec);
		MinecraftForge.EVENT_BUS.register(this);
		
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		Registration.BLOCKS.register(modEventBus);
		Registration.ITEMS.register(modEventBus);
		Registration.EFFECTS.register(modEventBus);
		Registration.LOOT_MODIFIERS.register(modEventBus);
		Registration.RECIPE_SERIALIZERS.register(modEventBus);
		Registration.FEATURES.register(modEventBus);
		Registration.CONFIGURED_FEATURES.register(modEventBus);
		Registration.PLACED_FEATURES.register(modEventBus);
		
		SimplyTags.init();
	}
}
