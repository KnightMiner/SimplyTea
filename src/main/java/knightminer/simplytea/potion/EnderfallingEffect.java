package knightminer.simplytea.potion;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class EnderfallingEffect extends Effect {

	public EnderfallingEffect() {
		super(EffectType.BENEFICIAL, 0x4E2043);

		this.addAttributesModifier(SharedMonsterAttributes.KNOCKBACK_RESISTANCE, "8ACB8640-6D4E-11E9-A923-1681BE663D3E", 0.25, Operation.MULTIPLY_TOTAL);
	}
}
