package knightminer.simplytea.item;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

import net.minecraft.world.item.Item.Properties;

public class WoodBlockItem extends BlockItem {
  public WoodBlockItem(Block block, Properties props) {
    super(block, props);
  }

  @Override
  public int getBurnTime(ItemStack stack) {
    return 300;
  }
}
