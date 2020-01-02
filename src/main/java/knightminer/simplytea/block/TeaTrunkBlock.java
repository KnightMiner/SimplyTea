package knightminer.simplytea.block;

import knightminer.simplytea.SimplyTea;
import knightminer.simplytea.core.Config;
import knightminer.simplytea.core.Registration;
import knightminer.simplytea.core.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Locale;
import java.util.Random;

public class TeaTrunkBlock extends Block {
	public static final EnumProperty<TrunkType> TYPE = EnumProperty.create("type", TrunkType.class);
	public static final BooleanProperty CLIPPED = BooleanProperty.create("clipped");

	public static final ResourceLocation LEAVES_DROPS = new ResourceLocation(SimplyTea.MOD_ID, "blocks/tea_leaves");
	public static final VoxelShape BOUNDS_STUMP = Block.makeCuboidShape(6, 0, 6, 10, 16, 10);
	public static final VoxelShape[] BOUNDS_UNCLIPPED = {
			Block.makeCuboidShape(2, 0, 2, 14, 16, 14), // Bottom
			Block.makeCuboidShape(0, 0, 0, 16, 16, 16), // Middle
			Block.makeCuboidShape(2, 0, 2, 14, 12, 14), // Top
			Block.makeCuboidShape(2, 2, 0, 14, 14, 12), // North
			Block.makeCuboidShape(4, 2, 2, 16, 14, 14), // East
			Block.makeCuboidShape(2, 2, 4, 14, 14, 16), // South
			Block.makeCuboidShape(0, 2, 2, 12, 14, 14)  // West
	};
	public static final VoxelShape[] BOUNDS_CLIPPED = {
			Block.makeCuboidShape(6.5, 0, 6.5, 9.5, 16, 9.5), // Bottom
			Block.makeCuboidShape(6.5, 0, 6.5, 9.5, 16, 9.5), // Middle
			Block.makeCuboidShape(6.5, 0, 6.5, 9.5, 8, 9.5), // Top
			Block.makeCuboidShape(6.5, 6.5, 0, 9.5, 9.5, 8), // North
			Block.makeCuboidShape(8, 6.5, 6.5, 16, 9.5, 9.5), // East
			Block.makeCuboidShape(6.5, 6.5, 8, 9.5, 9.5, 16), // South
			Block.makeCuboidShape(0, 6.5, 6.5, 8, 9.5, 9.5) // West
	};

	public TeaTrunkBlock(Properties props) {
		super(props);

		this.setDefaultState(this.stateContainer.getBaseState().with(TYPE, TrunkType.STUMP).with(CLIPPED, false));
	}

	// block tick
	@Deprecated
	@Override
	public void func_225534_a_(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
		super.func_225534_a_(state, world, pos, rand);
		if(state.getBlock() == this && state.get(CLIPPED)) {
			if (rand.nextFloat() < Config.SERVER.tree.regrowthChance()) {
				world.setBlockState(pos, state.with(CLIPPED, false));
				world.notifyBlockUpdate(pos, state, state.with(CLIPPED, false), 8);
			}
		}
	}

	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
		return new ItemStack(Registration.tea_sapling);
	}

	@Deprecated
	public ActionResultType func_225533_a_(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
		if(state.getBlock() != this || state.get(CLIPPED) || state.get(TYPE) == TrunkType.STUMP) {
			return ActionResultType.PASS;
		}

		ItemStack heldItem = player.getHeldItem(hand);
		Item item = heldItem.getItem();
		if(item instanceof ShearsItem || item.getToolTypes(heldItem).contains(SimplyTea.SHEAR_TYPE)) {
			ItemStack stack = heldItem.copy();
			stack.damageItem(1, player, (playerEntity) -> {
				playerEntity.sendBreakAnimation(hand);
			});
			player.setHeldItem(hand, stack);

			if (world instanceof ServerWorld) {
				List<ItemStack> drops = Util.getBlockLoot(state, (ServerWorld)world, pos, player, heldItem, LEAVES_DROPS);
				for (ItemStack drop : drops) {
					spawnAsEntity(world, pos, drop);
				}
			}

			world.setBlockState(pos, state.with(CLIPPED, true), 8);
			world.notifyBlockUpdate(pos, state, state.with(CLIPPED, true), 8);
			world.playSound(player, pos, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1.0F, 1.0F);
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}

	@Deprecated
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
		if(state.getBlock() != this) {
			return BOUNDS_STUMP;
		}
		TrunkType type = state.get(TYPE);
		if (type == TrunkType.STUMP) {
			return BOUNDS_STUMP;
		}

		return (state.get(CLIPPED) ? BOUNDS_CLIPPED : BOUNDS_UNCLIPPED)[type.getIndex() - 1];
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(TYPE, CLIPPED);
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {}

	public static enum TrunkType implements IStringSerializable {
		STUMP,
		BOTTOM,
		MIDDLE,
		TOP,
		NORTH,
		EAST,
		SOUTH,
		WEST;

		private int index;
		TrunkType() {
			this.index = ordinal();
		}

		public int getIndex() {
			return index;
		}

		@Override
		public String getName() {
			return this.name().toLowerCase(Locale.US);
		}

		public static TrunkType fromIndex(int index) {
			if(index < 0 || index >= values().length) {
				index = 0;
			}
			return values()[index];
		}
	}
}
