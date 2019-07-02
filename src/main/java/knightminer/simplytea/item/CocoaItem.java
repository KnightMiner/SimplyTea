package knightminer.simplytea.item;

import knightminer.simplytea.core.Config;
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
			if (Config.SERVER.cocoa.clearsEffects()) {
				living.curePotionEffects(MILK_BUCKET);
			}
		}
		return stack;
	}
}
