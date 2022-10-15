package knightminer.simplytea.worldgen;

import java.util.Random;

import knightminer.simplytea.block.TeaTrunkBlock;
import knightminer.simplytea.block.TeaTrunkBlock.TrunkType;
import knightminer.simplytea.core.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
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
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> pContext) {
		BlockPos down = pContext.origin().below();
		BlockState soil = pContext.level().getBlockState(down);
		if(soil.canSustainPlant(pContext.level(), down, Direction.UP, Registration.tea_sapling.get()) && pContext.level().getBlockState(pContext.origin()).isAir()) {
			
			WorldGenLevel level = pContext.level();
			BlockPos pos = pContext.origin();
			Random random = pContext.random();
			
			// TODO: move to tree?
			BlockState trunk = Registration.tea_trunk.get().defaultBlockState().setValue(TeaTrunkBlock.CLIPPED, false);
			
			// tree stump
			level.setBlock(pos, trunk.setValue(TeaTrunkBlock.TYPE, TrunkType.STUMP), 3);
			level.setBlock(pos.above(), trunk.setValue(TeaTrunkBlock.TYPE, TrunkType.BOTTOM), 3);

			// tree branches
			// tree minimum is 4 blocks tall, but can be up to two blocks taller if space permits
			int height = 3;
			if(level.isEmptyBlock(pos.above(4))) {
				height += random.nextInt(level.isEmptyBlock(pos.above(5)) ? 3 : 2);
			}
			boolean north, south, west, east;
			BlockPos branch;
			for(int i = 2; i < height; i++) {
				north = random.nextBoolean();
				south = random.nextBoolean();
				west = random.nextBoolean();
				east = random.nextBoolean();
				branch = pos.above(i);

				if (north) setBlockSafe(level, branch.north(), trunk.setValue(TeaTrunkBlock.TYPE, TrunkType.SOUTH));
				if (east)  setBlockSafe(level, branch.east(),  trunk.setValue(TeaTrunkBlock.TYPE, TrunkType.WEST));
				if (south) setBlockSafe(level, branch.south(), trunk.setValue(TeaTrunkBlock.TYPE, TrunkType.NORTH));
				if (west)  setBlockSafe(level, branch.west(),  trunk.setValue(TeaTrunkBlock.TYPE, TrunkType.EAST));

				level.setBlock(branch, trunk.setValue(TeaTrunkBlock.TYPE, TrunkType.MIDDLE), 3);
			}

			// tree top
			level.setBlock(pos.above(height), trunk.setValue(TeaTrunkBlock.TYPE, TrunkType.TOP), 3);
			
			return true;
		}
		return false;
	}
	
	/**
	 * Sets a block only if the block is replaceable
	 */
	private static void setBlockSafe(WorldGenLevel world, BlockPos pos, BlockState state) {
		BlockState old = world.getBlockState(pos);
		if(old.isAir() || old.getMaterial().isReplaceable() || old.is(BlockTags.LEAVES)) {
			world.setBlock(pos, state, 3);
		}
	}
}