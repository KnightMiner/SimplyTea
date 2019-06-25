package knightminer.simplytea;

import knightminer.simplytea.core.Config;
import knightminer.simplytea.core.Registration;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SimplyTea.MOD_ID)
public class SimplyTea {
	public static final String MOD_ID = "simplytea";
	public static SimplyTea instance;

	public SimplyTea() {
		instance = this;
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
		MinecraftForge.EVENT_BUS.register(this);
	}

	public void init(final FMLCommonSetupEvent event) {
		Config.parse();

		if (Registration.tea_tree != null) {
			BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST).forEach((biome) -> {
				biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(Registration.tea_tree, IFeatureConfig.NO_FEATURE_CONFIG, Placement.DARK_OAK_TREE, IPlacementConfig.NO_PLACEMENT_CONFIG));
			});
		}
	}
}
