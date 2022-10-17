package knightminer.simplytea.item;

import knightminer.simplytea.core.Config;
import knightminer.simplytea.core.Registration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.DrinkHelper;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;

import net.minecraft.item.Item.Properties;

public class TeapotItem extends TooltipItem {
	public TeapotItem(Properties props) {
		super(props);
	}

	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getItemInHand(hand);
		BlockRayTraceResult rayTrace = getPlayerPOVHitResult(world, player, RayTraceContext.FluidMode.SOURCE_ONLY);
		if (rayTrace.getType() == Type.BLOCK) {
			BlockPos pos = rayTrace.getBlockPos();
			BlockState state = world.getBlockState(pos);
			Block block = state.getBlock();

			// try filling from the cauldron
			if (Config.SERVER.teapot.fillFromCauldron() && block == Blocks.CAULDRON && state.getValue(CauldronBlock.LEVEL) == 3) {
				((CauldronBlock)Blocks.CAULDRON).setWaterLevel(world, pos, state, 0);
				stack = DrinkHelper.createFilledResult(stack, player, new ItemStack(Registration.teapot_water));
				return new ActionResult<>(ActionResultType.SUCCESS, stack);
			}

			// we use name for lookup to prevent default fluid conflicts
			Fluid fluid = state.getFluidState().getType();
			if(fluid != Fluids.EMPTY) {
				// try for water or milk using the config lists
				Item item = null;
				if (fluid.is(FluidTags.WATER)) {
					item = Registration.teapot_water;
				} // TODO: milk when mods make a standard

				// if either one is found, update the stack
				if(item != null) {
					// water is considered infinite unless disabled in the config
					if(!Config.SERVER.teapot.infiniteWater()) {
						Direction side = rayTrace.getDirection();
						// unable to modify the block, fail
						if (!world.mayInteract(player, pos) || !player.mayUseItemAt(pos.relative(side), side, stack) || !(state.getBlock() instanceof IBucketPickupHandler)) {
							return new ActionResult<>(ActionResultType.FAIL, stack);
						}
						((IBucketPickupHandler)state.getBlock()).takeLiquid(world, pos, state);
					}

					stack = DrinkHelper.createFilledResult(stack, player, new ItemStack(item));

					// TODO: fluid sound based on fluid
					player.playSound(SoundEvents.BUCKET_FILL, 1.0f, 1.0f);
					return new ActionResult<>(ActionResultType.SUCCESS, stack);
				}
			}
		}
		return new ActionResult<>(ActionResultType.FAIL, stack);
	}

	@Override
	public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
		// only work if the teapot is empty and right clicking a cow
		if(Config.SERVER.teapot.canMilkCows() && target instanceof CowEntity) {
			// sound
			player.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);

			// fill with milk
			player.setItemInHand(hand, DrinkHelper.createFilledResult(stack.copy(), player, new ItemStack(Registration.teapot_milk)));
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}
}