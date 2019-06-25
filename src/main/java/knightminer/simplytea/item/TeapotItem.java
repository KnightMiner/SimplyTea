package knightminer.simplytea.item;

import knightminer.simplytea.core.Config;
import knightminer.simplytea.core.Registration;
import knightminer.simplytea.core.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
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
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;

public class TeapotItem extends TooltipItem {
	public TeapotItem(Properties props) {
		super(props);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getHeldItem(hand);
		RayTraceResult rayTrace = this.rayTrace(world, player, RayTraceContext.FluidMode.SOURCE_ONLY);
		if (rayTrace != null && rayTrace.getType() == Type.BLOCK) {
			BlockRayTraceResult trace = (BlockRayTraceResult)rayTrace;
			BlockPos pos = trace.getPos();
			BlockState state = world.getBlockState(pos);
			Block block = state.getBlock();

			// try filling from the cauldron
			if (Config.teapot.fill_from_cauldron && block == Blocks.CAULDRON && state.get(CauldronBlock.LEVEL) == 3) {
				((CauldronBlock)Blocks.CAULDRON).setWaterLevel(world, pos, state, 0);
				stack = Util.fillContainer(player, stack, new ItemStack(Registration.teapot_water));
				return new ActionResult<ItemStack>(ActionResultType.SUCCESS, stack);
			}

			// we use name for lookup to prevent default fluid conflicts
			Fluid fluid = state.getFluidState().getFluid();
			if(fluid != Fluids.EMPTY) {
				// try for water or milk using the config lists
				Item item = null;
				if (FluidTags.WATER.contains(fluid)) {
					item = Registration.teapot_water;
				} // TODO: milk when mods make a standard

				// if either one is found, update the stack
				if(item != null) {
					// water is considered infinite unless disabled in the config
					if(!Config.teapot.infinite_water) {
						world.setBlockState(pos, Blocks.AIR.getDefaultState());
					}

					stack = Util.fillContainer(player, stack, new ItemStack(item));

					// TODO: fluid sound based on fluid
					player.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0f, 1.0f);
					return new ActionResult<ItemStack>(ActionResultType.SUCCESS, stack);
				}
			}
		}
		return new ActionResult<ItemStack>(ActionResultType.FAIL, stack);
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
		// only work if the teapot is empty and right clicking a cow
		if(Config.teapot.milk_cow && target instanceof CowEntity && !player.isCreative()) {
			// sound
			player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);

			// fill with milk
			player.setHeldItem(hand, Util.fillContainer(player, stack, new ItemStack(Registration.teapot_milk)));
			return true;
		}
		return false;
	}
}