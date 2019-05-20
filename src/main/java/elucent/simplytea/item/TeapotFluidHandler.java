package elucent.simplytea.item;

import elucent.simplytea.SimplyTea;
import elucent.simplytea.core.Config;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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

		// Check contained fluid if meta not 0
		int meta = stack.getMetadata();
		if(meta > 0) {
			// determine what fluids we ha
			String[] fluidNames = meta == 2 ? Config.teapot.milks : Config.teapot.waters;
			if (fluidNames.length != 0) {
				// if we find the fluid, return that
				Fluid registered = FluidRegistry.getFluid(fluidNames[0]);
				if(registered != null) {
					fluid = new FluidStack(registered, Fluid.BUCKET_VOLUME);
				}
			}
		}

		return new IFluidTankProperties[] {new FluidTankProperties(fluid, Fluid.BUCKET_VOLUME)};
	}

    @Override
    public int fill(FluidStack resource, boolean doFill) {
    	// must be empty and bucket amount
        if (stack.getItemDamage() != 0 || resource == null || resource.getFluid() == null || resource.amount < Fluid.BUCKET_VOLUME) {
        	return 0;
        }

    	// only fill with water or milk
        String fluid = resource.getFluid().getName();
        int meta = 2; // assume milk to start, we still end up allowing water past first
        if (Config.teapot.waterSet.contains(fluid)) {
			meta = 1;
        } else if(!Config.teapot.milkSet.contains(fluid)) {
        	return 0;
		}

        if (doFill) {
            stack = new ItemStack(SimplyTea.teapot, 1, meta);
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
