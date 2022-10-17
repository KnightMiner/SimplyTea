package knightminer.simplytea.potion;


import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class InvigoratedEffect extends MobEffect {
	public InvigoratedEffect() {
		super(MobEffectCategory.BENEFICIAL, 0xD79659);

		this.addAttributeModifier(Attributes.ATTACK_DAMAGE,    "38dd13a2-b4f1-11eb-8529-0242ac130003", 1, Operation.ADDITION);
		this.addAttributeModifier(Attributes.ATTACK_KNOCKBACK, "401daf1e-b4f1-11eb-8529-0242ac130003", 1, Operation.ADDITION);
	}
}
