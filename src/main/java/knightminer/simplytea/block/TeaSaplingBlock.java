package knightminer.simplytea.block;

import knightminer.simplytea.block.TeaTrunkBlock.TrunkType;
import knightminer.simplytea.core.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.ForgeEventFactory;

public class TeaSaplingBlock extends SaplingBlock {

	public TeaSaplingBlock(Properties props) {
		// TODO: add a tree for some logic?
		super(null, props);
	}

	@SuppressWarnings("removal")
	@Override
	public void advanceTree(ServerLevel pLevel, BlockPos pPos, BlockState pState, RandomSource pRandom) {
		if (pState.getValue(STAGE) == 0) {
			pLevel.setBlock(pPos, pState.cycle(STAGE), 4);
		} else if (ForgeEventFactory.saplingGrowTree(pLevel, pRandom, pPos)) {
			TeaSaplingBlock.generateTree(pLevel, pPos, pRandom);
		}
	}

	@Override
	public boolean isValidBonemealTarget(BlockGetter world, BlockPos pos, BlockState state, boolean b) {
		// tree minimum is 4 blocks tall (sapling position plus 3)
		for(int i = 1; i <= 3; i++) {
			// TODO: use state.isReplaceable?
			BlockState up = world.getBlockState(pos.above(i));
			if(!up.isAir() && !up.getMaterial().isReplaceable()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Creates a new tea tree
	 */
	public static void generateTree(WorldGenLevel world, BlockPos pos, RandomSource random) {		
		// TODO: move to tree?
		BlockState trunk = Registration.tea_trunk.get().defaultBlockState().setValue(TeaTrunkBlock.CLIPPED, false);
		
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