package knightminer.simplytea.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;

public class TeaStickItem extends TooltipItem {

  public TeaStickItem(Properties props) {
    super(props);
  }

  @Override
  public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
    return 100;
  }
}
