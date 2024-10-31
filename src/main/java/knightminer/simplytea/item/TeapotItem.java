package knightminer.simplytea.item;

import org.jetbrains.annotations.Nullable;

import knightminer.simplytea.core.Config;
import knightminer.simplytea.core.Registration;
import knightminer.simplytea.fluid.FluidTeapotWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.Optional;

public class TeapotItem extends TooltipItem {
	public TeapotItem(Properties props) {
		super(props);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		BlockHitResult rayTrace = getPlayerPOVHitResult(world, player, ClipContext.Fluid.SOURCE_ONLY);
		if (rayTrace.getType() != Type.BLOCK) {
			return InteractionResultHolder.pass(stack);
		}
		
		BlockPos pos = rayTrace.getBlockPos();
		Direction side = rayTrace.getDirection();
		BlockState state = world.getBlockState(pos);

		if (!world.mayInteract(player, pos) || !player.mayUseItemAt(pos.relative(side), side, stack)) {
			return InteractionResultHolder.fail(stack);
		}

		ItemStack filledStack = ItemStack.EMPTY;
		if (state.getBlock() instanceof BucketPickup bucketPickup) {
			// special case for infinite water
			if (FluidTeapotWrapper.isWater(state.getFluidState().getType()) && Config.SERVER.teapot.infiniteWater()) {
				filledStack = new ItemStack(Registration.teapot_water);
				Optional<SoundEvent> sound = bucketPickup.getPickupSound(state);
				if (sound.isPresent()) {
					player.playSound(sound.get(), 1.0f, 1.0f);
				}
			}
			
			// use teapot like a bucket to get fluid, should work in most cases
			if (filledStack.isEmpty()) {
				FluidActionResult actionResult = FluidUtil.tryPickUpFluid(stack, player, world, pos, side);
				if (actionResult.isSuccess()) {
					filledStack = actionResult.getResult();
				}
			}
		}

		if (!filledStack.isEmpty()) {
			ItemStack filledResult = ItemUtils.createFilledResult(stack, player, filledStack);
			return InteractionResultHolder.sidedSuccess(filledResult, world.isClientSide());
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
			player.setItemInHand(hand, ItemUtils.createFilledResult(stack.copy(), player, new ItemStack(Registration.teapot_milk)));
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
		return new FluidTeapotWrapper(stack);
	}
}
