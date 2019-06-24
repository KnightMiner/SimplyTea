package knightminer.simplytea.potion;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

import java.util.ArrayList;
import java.util.List;

public class RestfulEffect extends Effect {

	public RestfulEffect() {
		super(EffectType.BENEFICIAL, 0xAD601A);
	}

	@Override
	public List<ItemStack> getCurativeItems() {
		// no curing with milk, that would be weird
		return new ArrayList<ItemStack>();
	}
}
