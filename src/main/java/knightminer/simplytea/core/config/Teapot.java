package knightminer.simplytea.core.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.fluids.FluidType;

public class Teapot {
  private BooleanValue infinite_water;
  private BooleanValue fill_from_cauldron;
  private BooleanValue milk_cow;
  private IntValue teapot_capacity;
  public Teapot(ForgeConfigSpec.Builder builder) {
    builder.comment("Options related to filling the teapot").push("teapot");
    infinite_water = builder.comment("If true, the teapot will not consume water source blocks when filling. It will still consume water from tank and cauldrons.")
                            .translation("simplytea.config.teapot.infinite_water")
                            .define("infinite_water", false);
    fill_from_cauldron = builder.comment("If true, the teapot can be filled with water from a cauldron")
                                .translation("simplytea.config.teapot.fill_from_cauldron")
                                .define("fill_from_cauldron", true);
    milk_cow = builder.comment("If true, cows can be milked using a teapot to fill it with milk")
                      .translation("simplytea.config.teapot.milk_cow")
                      .define("milk_cow", true);
    teapot_capacity = builder.comment("Amount of fluid consumed when filling a teapot from a tank")
                             .translation("simplytea.config.teapot.teapot_capacity")
                             .defineInRange("teapot_capacity", FluidType.BUCKET_VOLUME, 1, FluidType.BUCKET_VOLUME * 256);
    builder.pop();
  }

  /** True if teapots can fill without removing the source */
  public boolean infiniteWater() {
    return infinite_water.get();
  }

  /** True if teapots can be filled from a cauldron */
  public boolean fillFromCauldron() {
    return fill_from_cauldron.get();
  }

  /** True if teapots can be used to milk cows */
  public boolean canMilkCows() {
    return milk_cow.get();
  }

  /** Amount of fluid a teapot can contain */
  public int teapotCapacity() {
    return teapot_capacity.get();
  }
}
