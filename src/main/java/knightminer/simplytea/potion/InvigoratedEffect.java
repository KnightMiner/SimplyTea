package knightminer.simplytea.potion;


import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class InvigoratedEffect extends Effect {
	public InvigoratedEffect() {
		super(EffectType.BENEFICIAL, 0xD79659);

		this.addAttributesModifier(Attributes.ATTACK_DAMAGE,    "38dd13a2-b4f1-11eb-8529-0242ac130003", 1, Operation.ADDITION);
		this.addAttributesModifier(Attributes.ATTACK_KNOCKBACK, "401daf1e-b4f1-11eb-8529-0242ac130003", 1, Operation.ADDITION);
	}
}
