package elucent.simplytea.item;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import elucent.simplytea.SimplyTea;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class TeapotFluidHandler implements ICapabilityProvider, IFluidHandlerItem {

	private ItemStack stack;
    public TeapotFluidHandler(ItemStack stack) {
    	this.stack = stack;
	}

	@Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY;
    }

    @Override
    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY) {
            return CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY.cast(this);
        }
        return null;
    }

	@Override
	public IFluidTankProperties[] getTankProperties() {
		FluidStack fluid = null;
		int meta = stack.getMetadata();
		if (meta == 1) {
			fluid = new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME);
		} else if (meta == 2) {
			Fluid milk = FluidRegistry.getFluid("milk");
			if (milk != null) {
				fluid = new FluidStack(milk, Fluid.BUCKET_VOLUME);
			}
		}
		return new IFluidTankProperties[] {new FluidTankProperties(fluid, Fluid.BUCKET_VOLUME)};
	}

    @Override
    public int fill(FluidStack resource, boolean doFill) {
    	// must be empty and bucket amount
        if (stack.getItemDamage() != 0 || resource == null || resource.amount < Fluid.BUCKET_VOLUME) {
        	return 0;
        }

    	// only fill with water or milk
        Fluid fluid = resource.getFluid();
        if (fluid != FluidRegistry.WATER && !fluid.getName().equals("milk")) {
        	return 0;
        }

        if (doFill) {
            stack = new ItemStack(SimplyTea.teapot, 1, fluid == FluidRegistry.WATER ? 1 : 2);
        }

        return Fluid.BUCKET_VOLUME;
    }

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) {
		return null;
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		return null;
	}

	@Override
	public ItemStack getContainer() {
		return new ItemStack(SimplyTea.teapot);
	}
}
