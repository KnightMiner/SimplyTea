package knightminer.simplytea.potion;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class EnderfallingEffect extends MobEffect {

	public EnderfallingEffect() {
		super(MobEffectCategory.BENEFICIAL, 0x4E2043);

		this.addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, "8ACB8640-6D4E-11E9-A923-1681BE663D3E", 0.25, Operation.MULTIPLY_TOTAL);
	}
}
