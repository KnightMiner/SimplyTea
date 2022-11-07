package knightminer.simplytea.block;

import knightminer.simplytea.SimplyTea;
import knightminer.simplytea.core.Config;
import knightminer.simplytea.core.Registration;
import knightminer.simplytea.core.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ToolActions;

import java.util.List;
import java.util.Locale;

public class TeaTrunkBlock extends Block {
	public static final EnumProperty<TrunkType> TYPE = EnumProperty.create("type", TrunkType.class);
	public static final BooleanProperty CLIPPED = BooleanProperty.create("clipped");

	public static final ResourceLocation LEAVES_DROPS = new ResourceLocation(SimplyTea.MOD_ID, "blocks/tea_leaves");
	public static final VoxelShape BOUNDS_STUMP = Block.box(6, 0, 6, 10, 16, 10);
	public static final VoxelShape[] BOUNDS_UNCLIPPED = {
			Block.box(2, 0, 2, 14, 16, 14), // Bottom
			Block.box(0, 0, 0, 16, 16, 16), // Middle
			Block.box(2, 0, 2, 14, 12, 14), // Top
			Block.box(2, 2, 0, 14, 14, 12), // North
			Block.box(4, 2, 2, 16, 14, 14), // East
			Block.box(2, 2, 4, 14, 14, 16), // South
			Block.box(0, 2, 2, 12, 14, 14)  // West
	};
	public static final VoxelShape[] BOUNDS_CLIPPED = {
			Block.box(6.5, 0, 6.5, 9.5, 16, 9.5), // Bottom
			Block.box(6.5, 0, 6.5, 9.5, 16, 9.5), // Middle
			Block.box(6.5, 0, 6.5, 9.5, 8, 9.5), // Top
			Block.box(6.5, 6.5, 0, 9.5, 9.5, 8), // North
			Block.box(8, 6.5, 6.5, 16, 9.5, 9.5), // East
			Block.box(6.5, 6.5, 8, 9.5, 9.5, 16), // South
			Block.box(0, 6.5, 6.5, 8, 9.5, 9.5) // West
	};

	public TeaTrunkBlock(Properties props) {
		super(props);

		this.registerDefaultState(this.stateDefinition.any().setValue(TYPE, TrunkType.STUMP).setValue(CLIPPED, false));
	}

	@Deprecated
	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		super.tick(state, world, pos, random);
		if(state.getBlock() == this && state.getValue(CLIPPED)) {
			if (random.nextFloat() < Config.SERVER.tree.regrowthChance()) {
				world.setBlockAndUpdate(pos, state.setValue(CLIPPED, false));
				world.sendBlockUpdated(pos, state, state.setValue(CLIPPED, false), 8);
			}
		}
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter pLevel, BlockPos pPos, BlockState pState) {
		return new ItemStack(Registration.tea_sapling);
	}

	@Override
	@Deprecated
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult trace) {
		if(state.getBlock() != this || state.getValue(CLIPPED) || state.getValue(TYPE) == TrunkType.STUMP) {
			return InteractionResult.PASS;
		}

		ItemStack heldItem = player.getItemInHand(hand);
		Item item = heldItem.getItem();
		if(item instanceof ShearsItem || heldItem.canPerformAction(ToolActions.SHEARS_HARVEST)) {
			ItemStack stack = heldItem.copy();
			stack.hurtAndBreak(1, player, (playerEntity) -> {
				playerEntity.broadcastBreakEvent(hand);
			});
			player.setItemInHand(hand, stack);

			if (world instanceof ServerLevel) {
				List<ItemStack> drops = Util.getBlockLoot(state, (ServerLevel)world, pos, player, heldItem, LEAVES_DROPS);
				for (ItemStack drop : drops) {
					popResource(world, pos, drop);
				}
			}

			world.setBlock(pos, state.setValue(CLIPPED, true), 8);
			world.sendBlockUpdated(pos, state, state.setValue(CLIPPED, true), 8);
			world.playSound(player, pos, SoundEvents.SHEEP_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Deprecated
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		if(state.getBlock() != this) {
			return BOUNDS_STUMP;
		}
		TrunkType type = state.getValue(TYPE);
		if (type == TrunkType.STUMP) {
			return BOUNDS_STUMP;
		}

		return (state.getValue(CLIPPED) ? BOUNDS_CLIPPED : BOUNDS_UNCLIPPED)[type.getIndex() - 1];
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(TYPE, CLIPPED);
	}

	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {}

	public static enum TrunkType implements StringRepresentable {
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
		public String getSerializedName() {
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
