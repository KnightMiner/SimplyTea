package knightminer.simplytea.item;

import knightminer.simplytea.core.Config;
import knightminer.simplytea.core.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

public class TeapotItem extends TooltipItem {

	public TeapotItem(Properties props) {
		super(props);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		BlockHitResult rayTrace = getPlayerPOVHitResult(world, player, ClipContext.Fluid.SOURCE_ONLY);
		if(rayTrace.getType() == HitResult.Type.MISS || rayTrace.getType() != HitResult.Type.BLOCK) {
			return InteractionResultHolder.pass(stack);
		}

		BlockPos blockpos = rayTrace.getBlockPos();
		Direction direction = rayTrace.getDirection();
		BlockPos nearPos = blockpos.relative(direction);

		if(world.mayInteract(player, blockpos) && player.mayUseItemAt(nearPos, direction, stack)) {
			BlockState blockstate = world.getBlockState(blockpos);

			if(blockstate.getBlock() instanceof BucketPickup) {
				Fluid fluid = blockstate.getFluidState().getType();
				if(fluid == Fluids.EMPTY) {
					return InteractionResultHolder.fail(stack);
				}

				BucketPickup bucketpickup = (BucketPickup) blockstate.getBlock();

				boolean isWater = ForgeRegistries.FLUIDS.tags().getTag(FluidTags.WATER).contains(fluid);
				boolean isMilk = ForgeRegistries.FLUIDS.tags().getTag(Tags.Fluids.MILK).contains(fluid);

				if(!isWater && !isMilk) {
					return InteractionResultHolder.fail(stack);
				}

				boolean blockHasFluid = true;
				if(!Config.SERVER.teapot.infiniteWater()) {
					ItemStack filledBucket = bucketpickup.pickupBlock(world, blockpos, blockstate);
					blockHasFluid = !filledBucket.isEmpty();
				}

				if(blockHasFluid) {
					player.awardStat(Stats.ITEM_USED.get(this));
					bucketpickup.getPickupSound(blockstate).ifPresent((sound) -> {
						player.playSound(sound, 1.0F, 1.0F);
					});
					world.gameEvent(player, GameEvent.FLUID_PICKUP, blockpos);

					ItemStack filledTeapot;
					if(isWater) {
						filledTeapot = ItemUtils.createFilledResult(stack, player, new ItemStack(Registration.teapot_water.get()));
					}else {
						// TODO: test
						filledTeapot = ItemUtils.createFilledResult(stack, player, new ItemStack(Registration.teapot_milk.get()));
					}

					return InteractionResultHolder.sidedSuccess(filledTeapot, world.isClientSide());
				}
			}
		}

		return InteractionResultHolder.fail(stack);
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