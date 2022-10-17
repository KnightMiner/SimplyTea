package knightminer.simplytea.worldgen;

import knightminer.simplytea.block.TeaSaplingBlock;
import knightminer.simplytea.core.Registration;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Random;

public class TeaTreeFeature extends Feature<NoneFeatureConfiguration> {
	public TeaTreeFeature() {
		super(NoneFeatureConfiguration.CODEC);
	}

	@Override
	public boolean place(WorldGenLevel world, ChunkGenerator generator, Random random, BlockPos pos, NoneFeatureConfiguration config) {
		BlockPos down = pos.below();
		BlockState soil = world.getBlockState(down);
		if(soil.canSustainPlant(world, down, Direction.UP, Registration.tea_sapling) && world.getBlockState(pos).isAir(world, pos)) {
			TeaSaplingBlock.generateTree(world, pos, random);
			return true;
		}
		return false;
	}
}