package elucent.simplytea.block;

import java.util.Random;

import elucent.simplytea.IModeledObject;
import elucent.simplytea.SimplyTea;
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
		int height = random.nextInt(3) + 4;
		world.setBlockState(pos, SimplyTea.tea_trunk.getDefaultState().withProperty(BlockTeaTrunk.TYPE, 0)
				.withProperty(BlockTeaTrunk.CLIPPED, false));
		for(int i = 1; i < height; i++) {
			if(i == 1) {
				world.setBlockState(pos.up(i), SimplyTea.tea_trunk.getDefaultState().withProperty(BlockTeaTrunk.TYPE, 1)
						.withProperty(BlockTeaTrunk.CLIPPED, false));
			}
			else if(i < height - 1) {
				boolean north = random.nextBoolean();
				boolean south = random.nextBoolean();
				boolean west = random.nextBoolean();
				boolean east = random.nextBoolean();
				if(north) {
					world.setBlockState(pos.up(i).north(), SimplyTea.tea_trunk.getDefaultState()
							.withProperty(BlockTeaTrunk.TYPE, 6).withProperty(BlockTeaTrunk.CLIPPED, false));
				}
				if(east) {
					world.setBlockState(pos.up(i).east(), SimplyTea.tea_trunk.getDefaultState()
							.withProperty(BlockTeaTrunk.TYPE, 7).withProperty(BlockTeaTrunk.CLIPPED, false));
				}
				if(south) {
					world.setBlockState(pos.up(i).south(), SimplyTea.tea_trunk.getDefaultState()
							.withProperty(BlockTeaTrunk.TYPE, 4).withProperty(BlockTeaTrunk.CLIPPED, false));
				}
				if(west) {
					world.setBlockState(pos.up(i).west(), SimplyTea.tea_trunk.getDefaultState()
							.withProperty(BlockTeaTrunk.TYPE, 5).withProperty(BlockTeaTrunk.CLIPPED, false));
				}
				world.setBlockState(pos.up(i), SimplyTea.tea_trunk.getDefaultState().withProperty(BlockTeaTrunk.TYPE, 2)
						.withProperty(BlockTeaTrunk.CLIPPED, false));
			}
			else {
				world.setBlockState(pos.up(i), SimplyTea.tea_trunk.getDefaultState().withProperty(BlockTeaTrunk.TYPE, 3)
						.withProperty(BlockTeaTrunk.CLIPPED, false));
			}
		}
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