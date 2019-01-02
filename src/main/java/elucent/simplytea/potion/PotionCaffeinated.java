package elucent.simplytea.potion;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;

public class PotionCaffeinated extends ModPotion {

	public PotionCaffeinated(String name) {
		super(name, false, 0x66300E);

		this.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "7BB42B36-75AC-448D-9BE2-B318E42D6898", 0.1, 2);
		this.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, "C3B3B80D-B94D-4C67-83FC-5A893EB98230", 0.1, 2);
	}

    @Override
	public List<ItemStack> getCurativeItems() {
    	// no curing with milk, that would be weird
    	return new ArrayList<ItemStack>();
    }
}
