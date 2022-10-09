package knightminer.simplytea.item;

import knightminer.simplytea.core.Config;
import knightminer.simplytea.core.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;

public class TeapotItem extends TooltipItem {
	public TeapotItem(Properties props) {
		super(props);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		BlockHitResult rayTrace = getPlayerPOVHitResult(world, player, ClipContext.Fluid.SOURCE_ONLY);
		if (rayTrace.getType() == Type.BLOCK) {
			BlockPos pos = rayTrace.getBlockPos();
			BlockState state = world.getBlockState(pos);
			Block block = state.getBlock();

			// try filling from the cauldron
			if (Config.SERVER.teapot.fillFromCauldron() && block == Blocks.WATER_CAULDRON && state.getValue(BlockStateProperties.LEVEL_CAULDRON) == 3) {
				world.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());
				stack = ItemUtils.createFilledResult(stack, player, new ItemStack(Registration.teapot_water.get()));
				return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
			}

			// we use name for lookup to prevent default fluid conflicts
			Fluid fluid = state.getFluidState().getType();
			if(fluid != Fluids.EMPTY) {
				// try for water or milk using the config lists
				Item item = null;
				if (fluid.is(FluidTags.WATER)) {
					item = Registration.teapot_water.get();
				} // TODO: milk when mods make a standard

				// if either one is found, update the stack
				if(item != null) {
					// water is considered infinite unless disabled in the config
					if(!Config.SERVER.teapot.infiniteWater()) {
						Direction side = rayTrace.getDirection();
						// unable to modify the block, fail
						if (!world.mayInteract(player, pos) || !player.mayUseItemAt(pos.relative(side), side, stack) || !(state.getBlock() instanceof BucketPickup)) {
							return new InteractionResultHolder<>(InteractionResult.FAIL, stack);
						}
						((BucketPickup)state.getBlock()).pickupBlock(world, pos, state);
					}

					stack = ItemUtils.createFilledResult(stack, player, new ItemStack(item));

					// TODO: fluid sound based on fluid
					player.playSound(SoundEvents.BUCKET_FILL, 1.0f, 1.0f);
					return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
				}
			}
		}
		return new InteractionResultHolder<>(InteractionResult.FAIL, stack);
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
		// only work if the teapot is empty and right clicking a cow
		if(Config.SERVER.teapot.canMilkCows() && target instanceof Cow) {
			// sound
			player.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);

			// fill with milk
			player.setItemInHand(hand, ItemUtils.createFilledResult(stack.copy(), player, new ItemStack(Registration.teapot_milk.get())));
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}
}