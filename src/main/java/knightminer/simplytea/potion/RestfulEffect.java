package knightminer.simplytea.potion;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

import java.util.ArrayList;
import java.util.List;

public class RestfulEffect extends MobEffect {
	private static final List<MobEffect> CONFLICTING = new ArrayList<>();

	public RestfulEffect() {
		super(MobEffectCategory.BENEFICIAL, 0xAD601A);
	}

	/** Marks an effect as preventing restfulness */
	public static void addConflict(MobEffect effect) {
		CONFLICTING.add(effect);
	}

	/**
	 * Removes the effects that conflict with restful
	 * @return true  If the effect was removed
	 */
	public static boolean removeConflicts(LivingEntity entity) {
		boolean hasConflicts = false;
		for (MobEffect effect : CONFLICTING) {
			if (entity.hasEffect(effect)) {
				entity.removeEffect(effect);
				hasConflicts = true;
			}
		}
		return hasConflicts;
	}
}
