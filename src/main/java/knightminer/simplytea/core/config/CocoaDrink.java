package knightminer.simplytea.core.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CocoaDrink extends Drink {
  private final ForgeConfigSpec.EnumValue<ClearType> clear_effects;

  /**
   * Creates a new tea settings category for the given tea
   * @param builder        Config builder
   * @param hunger         Default hunger value
   * @param saturation     Default saturation value
   */
  public CocoaDrink(String name, ForgeConfigSpec.Builder builder, ClearType defaultClearEffects, int hunger, double saturation) {
    super(name, builder, hunger, saturation);
    this.clear_effects = builder.comment("If ALL, drinking clears status effects like milk", "If NEGATIVE, clears only negative effects (default)", "If NONE, does not clear effects", "For all values, regular cocoa or frothed milk clears just the first effect, while cocoa with cinnamon clears all")
                                .translation("simplytea.config.tea.clear_effects")
                                .defineEnum("clear_effects", defaultClearEffects);
    builder.pop();
  }

  /**
   * If true, drinking cocoa clears effects
   * @return  true if cocoa clears effects
   */
  public boolean clearsEffects() {
    return clear_effects.get() != ClearType.NONE;
  }

  /**
   * If true, drinking cocoa clears positive effects
   * @return  true if cocoa clears positive effects
   */
  public boolean clearsPositive() {
    return clear_effects.get() == ClearType.ALL;
  }

  /** Clear options */
  public enum ClearType {
    ALL,
    NEGATIVE,
    NONE
  }
}
