package knightminer.simplytea.potion;

import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

import java.util.ArrayList;
import java.util.List;

public class CaffeinatedEffect extends Effect {

	public CaffeinatedEffect() {
		super(EffectType.BENEFICIAL, 0x66300E);

		this.addAttributesModifier(Attributes.MOVEMENT_SPEED, "7BB42B36-75AC-448D-9BE2-B318E42D6898", 0.06, Operation.MULTIPLY_TOTAL);
		this.addAttributesModifier(Attributes.ATTACK_SPEED, "C3B3B80D-B94D-4C67-83FC-5A893EB98230", 0.05, Operation.MULTIPLY_TOTAL);
	}

	@Override
	public List<ItemStack> getCurativeItems() {
   	// no curing with milk, that would be weird
		return new ArrayList<ItemStack>();
	}
}
