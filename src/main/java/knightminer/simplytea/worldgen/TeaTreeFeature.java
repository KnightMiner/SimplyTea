package knightminer.simplytea.worldgen;

import knightminer.simplytea.block.TeaSaplingBlock;
import knightminer.simplytea.core.Registration;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;

public class TeaTreeFeature extends Feature<NoFeatureConfig> {
	public TeaTreeFeature() {
		super(NoFeatureConfig.field_236558_a_);
	}

	@Override
	public boolean generate(ISeedReader world, ChunkGenerator generator, Random random, BlockPos pos, NoFeatureConfig config) {
		System.out.println(pos);
		BlockPos down = pos.down();
		BlockState soil = world.getBlockState(down);
		if(soil.canSustainPlant(world, down, Direction.UP, Registration.tea_sapling) && world.getBlockState(pos).isAir(world, pos)) {
			TeaSaplingBlock.generateTree(world, pos, random);
			return true;
		}
		return false;
	}
}