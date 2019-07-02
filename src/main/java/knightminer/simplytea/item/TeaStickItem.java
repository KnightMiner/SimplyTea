package knightminer.simplytea.item;

import net.minecraft.item.ItemStack;

public class TeaStickItem extends TooltipItem {

  public TeaStickItem(Properties props) {
    super(props);
  }

  @Override
  public int getBurnTime(ItemStack itemStack) {
    return 100;
  }
}
