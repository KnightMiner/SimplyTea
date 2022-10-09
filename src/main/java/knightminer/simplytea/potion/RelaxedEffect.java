package knightminer.simplytea.potion;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

/** Effectively just slow regeneration */
public class RelaxedEffect extends MobEffect {
	public RelaxedEffect() {
		super(MobEffectCategory.BENEFICIAL, 0xA0E8A7);
	}

	@Override
	public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier) {
		if (entityLivingBaseIn.getHealth() < entityLivingBaseIn.getMaxHealth()) {
			entityLivingBaseIn.heal(1.0F);
		}
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		// at level 1, 1 half heart every 60 seconds
		// level 2, every 30 seconds
		// level 3, every 20 seconds
		int frequency = 1200 / (amplifier + 1);
		return frequency == 0 || duration % frequency == 1;
	}
}
