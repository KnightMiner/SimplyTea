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

public class TeaCupItem extends Item {
	public static final String HONEY_TAG = "with_honey";
	private static final ITextComponent WITH_HONEY = new TranslationTextComponent("item.simplytea.cup.with_honey")
			.modifyStyle(style -> style.setColor(Color.fromInt(0xFF9116)));

	public TeaCupItem(Properties props) {
		super(props);
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return stack.getItem().isFood() ? UseAction.DRINK : UseAction.NONE;
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return stack.getDamage() > 0;
	}

	@Override
	public ItemStack getContainerItem(ItemStack stack) {
		if (stack.getDamage() + 1 >= stack.getMaxDamage()) {
			return super.getContainerItem(stack);
		}
		stack = stack.copy();
		stack.setDamage(stack.getDamage()+1);
		return stack;
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity living) {
		if (this.isFood()) {
			ItemStack result = stack.getContainerItem();
			boolean hasHoney = hasHoney(stack);
			living.onFoodEaten(worldIn, stack);
			// we handle effects directly so it can be stack sensitive
			Food food = getFood();
			if (food instanceof TeaDrink) {
				EffectInstance effectInstance = ((TeaDrink) food).getEffect(hasHoney);
				if (effectInstance != null) {
					living.addPotionEffect(effectInstance);
				}
			}
			return result;
		}
		return stack;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if (hasHoney(stack)) {
			tooltip.add(WITH_HONEY);
		}
	}

	/** Ads honey to the given tea */
	public static ItemStack withHoney(ItemStack stack) {
		stack.getOrCreateTag().putBoolean(HONEY_TAG, true);
		return stack;
	}

	/** Checks if the given tea contains honey */
	public static boolean hasHoney(ItemStack stack) {
		CompoundNBT nbt = stack.getTag();
		return nbt != null && nbt.getBoolean(HONEY_TAG);
	}
}