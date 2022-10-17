package knightminer.simplytea.item;

import net.minecraft.item.ItemStack;

import net.minecraft.item.Item.Properties;

public class TeaStickItem extends TooltipItem {

  public TeaStickItem(Properties props) {
    super(props);
  }

  @Override
  public int getBurnTime(ItemStack itemStack) {
    return 100;
  }
}
