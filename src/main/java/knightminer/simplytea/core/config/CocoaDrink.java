package knightminer.simplytea.core.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CocoaDrink extends Drink {
  private ForgeConfigSpec.BooleanValue clear_effects;

  /**
   * Creates a new tea settings category for the given tea
   * @param builder        Config builder
   * @param hunger         Default hunger value
   * @param saturation     Default saturation value
   */
  public CocoaDrink(ForgeConfigSpec.Builder builder, int hunger, double saturation) {
    super("cocoa", builder, hunger, saturation);
    this.clear_effects = builder.comment("If true, drinking cocoa clears status effects like milk")
                                .translation("simplytea.config.tea.clear_effects")
                                .define("clear_effects", true);
    builder.pop();
  }

  /**
   * If true, drinking cocoa clears effects
   * @return  true if cocoa clears effects
   */
  public boolean clearsEffects() {
    return clear_effects.get();
  }
}
