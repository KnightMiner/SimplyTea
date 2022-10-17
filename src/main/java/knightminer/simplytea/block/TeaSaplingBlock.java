package knightminer.simplytea.block;

import knightminer.simplytea.block.TeaTrunkBlock.TrunkType;
import knightminer.simplytea.core.Registration;
import net.minecraft.block.BlockState;
import net.minecraft.block.SaplingBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.Random;

import net.minecraft.block.AbstractBlock.Properties;

public class TeaSaplingBlock extends SaplingBlock {

	public TeaSaplingBlock(Properties props) {
		// TODO: add a tree for some logic?
		super(null, props);
	}

	@Override
	public void advanceTree(ServerWorld world, BlockPos pos, BlockState state, Random rand) {
		if (state.getValue(STAGE) == 0) {
			world.setBlock(pos, state.cycle(STAGE), 4);
		} else if (ForgeEventFactory.saplingGrowTree(world, rand, pos)) {
			TeaSaplingBlock.generateTree(world, pos, rand);
		}
	}

	@Override
	public boolean isValidBonemealTarget(IBlockReader world, BlockPos pos, BlockState state, boolean b) {
		// tree minimum is 4 blocks tall (sapling position plus 3)
		for(int i = 1; i <= 3; i++) {
			// TODO: use state.isReplaceable?
			BlockState up = world.getBlockState(pos.above(i));
			if(!up.isAir(world, pos) && !up.getMaterial().isReplaceable()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Creates a new tea tree
	 */
	public static void generateTree(IWorld world, BlockPos pos, Random random) {
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
	}

	/**
	 * Sets a block only if the block is replaceable
	 */
	private static void setBlockSafe(IWorld world, BlockPos pos, BlockState state) {
		BlockState old = world.getBlockState(pos);
		if(old.isAir(world, pos) || old.getMaterial().isReplaceable() || old.getBlock().is(BlockTags.LEAVES)) {
			world.setBlock(pos, state, 3);
		}
	}
}