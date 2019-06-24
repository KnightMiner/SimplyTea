package knightminer.simplytea.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;

public class CocoaItem extends TeaCupItem {

	private static final ItemStack MILK_BUCKET = new ItemStack(Items.MILK_BUCKET);

	public CocoaItem(Properties props) {
		super(props);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity living) {
		if(this.isFood()) {
			living.onFoodEaten(worldIn, stack.copy());
			stack = getContainerItem(stack);
			living.curePotionEffects(MILK_BUCKET);
		}
		return stack;
	}
}
