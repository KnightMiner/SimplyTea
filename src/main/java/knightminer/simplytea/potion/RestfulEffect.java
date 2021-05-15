package knightminer.simplytea.potion;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

import java.util.ArrayList;
import java.util.List;

public class RestfulEffect extends Effect {
	private static final List<Effect> CONFLICTING = new ArrayList<>();

	public RestfulEffect() {
		super(EffectType.BENEFICIAL, 0xAD601A);
	}

	/** Marks an effect as preventing restfulness */
	public static void addConflict(Effect effect) {
		CONFLICTING.add(effect);
	}

	/**
	 * Removes the effects that conflict with restful
	 * @return true  If the effect was removed
	 */
	public static boolean removeConflicts(LivingEntity entity) {
		boolean hasConflicts = false;
		for (Effect effect : CONFLICTING) {
			if (entity.isPotionActive(effect)) {
				entity.removePotionEffect(effect);
				hasConflicts = true;
			}
		}
		return hasConflicts;
	}
}
