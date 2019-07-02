package knightminer.simplytea.item;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

public class WoodBlockItem extends BlockItem {
  public WoodBlockItem(Block block, Properties props) {
    super(block, props);
  }

  @Override
  public int getBurnTime(ItemStack stack) {
    return 300;
  }
}
