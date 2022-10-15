package knightminer.simplytea.item;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

public class TeaStickItem extends TooltipItem {

  public TeaStickItem(Properties props) {
    super(props);
  }

  @Override
  public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
    return 100;
  }
}
