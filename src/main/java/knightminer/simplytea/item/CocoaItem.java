package knightminer.simplytea.item;

import knightminer.simplytea.core.Config;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.PotionEvent.PotionRemoveEvent;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

import net.minecraft.world.item.Item.Properties;

public class CocoaItem extends TeaCupItem {
	public static final String CINNAMON_TAG = "with_cinnamon";
	private static final Component WITH_CINNAMON = new TranslatableComponent("item.simplytea.cup.with_cinnamon")
			.withStyle(style -> style.withColor(TextColor.fromRgb(0x805232)));

	private static final ItemStack MILK_BUCKET = new ItemStack(Items.MILK_BUCKET);

	public CocoaItem(Properties props) {
		super(props);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity living) {
		if (this.isEdible()) {
			ItemStack result = getContainerItem(stack);
			living.eat(worldIn, stack);
			if (!worldIn.isClientSide && Config.SERVER.cocoa.clearsEffects()) {
				// logic basically copied from living entity, so we can choose which effects to remove
				Iterator<MobEffectInstance> itr = living.getActiveEffectsMap().values().iterator();
				boolean hasCinnamon = hasHoney(stack, CINNAMON_TAG);
				while (itr.hasNext()) {
					MobEffectInstance effect = itr.next();
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
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		if (hasHoney(stack, CINNAMON_TAG)) {
			tooltip.add(WITH_CINNAMON);
		}
	}
}
