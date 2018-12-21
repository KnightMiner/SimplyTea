package elucent.simplytea.block;

import java.util.Random;

import elucent.simplytea.IModeledObject;
import elucent.simplytea.SimplyTea;
import elucent.simplytea.block.BlockTeaTrunk.TrunkType;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockTeaSapling extends BlockBush implements IGrowable, IModeledObject, IItemBlock {
	public Item itemBlock = null;
	public static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 1);

	public BlockTeaSapling(String name, boolean addToTab) {
		super();
		this.setSoundType(SoundType.PLANT);
		setUnlocalizedName(name);
		setRegistryName(new ResourceLocation(SimplyTea.MODID, name));
		if(addToTab) {
			setCreativeTab(SimplyTea.tab);
		}
		this.setTickRandomly(true);
		itemBlock = (new ItemBlock(this).setRegistryName(this.getRegistryName()));
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
	public boolean isFullBlock(IBlockState state) {
		return false;
	}

	public static void generateTree(World world, BlockPos pos, IBlockState state, Random random) {
		IBlockState trunk = SimplyTea.tea_trunk.getDefaultState().withProperty(BlockTeaTrunk.CLIPPED, false);
		// tree stump
		world.setBlockState(pos, trunk.withProperty(BlockTeaTrunk.TYPE, TrunkType.STUMP));
		world.setBlockState(pos.up(), trunk.withProperty(BlockTeaTrunk.TYPE, TrunkType.BOTTOM));

		// tree branches
		int height = random.nextInt(3) + 3;
		boolean north, south, west, east;
		BlockPos branch;
		for(int i = 2; i < height; i++) {
			north = random.nextBoolean();
			south = random.nextBoolean();
			west = random.nextBoolean();
			east = random.nextBoolean();
			branch = pos.up(i);
			if(north) {
				world.setBlockState(branch.north(), trunk.withProperty(BlockTeaTrunk.TYPE, TrunkType.SOUTH));
			}
			if(east) {
				world.setBlockState(branch.east(), trunk.withProperty(BlockTeaTrunk.TYPE, TrunkType.WEST));
			}
			if(south) {
				world.setBlockState(branch.south(), trunk.withProperty(BlockTeaTrunk.TYPE, TrunkType.NORTH));
			}
			if(west) {
				world.setBlockState(branch.west(), trunk.withProperty(BlockTeaTrunk.TYPE, TrunkType.EAST));
			}
			world.setBlockState(branch, trunk.withProperty(BlockTeaTrunk.TYPE, TrunkType.MIDDLE));
		}

		// tree top
		world.setBlockState(pos.up(height), trunk.withProperty(BlockTeaTrunk.TYPE, TrunkType.TOP));
	}

	@Override
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
				new ModelResourceLocation(getRegistryName().toString(), "inventory"));
	}

	@Override
	public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean isClient) {
		return world.isAirBlock(pos.up(1)) && world.isAirBlock(pos.up(2)) && world.isAirBlock(pos.up(3))
				&& world.isAirBlock(pos.up(4)) && world.isAirBlock(pos.up(5)) && world.isAirBlock(pos.up(6));
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