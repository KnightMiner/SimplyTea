package knightminer.simplytea.item;

import net.minecraft.world.item.ItemStack;

public class HotTeapotItem extends TooltipItem {
	public HotTeapotItem(Properties props) {
		super(props);
	}

	@Override
	public ItemStack getCraftingRemainingItem(ItemStack stack) {
		stack = stack.copy();
		stack.setDamageValue(stack.getDamageValue()+1);
		if (stack.getDamageValue() >= stack.getMaxDamage()) {
			return super.getCraftingRemainingItem(stack);
		}
		return stack;
	}
}