package elucent.simplytea.item;

import net.minecraft.item.ItemStack;

public class ItemTeaStick extends ItemTooltip {

	public ItemTeaStick(String name) {
		super(name);
	}

	@Override
	public int getItemBurnTime(ItemStack stack) {
		return 100;
	}
}
