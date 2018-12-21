package elucent.simplytea.block;

import java.util.Random;

import elucent.simplytea.IModeledObject;
import elucent.simplytea.SimplyTea;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockTeaTrunk extends Block implements IModeledObject, IItemBlock {
	public static final PropertyInteger TYPE = PropertyInteger.create("type", 0, 7);
	public static final PropertyBool CLIPPED = PropertyBool.create("clipped");

	public static final AxisAlignedBB AABB_BOTTOM = new AxisAlignedBB(0.375, 0, 0.375, 0.625, 1, 0.625);
	public static final AxisAlignedBB AABB_BOTTOM_2 = new AxisAlignedBB(0.125, 0, 0.125, 0.875, 1, 0.875);
	public static final AxisAlignedBB AABB_MID = new AxisAlignedBB(0, 0, 0, 1, 1, 1);
	public static final AxisAlignedBB AABB_TOP = new AxisAlignedBB(0.125, 0, 0.125, 0.875, 0.75, 0.875);
	public static final AxisAlignedBB AABB_NORTH = new AxisAlignedBB(0.125, 0.125, 0, 0.875, 0.875, 0.75);
	public static final AxisAlignedBB AABB_WEST = new AxisAlignedBB(0, 0.125, 0.125, 0.75, 0.875, 0.875);
	public static final AxisAlignedBB AABB_SOUTH = new AxisAlignedBB(0.125, 0.125, 0.25, 0.875, 0.875, 1.0);
	public static final AxisAlignedBB AABB_EAST = new AxisAlignedBB(0.25, 0.125, 0.125, 1.0, 0.875, 0.875);

	public Item itemBlock = null;

	public BlockTeaTrunk(String name, boolean addToTab) {
		super(Material.WOOD);
		this.setSoundType(SoundType.WOOD);
		setHarvestLevel("axe", -1);
		setHardness(1.8f);
		setUnlocalizedName(name);
		setRegistryName(new ResourceLocation(SimplyTea.MODID, name));
		if(addToTab) {
			setCreativeTab(SimplyTea.tab);
		}
		this.setTickRandomly(true);
		itemBlock = (new ItemBlock(this).setRegistryName(this.getRegistryName()));
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
		if(state.getBlock() == this) {
			if(state.getValue(CLIPPED)) {
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
		if(state.getBlock() != this || state.getValue(CLIPPED) || state.getValue(TYPE) == 0) {
			return false;
		}

		ItemStack heldItem = player.getHeldItem(hand);
		Item item = heldItem.getItem();
		if(item instanceof ItemShears || item.getToolClasses(heldItem).contains("shears")) {
			ItemStack stack = heldItem.copy();
			stack.damageItem(1, player);
			player.setHeldItem(hand, stack);
			this.dropBlockAsItem(world, pos, state, 0);
			world.setBlockState(pos, state.withProperty(CLIPPED, true), 8);
			world.notifyBlockUpdate(pos, state, state.withProperty(CLIPPED, true), 8);
			return true;
		}
		return false;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		if(state.getBlock() == this) {
			if(state.getValue(TYPE) == 0) {
				return AABB_BOTTOM;
			}
			if(state.getValue(TYPE) == 1) {
				return AABB_BOTTOM_2;
			}
			if(state.getValue(TYPE) == 2) {
				return AABB_MID;
			}
			if(state.getValue(TYPE) == 3) {
				return AABB_TOP;
			}
			if(state.getValue(TYPE) == 4) {
				return AABB_NORTH;
			}
			if(state.getValue(TYPE) == 5) {
				return AABB_EAST;
			}
			if(state.getValue(TYPE) == 6) {
				return AABB_SOUTH;
			}
			if(state.getValue(TYPE) == 7) {
				return AABB_WEST;
			}
		}
		return AABB_MID;
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, TYPE, CLIPPED);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(TYPE) + (state.getValue(CLIPPED) ? 8 : 0);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(TYPE, meta % 8).withProperty(CLIPPED, meta >= 8);
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

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {

	}

	@Override
	public NonNullList<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		NonNullList<ItemStack> drops = NonNullList.create();
		if(state.getBlock() == this) {
			if(state.getValue(TYPE) > 0 && !state.getValue(CLIPPED)) {
				drops.add(new ItemStack(SimplyTea.leaf_tea, RANDOM.nextInt(3) + 1));
				if(RANDOM.nextInt(10) == 0) {
					drops.add(new ItemStack(SimplyTea.tea_sapling, 1));
				}
			}
			drops.add(new ItemStack(Items.STICK, RANDOM.nextInt(3) + 1));
		}
		return drops;
	}

	@Override
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
				new ModelResourceLocation(getRegistryName().toString(), "inventory"));
	}

	@Override
	public Item getItemBlock() {
		return itemBlock;
	}
}
