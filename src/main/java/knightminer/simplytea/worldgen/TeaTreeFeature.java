package knightminer.simplytea.worldgen;

import knightminer.simplytea.block.TeaSaplingBlock;
import knightminer.simplytea.core.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class TeaTreeFeature extends Feature<NoneFeatureConfiguration> {
	public TeaTreeFeature() {
		super(NoneFeatureConfiguration.CODEC);
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> pContext) {
		BlockPos down = pContext.origin().below();
		BlockState soil = pContext.level().getBlockState(down);
		if(soil.canSustainPlant(pContext.level(), down, Direction.UP, Registration.tea_sapling.get()) && pContext.level().getBlockState(pContext.origin()).isAir()) {
			TeaSaplingBlock.generateTree(pContext.level(), pContext.origin(), pContext.random());
			return true;
		}
		return false;
	}
}