package knightminer.simplytea.item;

import knightminer.simplytea.core.Config;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.PotionEvent.PotionRemoveEvent;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

import net.minecraft.item.Item.Properties;

public class CocoaItem extends TeaCupItem {
	public static final String CINNAMON_TAG = "with_cinnamon";
	private static final ITextComponent WITH_CINNAMON = new TranslationTextComponent("item.simplytea.cup.with_cinnamon")
			.withStyle(style -> style.withColor(Color.fromRgb(0x805232)));

	private static final ItemStack MILK_BUCKET = new ItemStack(Items.MILK_BUCKET);

	public CocoaItem(Properties props) {
		super(props);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity living) {
		if (this.isEdible()) {
			ItemStack result = getContainerItem(stack);
			living.eat(worldIn, stack);
			if (!worldIn.isClientSide && Config.SERVER.cocoa.clearsEffects()) {
				// logic basically copied from living entity, so we can choose which effects to remove
				Iterator<EffectInstance> itr = living.getActiveEffectsMap().values().iterator();
				boolean hasCinnamon = hasHoney(stack, CINNAMON_TAG);
				while (itr.hasNext()) {
					EffectInstance effect = itr.next();
					if ((Config.SERVER.cocoa.clearsPositive() || !effect.getEffect().isBeneficial())
							&& effect.isCurativeItem(MILK_BUCKET) && !MinecraftForge.EVENT_BUS.post(new PotionRemoveEvent(living, effect))) {
						living.onEffectRemoved(effect);
						itr.remove();
						// need honey to remove more than one negative effect
						if (!hasCinnamon) {
							break;
						}
					}
				}
			}
			return result;
		}
		return stack;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if (hasHoney(stack, CINNAMON_TAG)) {
			tooltip.add(WITH_CINNAMON);
		}
	}
}
