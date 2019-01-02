package elucent.simplytea.item;

import elucent.simplytea.SimplyTea;
import elucent.simplytea.core.Config;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemTeaChamomile extends ItemTeaCup {
	public ItemTeaChamomile(String name, boolean addToTab) {
		super(name, Config.tea.chamomile.hunger, Config.tea.chamomile.saturation, addToTab);
		if (Config.tea.chamomile.hearts > 0) {
			this.setPotionEffect(new PotionEffect(SimplyTea.restful, 600, Config.tea.chamomile.hearts - 1), 1.0f);
		}
	}

    @Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
    	// skip effect if caffeinated
        if (!player.isPotionActive(SimplyTea.caffeinated)) {
        	super.onFoodEaten(stack, worldIn, player);
        }
    }
}
