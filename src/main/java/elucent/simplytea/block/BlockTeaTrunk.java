package elucent.simplytea.block;

import elucent.simplytea.SimplyTea;
import elucent.simplytea.core.Config;
import elucent.simplytea.core.IModeledObject;
import elucent.simplytea.core.Util;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Locale;
import java.util.Random;

public class BlockTeaTrunk extends Block implements IModeledObject, IItemBlock {
	public static final PropertyEnum<TrunkType> TYPE = PropertyEnum.create("type", TrunkType.class);
	public static final PropertyBool CLIPPED = PropertyBool.create("clipped");

	public static final AxisAlignedBB BOUNDS_STUMP = new AxisAlignedBB(0.375, 0, 0.375, 0.625, 1, 0.625);
	public static final AxisAlignedBB[] BOUNDS_UNCLIPPED = {
			new AxisAlignedBB(0.125, 0,     0.125, 0.875, 1,     0.875), // BOTTOM
			new AxisAlignedBB(0,     0,     0,     1,     1,     1    ), // MIDDLE
			new AxisAlignedBB(0.125, 0,     0.125, 0.875, 0.75,  0.875), // TOP
			new AxisAlignedBB(0.125, 0.125, 0,     0.875, 0.875, 0.75 ), // NORTH
			new AxisAlignedBB(0.25,  0.125, 0.125, 1.0,   0.875, 0.875), // EAST
			new AxisAlignedBB(0.125, 0.125, 0.25,  0.875, 0.875, 1.0  ), // SOUTH
			new AxisAlignedBB(0,     0.125, 0.125, 0.75,  0.875, 0.875) // WEST
	};
	public static final AxisAlignedBB[] BOUNDS_CLIPPED = {
			new AxisAlignedBB(0.40625, 0,       0.40625, 0.59375, 1,       0.59375), // BOTTOM
			new AxisAlignedBB(0.40625, 0,       0.40625, 0.59375, 1,       0.59375), // MIDDLE
			new AxisAlignedBB(0.40625, 0,       0.40625, 0.59375, 0.5,     0.59375), // TOP
			new AxisAlignedBB(0.40625, 0.40625, 0,       0.59375, 0.59375, 0.5    ), // NORTH
			new AxisAlignedBB(0.5,     0.40625, 0.40625, 1.0,     0.59375, 0.59375), // EAST
			new AxisAlignedBB(0.40625, 0.40625, 0.5,     0.59375, 0.59375, 1.0    ), // SOUTH
			new AxisAlignedBB(0,       0.40625, 0.40625, 0.5,     0.59375, 0.59375) // WEST
	};

	private Item itemBlock = null;

	public BlockTeaTrunk(String name) {
		super(Material.WOOD);
		itemBlock = Util.init(this, name, false);

		this.setSoundType(SoundType.WOOD);
		this.setHarvestLevel("axe", -1);
		this.setHardness(1.8f);
		this.setTickRandomly(true);
		this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, TrunkType.STUMP).withProperty(CLIPPED, false));
	}

	@Override
	public boolean getTickRandomly() {
		return true;
	}

	@Override
	public boolean requiresUpdates() {
		return true;
	}

	@Override
	public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if(state.getBlock() == this && state.getValue(CLIPPED)) {
			if (rand.nextFloat() < Config.tree.leaf_growth_chance) {
				worldIn.setBlockState(pos, state.withProperty(CLIPPED, false));
				worldIn.notifyBlockUpdate(pos, state, state.withProperty(CLIPPED, false), 8);
			}
		}
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos,
			EntityPlayer player) {
		return new ItemStack(SimplyTea.tea_sapling, 1);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing face, float hitX, float hitY, float hitZ) {
		if(state.getBlock() != this || state.getValue(CLIPPED) || state.getValue(TYPE) == TrunkType.STUMP) {
			return false;
		}

		ItemStack heldItem = player.getHeldItem(hand);
		Item item = heldItem.getItem();
		if(item instanceof ItemShears || item.getToolClasses(heldItem).contains("shears")) {
			ItemStack stack = heldItem.copy();
			stack.damageItem(1, player);
			player.setHeldItem(hand, stack);

			NonNullList<ItemStack> drops = addTeaDrops(NonNullList.create(), state);
            for (ItemStack drop : drops) {
                spawnAsEntity(world, pos, drop);
            }

			world.setBlockState(pos, state.withProperty(CLIPPED, true), 8);
			world.notifyBlockUpdate(pos, state, state.withProperty(CLIPPED, true), 8);
			world.playSound(player, pos, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1.0F, 1.0F);
			return true;
		}
		return false;
	}

	@Override
	@Deprecated
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		if(state.getBlock() != this) {
			return BOUNDS_STUMP;
		}
		TrunkType type = state.getValue(TYPE);
		if (type == TrunkType.STUMP) {
			return BOUNDS_STUMP;
		}

		return (state.getValue(CLIPPED) ? BOUNDS_CLIPPED : BOUNDS_UNCLIPPED)[type.getMeta() - 1];
	}

	@Override
	@Deprecated
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing side) {
		TrunkType type = state.getValue(TYPE);
		if (side == EnumFacing.DOWN && type == TrunkType.TOP) {
			return BlockFaceShape.CENTER;
		}
		if (side.getAxis().isVertical() && (type == TrunkType.BOTTOM || type == TrunkType.MIDDLE || type == TrunkType.STUMP)) {
			return BlockFaceShape.CENTER;
		}

		return BlockFaceShape.UNDEFINED;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, TYPE, CLIPPED);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(TYPE).getMeta() + (state.getValue(CLIPPED) ? 8 : 0);
	}

	@Override
	@Deprecated
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(TYPE, TrunkType.fromMeta(meta % 8)).withProperty(CLIPPED, meta >= 8);
	}

	@Override
	@Deprecated
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	@Deprecated
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	@Deprecated
	public boolean isFullBlock(IBlockState state) {
		return false;
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {

	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		addTeaDrops(drops, state);
		drops.add(new ItemStack(SimplyTea.tea_stick, randomCount(Config.tree.max_sticks)));
	}

	private NonNullList<ItemStack> addTeaDrops(NonNullList<ItemStack> drops, IBlockState state) {
		if(state.getValue(TYPE) != TrunkType.STUMP && !state.getValue(CLIPPED)) {
			drops.add(new ItemStack(SimplyTea.leaf_tea, randomCount(Config.tree.max_leaves)));
			if(RANDOM.nextFloat() < Config.tree.sapling_chance) {
				drops.add(new ItemStack(SimplyTea.tea_sapling, 1));
			}
		}
		return drops;
	}

	private static int randomCount(int max) {
		if(max == 1) {
			return 1;
		}
		return 1 + RANDOM.nextInt(max);
	}

	@Override
	public Item getItemBlock() {
		return itemBlock;
	}

	public static enum TrunkType implements IStringSerializable {
		STUMP,
		BOTTOM,
		MIDDLE,
		TOP,
		NORTH,
		EAST,
		SOUTH,
		WEST;

		private int meta;
		TrunkType() {
			this.meta = ordinal();
		}

		public int getMeta() {
			return meta;
		}

		@Override
		public String getName() {
			return this.name().toLowerCase(Locale.US);
		}

		public static TrunkType fromMeta(int meta) {
			if(meta < 0 || meta >= values().length) {
				meta = 0;
			}
			return values()[meta];
		}
	}
}
