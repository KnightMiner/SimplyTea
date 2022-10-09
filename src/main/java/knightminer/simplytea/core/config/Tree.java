package knightminer.simplytea.core.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;

public class Tree {
  private DoubleValue regrow_chance;
  public Tree(ForgeConfigSpec.Builder builder) {
    builder.comment("Options related to tea trees").push("tree");
    regrow_chance = builder.comment("Chance of leaves to regrow every random tick.")
                      .translation("config.simplytea.tree.regrow_chance")
                      .defineInRange("regrow_chance", 0.05, 0.0, 1.0);
    builder.pop();
  }

  /** Chance between 0 and 1 of a tree leaf to regrow every random tick */
  public double regrowthChance() {
    return regrow_chance.get();
  }
}
