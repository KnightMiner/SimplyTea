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
		// full pots do not have a fluid handler, the pot can only be filled
        return stack.getItemDamage() == 0 && capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY;
    }

    @Override
    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if(stack.getItemDamage() == 0 && capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY) {
            return CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY.cast(this);
        }
        return null;
    }

	@Override
	public IFluidTankProperties[] getTankProperties() {
		return new IFluidTankProperties[] {new FluidTankProperties(null, Fluid.BUCKET_VOLUME)};
	}

    @Override
    public int fill(FluidStack resource, boolean doFill) {
    	// only fill with water
        if (stack.getItemDamage() == 1 || resource == null || resource.amount < Fluid.BUCKET_VOLUME || resource.getFluid() != FluidRegistry.WATER) {
        	return 0;
        }

        if (doFill) {
            stack = new ItemStack(SimplyTea.teapot, 1, 1);
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
		return stack;
	}
}
