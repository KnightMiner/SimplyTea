package elucent.simplytea.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class ItemTooltip extends ItemBase {
    public ItemTooltip(String name) {
        super(name, true);
    }

    @Override
    public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(I18n.format(this.getTranslationKey() + ".tooltip"));
    }
}
