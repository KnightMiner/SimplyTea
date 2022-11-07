package knightminer.simplytea.worldgen;

import knightminer.simplytea.block.TeaTrunkBlock;
import knightminer.simplytea.block.TeaTrunkBlock.TrunkType;
import knightminer.simplytea.core.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
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
		RandomSource random = context.random();
		if (world.getBlockState(pos).isAir()) {
			// TODO: move to tree?
			BlockState trunk = Registration.tea_trunk.defaultBlockState().setValue(TeaTrunkBlock.CLIPPED, false);
			// tree stump
			world.setBlock(pos, trunk.setValue(TeaTrunkBlock.TYPE, TrunkType.STUMP), 3);
			world.setBlock(pos.above(), trunk.setValue(TeaTrunkBlock.TYPE, TrunkType.BOTTOM), 3);

			// tree branches
			// tree minimum is 4 blocks tall, but can be up to two blocks taller if space permits
			int height = 3;
			if(world.isEmptyBlock(pos.above(4))) {
				height += random.nextInt(world.isEmptyBlock(pos.above(5)) ? 3 : 2);
			}
			boolean north, south, west, east;
			BlockPos branch;
			for(int i = 2; i < height; i++) {
				north = random.nextBoolean();
				south = random.nextBoolean();
				west = random.nextBoolean();
				east = random.nextBoolean();
				branch = pos.above(i);

				if (north) setBlockSafe(world, branch.north(), trunk.setValue(TeaTrunkBlock.TYPE, TrunkType.SOUTH));
				if (east)  setBlockSafe(world, branch.east(),  trunk.setValue(TeaTrunkBlock.TYPE, TrunkType.WEST));
				if (south) setBlockSafe(world, branch.south(), trunk.setValue(TeaTrunkBlock.TYPE, TrunkType.NORTH));
				if (west)  setBlockSafe(world, branch.west(),  trunk.setValue(TeaTrunkBlock.TYPE, TrunkType.EAST));

				world.setBlock(branch, trunk.setValue(TeaTrunkBlock.TYPE, TrunkType.MIDDLE), 3);
			}

			// tree top
			world.setBlock(pos.above(height), trunk.setValue(TeaTrunkBlock.TYPE, TrunkType.TOP), 3);
			
			return true;
		}
		return false;
	}
	
	/**
	 * Sets a block only if the block is replaceable
	 */
	private static void setBlockSafe(LevelAccessor world, BlockPos pos, BlockState state) {
		BlockState old = world.getBlockState(pos);
		if(old.isAir() || old.getMaterial().isReplaceable() || old.is(BlockTags.LEAVES)) {
			world.setBlock(pos, state, 3);
		}
	}
}