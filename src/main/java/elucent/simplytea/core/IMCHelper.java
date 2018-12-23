package elucent.simplytea.core;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class IMCHelper {
	/**
	 * Adds a Tinkers Construct drying rack recipe
	 * @param input   Input stack
	 * @param output  Output stack
	 * @param time    Drying time in seconds
	 */
	public static void addTinkersDrying(ItemStack input, ItemStack output, int time) {
		NBTTagCompound tagCompound = new NBTTagCompound();
		tagCompound.setTag("input", input.writeToNBT(new NBTTagCompound()));
		tagCompound.setTag("output", output.writeToNBT(new NBTTagCompound()));
		tagCompound.setInteger("time", time);
		FMLInterModComms.sendMessage("tconstruct", "addDryingRecipe", tagCompound);
	}
}
