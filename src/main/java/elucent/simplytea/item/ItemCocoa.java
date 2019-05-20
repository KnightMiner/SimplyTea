package elucent.simplytea.item;

import elucent.simplytea.core.Config;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemCocoa extends ItemTeaCup {

	private static final ItemStack MILK_BUCKET = new ItemStack(Items.MILK_BUCKET);

	public ItemCocoa(String name) {
		super(name, Config.tea.cocoa.hunger, Config.tea.cocoa.saturation);
	}

    @Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
        if (Config.tea.cocoa.clear_effects && !worldIn.isRemote) {
			player.curePotionEffects(MILK_BUCKET);
        }
    }
}
