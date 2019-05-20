package elucent.simplytea.block;

import elucent.simplytea.SimplyTea;
import elucent.simplytea.block.BlockTeaTrunk.TrunkType;
import elucent.simplytea.core.IModeledObject;
import elucent.simplytea.core.Util;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockTeaSapling extends BlockBush implements IGrowable, IModeledObject, IItemBlock {
	private Item itemBlock = null;
	public static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 1);

	public BlockTeaSapling(String name) {
		super();
		itemBlock = Util.init(this, name, true);

		this.setSoundType(SoundType.PLANT);
		this.setTickRandomly(true);
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, STAGE);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(STAGE);
	}

	@Override
	@Deprecated
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(STAGE, meta);
	}

	@Override
	public boolean requiresUpdates() {
		return true;
	}

	@Override
	public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if(!worldIn.isRemote) {
			super.updateTick(worldIn, pos, state, rand);

			if(worldIn.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(7) == 0) {
				this.grow(worldIn, pos, state, rand);
			}
		}
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if(!worldIn.isRemote) {
			super.updateTick(worldIn, pos, state, rand);

			if(worldIn.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(7) == 0) {
				this.grow(worldIn, pos, state, rand);
			}
		}
	}

	public void grow(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if(state.getValue(STAGE).intValue() == 0) {
			worldIn.setBlockState(pos, state.cycleProperty(STAGE), 4);
		}
		else {
			BlockTeaSapling.generateTree(worldIn, pos, state, rand);
		}
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	@Deprecated
	public boolean isFullBlock(IBlockState state) {
		return false;
	}

	public static void generateTree(World world, BlockPos pos, IBlockState state, Random random) {
		IBlockState trunk = SimplyTea.tea_trunk.getDefaultState().withProperty(BlockTeaTrunk.CLIPPED, false);
		// tree stump
		world.setBlockState(pos, trunk.withProperty(BlockTeaTrunk.TYPE, TrunkType.STUMP));
		world.setBlockState(pos.up(), trunk.withProperty(BlockTeaTrunk.TYPE, TrunkType.BOTTOM));

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

			if (north) setBlockSafe(world, branch.north(), trunk.withProperty(BlockTeaTrunk.TYPE, TrunkType.SOUTH));
			if (east)  setBlockSafe(world, branch.east(),  trunk.withProperty(BlockTeaTrunk.TYPE, TrunkType.WEST));
			if (south) setBlockSafe(world, branch.south(), trunk.withProperty(BlockTeaTrunk.TYPE, TrunkType.NORTH));
			if (west)  setBlockSafe(world, branch.west(),  trunk.withProperty(BlockTeaTrunk.TYPE, TrunkType.EAST));

			world.setBlockState(branch, trunk.withProperty(BlockTeaTrunk.TYPE, TrunkType.MIDDLE));
		}

		// tree top
		world.setBlockState(pos.up(height), trunk.withProperty(BlockTeaTrunk.TYPE, TrunkType.TOP));
	}

	/**
	 * Sets a block only if the block is replacable
	 */
	private static void setBlockSafe(World world, BlockPos pos, IBlockState state) {
		if(world.getBlockState(pos).getBlock().isReplaceable(world, pos)) {
			world.setBlockState(pos, state);
		}
	}

	@Override
	public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean isClient) {
		// tree minimum is 4 blocks tall (sapling position plus 3)
		for(int i = 1; i <= 3; i++) {
			if(!world.isAirBlock(pos.up(i))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		this.grow(worldIn, pos, state, rand);
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		return true;
	}

	@Override
	public Item getItemBlock() {
		return itemBlock;
	}
}