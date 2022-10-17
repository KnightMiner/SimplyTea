package knightminer.simplytea.core.config;

import net.minecraft.item.Food;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Collections;

/** Extension of vanilla food to allow linking stats to config */
public class Drink extends Food {
  private ForgeConfigSpec.IntValue hunger;
  private ForgeConfigSpec.DoubleValue saturation;

  /**
   * Creates a new tea settings category for the given drink.
   * Note this does not call pop on the builder category, the class extending this is expected to call that in the constructor
   * @param builder     Config builder
   * @param name        Name of the drink
   * @param hunger      Default hunger value
   * @param saturation  Default saturation value
   */
  protected Drink(String name, ForgeConfigSpec.Builder builder, int hunger, double saturation) {
    super(0, 0f, false, true, true, Collections.emptyList());
    builder.comment(String.format("Stats for %s", name)).push(name);
    this.hunger = builder.comment("Hunger restored when drinking this drink.")
                         .translation("simplytea.config.tea.hunger")
                         .defineInRange("hunger", hunger, 0, 20);
    this.saturation = builder.comment("Saturation restored when drinking this drink.")
                             .translation("simplytea.config.tea.saturation")
                             .defineInRange("saturation", saturation, 0D, 10D);
  }

  @Override
  public int getNutrition() {
    return hunger.get();
  }

  @Override
  public float getSaturationModifier() {
    return saturation.get().floatValue();
  }
}
