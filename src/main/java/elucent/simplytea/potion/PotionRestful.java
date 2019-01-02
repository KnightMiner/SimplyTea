package elucent.simplytea.potion;

import java.util.ArrayList;
import java.util.List;

import elucent.simplytea.SimplyTea;
import net.minecraft.item.ItemStack;

public class PotionRestful extends ModPotion {

	public PotionRestful(String name) {
		super(name, false, 0xAD601A);
	}

    @Override
	public List<ItemStack> getCurativeItems() {
    	// does not cure with milk, but cures with caffeinated teas
    	List<ItemStack> list = new ArrayList<>();
    	list.add(new ItemStack(SimplyTea.cup_tea_black, 1, 0));
    	list.add(new ItemStack(SimplyTea.cup_tea_black, 1, 1));
    	list.add(new ItemStack(SimplyTea.cup_tea_green, 1, 0));
    	list.add(new ItemStack(SimplyTea.cup_tea_green, 1, 1));
    	return list;
    }
}
