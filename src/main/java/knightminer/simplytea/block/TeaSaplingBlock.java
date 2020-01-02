package knightminer.simplytea.block;

import knightminer.simplytea.block.TeaTrunkBlock.TrunkType;
import knightminer.simplytea.core.Registration;
import net.minecraft.block.BlockState;
import net.minecraft.block.SaplingBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.Random;

public class TeaSaplingBlock extends SaplingBlock {

	public TeaSaplingBlock(Properties props) {
		// TODO: add a tree for some logic?
		super(null, props);
	}

	// IGrowable's grow
	@Override
	public void func_225535_a_(ServerWorld world, Random rand, BlockPos pos, BlockState state) {
		if(state.get(STAGE) == 0) {
			world.setBlockState(pos, state.cycle(STAGE), 4);
		}
		else if(ForgeEventFactory.saplingGrowTree(world, rand, pos)) {
			TeaSaplingBlock.generateTree(world, pos, rand);
		}
	}

	@Override
	public boolean canGrow(IBlockReader world, BlockPos pos, BlockState state, boolean b) {
		// tree minimum is 4 blocks tall (sapling position plus 3)
		for(int i = 1; i <= 3; i++) {
			// TODO: use state.isReplaceable?
			BlockState up = world.getBlockState(pos.up(i));
			if(!up.isAir(world, pos) && !up.getMaterial().isReplaceable()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean canUseBonemeal(World world, Random random, BlockPos blockPos, BlockState blockState) {
		return (double)world.rand.nextFloat() < 0.45D;
	}

	public static void generateTree(IWorld world, BlockPos pos, Random random) {
		// TODO: move to tree?
		BlockState trunk = Registration.tea_trunk.getDefaultState().with(TeaTrunkBlock.CLIPPED, false);
		// tree stump
		world.setBlockState(pos, trunk.with(TeaTrunkBlock.TYPE, TrunkType.STUMP), 3);
		world.setBlockState(pos.up(), trunk.with(TeaTrunkBlock.TYPE, TrunkType.BOTTOM), 3);

		// tree branches
		// tree minimum is 4 blocks tall, but can be up to two blocks taller if space permits
		int height = 3;
		if(world.isAirBlock(pos.up(4))) {
			height += random.nextInt(world.isAirBlock(pos.up(5)) ? 3 : 2);
		}
		boolean north, south, west, east;
		BlockPos branch;
		for(int i = 2; i < height; i++) {
			north = random.nextBoolean();
			south = random.nextBoolean();
			west = random.nextBoolean();
			east = random.nextBoolean();
			branch = pos.up(i);

			if (north) setBlockSafe(world, branch.north(), trunk.with(TeaTrunkBlock.TYPE, TrunkType.SOUTH));
			if (east)  setBlockSafe(world, branch.east(),  trunk.with(TeaTrunkBlock.TYPE, TrunkType.WEST));
			if (south) setBlockSafe(world, branch.south(), trunk.with(TeaTrunkBlock.TYPE, TrunkType.NORTH));
			if (west)  setBlockSafe(world, branch.west(),  trunk.with(TeaTrunkBlock.TYPE, TrunkType.EAST));

			world.setBlockState(branch, trunk.with(TeaTrunkBlock.TYPE, TrunkType.MIDDLE), 3);
		}

		// tree top
		world.setBlockState(pos.up(height), trunk.with(TeaTrunkBlock.TYPE, TrunkType.TOP), 3);
	}

	/**
	 * Sets a block only if the block is replaceable
	 */
	private static void setBlockSafe(IWorld world, BlockPos pos, BlockState state) {
		BlockState old = world.getBlockState(pos);
		if(old.isAir(world, pos) || old.getMaterial().isReplaceable() || old.getBlock().isIn(BlockTags.LEAVES)) {
			world.setBlockState(pos, state, 3);
		}
	}
}