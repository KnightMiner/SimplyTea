package knightminer.simplytea.item;

import net.minecraft.item.ItemStack;

public class HotTeapotItem extends TooltipItem {
	public HotTeapotItem(Properties props) {
		super(props);
	}

	@Override
	public ItemStack getContainerItem(ItemStack stack) {
		stack = stack.copy();
		stack.setDamage(stack.getDamage()+1);
		if (stack.getDamage() >= stack.getMaxDamage()) {
			return super.getContainerItem(stack);
		}
		return stack;
	}
}