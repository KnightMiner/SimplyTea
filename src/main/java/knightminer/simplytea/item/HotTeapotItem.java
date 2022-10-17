package knightminer.simplytea.item;

import net.minecraft.world.item.ItemStack;

import net.minecraft.world.item.Item.Properties;

public class HotTeapotItem extends TooltipItem {
	public HotTeapotItem(Properties props) {
		super(props);
	}

	@Override
	public ItemStack getContainerItem(ItemStack stack) {
		stack = stack.copy();
		stack.setDamageValue(stack.getDamageValue()+1);
		if (stack.getDamageValue() >= stack.getMaxDamage()) {
			return super.getContainerItem(stack);
		}
		return stack;
	}
}