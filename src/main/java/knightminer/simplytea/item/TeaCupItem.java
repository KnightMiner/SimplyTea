package knightminer.simplytea.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.world.World;

public class TeaCupItem extends Item {

	public TeaCupItem(Properties props) {
		super(props);
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return stack.getItem().isFood() ? UseAction.DRINK : UseAction.NONE;
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return stack.getDamage() > 0;
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

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity living) {
		if(this.isFood()) {
			living.onFoodEaten(worldIn, stack.copy());
			stack = getContainerItem(stack);
		}
		return stack;
	}
}