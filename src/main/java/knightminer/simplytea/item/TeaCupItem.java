package knightminer.simplytea.item;

import knightminer.simplytea.core.config.TeaDrink;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class TeaCupItem extends Item {
	public static final String HONEY_TAG = "with_honey";
	private static final Component WITH_HONEY = Component.translatable("item.simplytea.cup.with_honey")
			.withStyle(style -> style.withColor(TextColor.fromRgb(0xFF9116)));

	public TeaCupItem(Properties props) {
		super(props);
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return stack.getItem().isEdible() ? UseAnim.DRINK : UseAnim.NONE;
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		return stack.getDamageValue() > 0;
	}

	@Override
	public ItemStack getCraftingRemainingItem(ItemStack stack) {
		if (stack.getDamageValue() + 1 >= stack.getMaxDamage()) {
			return super.getCraftingRemainingItem(stack);
		}
		stack = stack.copy();
		stack.setDamageValue(stack.getDamageValue()+1);
		return stack;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity living) {
		if (this.isEdible()) {
			ItemStack result = stack.getCraftingRemainingItem();
			boolean hasHoney = hasHoney(stack, HONEY_TAG);
			living.curePotionEffects(stack); /// remove conflicting teas
			living.eat(worldIn, stack);
			// we handle effects directly so it can be stack sensitive
			FoodProperties food = getFoodProperties(stack, living);
			if (food instanceof TeaDrink drink) {
				MobEffectInstance effectInstance = drink.getEffect(hasHoney);
				if (effectInstance != null) {
					living.addEffect(effectInstance);
				}
			}
			return result;
		}
		return stack;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		if (hasHoney(stack, HONEY_TAG)) {
			tooltip.add(WITH_HONEY);
		}
	}

	/** Ads honey to the given tea */
	public static ItemStack withHoney(ItemStack stack, String tag) {
		stack.getOrCreateTag().putBoolean(tag, true);
		return stack;
	}

	/** Checks if the given tea contains honey */
	public static boolean hasHoney(ItemStack stack, String tag) {
		CompoundTag nbt = stack.getTag();
		return nbt != null && nbt.getBoolean(tag);
	}
}