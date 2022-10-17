package knightminer.simplytea.worldgen;

import knightminer.simplytea.block.TeaSaplingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class TeaTreeFeature extends Feature<NoneFeatureConfiguration> {
	public TeaTreeFeature() {
		super(NoneFeatureConfiguration.CODEC);
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		BlockPos pos = context.origin();
		BlockPos down = pos.below();
		WorldGenLevel world = context.level();
		BlockState soil = world.getBlockState(down);
		if (world.getBlockState(pos).isAir()) {
			TeaSaplingBlock.generateTree(world, pos, context.random());
		}
		return false;
	}
}