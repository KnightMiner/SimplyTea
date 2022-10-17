package knightminer.simplytea.item;

import knightminer.simplytea.core.config.TeaDrink;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.item.Item.Properties;

public class TeaCupItem extends Item {
	public static final String HONEY_TAG = "with_honey";
	private static final ITextComponent WITH_HONEY = new TranslationTextComponent("item.simplytea.cup.with_honey")
			.withStyle(style -> style.withColor(Color.fromRgb(0xFF9116)));

	public TeaCupItem(Properties props) {
		super(props);
	}

	@Override
	public UseAction getUseAnimation(ItemStack stack) {
		return stack.getItem().isEdible() ? UseAction.DRINK : UseAction.NONE;
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return stack.getDamageValue() > 0;
	}

	@Override
	public ItemStack getContainerItem(ItemStack stack) {
		if (stack.getDamageValue() + 1 >= stack.getMaxDamage()) {
			return super.getContainerItem(stack);
		}
		stack = stack.copy();
		stack.setDamageValue(stack.getDamageValue()+1);
		return stack;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity living) {
		if (this.isEdible()) {
			ItemStack result = stack.getContainerItem();
			boolean hasHoney = hasHoney(stack, HONEY_TAG);
			living.curePotionEffects(stack); /// remove conflicting teas
			living.eat(worldIn, stack);
			// we handle effects directly so it can be stack sensitive
			Food food = getFoodProperties();
			if (food instanceof TeaDrink) {
				EffectInstance effectInstance = ((TeaDrink) food).getEffect(hasHoney);
				if (effectInstance != null) {
					living.addEffect(effectInstance);
				}
			}
			return result;
		}
		return stack;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
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
		CompoundNBT nbt = stack.getTag();
		return nbt != null && nbt.getBoolean(tag);
	}
}