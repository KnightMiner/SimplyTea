package knightminer.simplytea.fluid;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import knightminer.simplytea.core.Config;
import knightminer.simplytea.core.Registration;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public class FluidTeapotWrapper implements IFluidHandlerItem, ICapabilityProvider {
    private final LazyOptional<IFluidHandlerItem> holder = LazyOptional.of(() -> this);

	@NotNull
    protected ItemStack container;
    protected FluidStack fluid = FluidStack.EMPTY;

    public FluidTeapotWrapper(ItemStack container) {
        this.container = container;
    }

    @Override
	public int getTanks() {
		return 1;
	}

	@Override
	public @NotNull FluidStack getFluidInTank(int tank) {
        return FluidStack.EMPTY;
	}

	@Override
	public int getTankCapacity(int tank) {
		return Config.SERVER.teapot.teapotCapacity();
	}

    public static boolean isWater(Fluid fluid) {
        // Many modded fluids use the WATER tag to give the fluids entity interaction
        // Only accept Fluids.WATER and not any other fluid tagged as WATER
        return fluid.isSame(Fluids.WATER) || fluid.isSame(Fluids.FLOWING_WATER);
    }

    public static boolean isMilk(Fluid fluid) {
        // Be more flexible with MILK because if it's tagged as MILK then it should really be milk
        return fluid.is(Tags.Fluids.MILK) || fluid.getBucket() == Items.MILK_BUCKET;
    }

	@Override
	public boolean isFluidValid(int tank, @NotNull FluidStack fluid) {
		return isWater(fluid.getFluid()) || isMilk(fluid.getFluid());
	}

	@Override
	public int fill(FluidStack resource, FluidAction action) {
		if (container.getCount() != 1 || !isFluidValid(0, resource) || resource.getAmount() < Config.SERVER.teapot.teapotCapacity()) {
            return 0;
        }
		
        if (action.execute()) {
		    if (isWater(resource.getFluid())) {
                this.container = new ItemStack(Registration.teapot_water);
            } else if (isMilk(resource.getFluid())) {
                this.container = new ItemStack(Registration.teapot_milk);
            }
		}
		
		return Config.SERVER.teapot.teapotCapacity();
	}

	@Override
	public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
        return FluidStack.EMPTY;
	}

	@Override
	public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
		return FluidStack.EMPTY;
	}

	@Override
	public @NotNull ItemStack getContainer() {
        return container;
	}

    @Override
	public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side) {
		return ForgeCapabilities.FLUID_HANDLER_ITEM.orEmpty(capability, holder);
	}
}
