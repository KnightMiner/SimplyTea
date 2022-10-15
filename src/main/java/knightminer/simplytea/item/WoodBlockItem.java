package knightminer.simplytea.item;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;

public class WoodBlockItem extends BlockItem {
  public WoodBlockItem(Block block, Properties props) {
    super(block, props);
  }

  @Override
  public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
    return 300;
  }
}
