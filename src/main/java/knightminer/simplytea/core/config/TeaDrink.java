package knightminer.simplytea.core.config;

import com.mojang.datafixers.util.Pair;
import knightminer.simplytea.core.Registration;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.common.ForgeConfigSpec;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Properties for a tea drink */
public class TeaDrink extends Drink {
  private final TeaEffect type;
  private final ForgeConfigSpec.IntValue configurable;
  private final int constant;
  private List<Pair<EffectInstance, Float>> effects = null;

  /**
   * Creates a new tea settings category for the given tea
   * @param builder     Config builder
   * @param name        Name of the tea
   * @param type        Tea effect type
   * @param hunger      Default hunger value
   * @param saturation  Default saturation value
   * @param time        Default effect time in seconds
   * @param level       Default effect level
   */
  public TeaDrink(String name, ForgeConfigSpec.Builder builder, TeaEffect type, int hunger, double saturation, int time, int level) {
    super(name, builder, hunger, saturation);

    // determine last property based on type
    this.type = type;
    if (type.isLevel()) {
      this.configurable = builder.comment(String.format("Level of the %s effect when drinking this tea.", type), "Set to 0 to disable the effect.")
                                 .translation("simplytea.config.tea.level")
                                 .defineInRange("level", level, 0, 10);
      this.constant = time;
    } else {
      this.configurable = builder.comment(String.format("Time in seconds for the %s effect from drinking this tea.", type), "Set to 0 to disable the effect.")
                                          .translation("simplytea.config.tea.time")
                                          .defineInRange("time", time, 0, 600);
      this.constant = level;
    }
    builder.pop();
  }

  @Nullable
  private EffectInstance getEffect() {
    int configurable = this.configurable.get();
    if (configurable != 0) {
      switch(type) {
        case CAFFEINE:
          return new EffectInstance(Registration.caffeinated, configurable * 20, constant - 1);
        case HERBAL:
          return new EffectInstance(Registration.restful, constant*20, configurable-1);
        case ENDERFALLING:
          return new EffectInstance(Registration.enderfalling, configurable*20, constant-1);
      }
    }
    return null;
  }

  @Override
  public List<Pair<EffectInstance,Float>> getEffects() {
    if (effects != null) {
      return effects;
    }

    // create effect list
    effects = new ArrayList<>();
    EffectInstance effect = getEffect();
    if (effect != null) {
      effects.add(Pair.of(effect, 1.0f));
    }
    return effects;
  }

  /** Invalidates the effect cache. Called on config reload to update the effect */
  public void invalidEffects() {
    this.effects = null;
  }

  /** Tea effect types */
  public enum TeaEffect {
    CAFFEINE(false),
    HERBAL(true),
    ENDERFALLING(false);

    private final boolean level;

    /** @param level  If true, level is configurable. If false time is configurable */
    TeaEffect(boolean level) {
      this.level = level;
    }

    /**
     * If true, the level is configurable. If false time is configurable
     * @return  true if level is configurable, false if time is
     */
    public boolean isLevel() {
      return level;
    }

    @Override
    public String toString() {
      return this.name().toLowerCase(Locale.US);
    }
  }
}
