package knightminer.simplytea.potion;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class CaffeinatedEffect extends MobEffect {

	public CaffeinatedEffect() {
		super(MobEffectCategory.BENEFICIAL, 0x66300E);

		this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "7BB42B36-75AC-448D-9BE2-B318E42D6898", 0.06, Operation.MULTIPLY_TOTAL);
		this.addAttributeModifier(Attributes.ATTACK_SPEED, "C3B3B80D-B94D-4C67-83FC-5A893EB98230", 0.05, Operation.MULTIPLY_TOTAL);
	}
}
