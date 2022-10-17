package knightminer.simplytea.item;

import net.minecraft.world.item.ItemStack;

import net.minecraft.world.item.Item.Properties;

public class TeaStickItem extends TooltipItem {

  public TeaStickItem(Properties props) {
    super(props);
  }

  @Override
  public int getBurnTime(ItemStack itemStack) {
    return 100;
  }
}
