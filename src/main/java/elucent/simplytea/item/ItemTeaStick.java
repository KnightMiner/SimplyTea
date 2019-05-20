package elucent.simplytea.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class ItemTeaStick extends ItemBase {

	public ItemTeaStick(String name) {
		super(name, true);
	}

	@Override
	public int getItemBurnTime(ItemStack stack) {
		return 100;
	}

	@Override
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(I18n.format(this.getUnlocalizedName() + ".tooltip"));
	}
}
