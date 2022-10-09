package knightminer.simplytea.core.config;

import com.mojang.datafixers.util.Pair;
import knightminer.simplytea.core.Registration;
import knightminer.simplytea.data.SimplyTags;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeConfigSpec;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

/** Properties for a tea drink */
public class TeaDrink extends Drink {
  private final TeaEffect type;
  private final ForgeConfigSpec.IntValue configurable;
  private final int constant;

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
      this.configurable = builder.comment(String.format("Level of the %s effect when drinking this tea.", type), type.getDescription(), "Set to 0 to disable the effect.")
                                 .translation("simplytea.config.tea.level")
                                 .defineInRange("level", level, 0, 10);
      this.constant = time;
    } else {
      this.configurable = builder.comment(String.format("Time in seconds for the %s effect from drinking this tea.", type), type.getDescription(), "Set to 0 to disable the effect.")
                                          .translation("simplytea.config.tea.time")
                                          .defineInRange("time", time, 0, 600);
      this.constant = level;
    }
    builder.pop();
  }

  /** Gets the effect for this drink, or null if the effect is disabled */
  @SuppressWarnings("deprecation")
@Nullable
  public MobEffectInstance getEffect(boolean hasHoney) {
    int levelOffset = hasHoney ? 0 : -1; // config stores values as +1 to make it easier to understand
    int configurable = this.configurable.get();
    if (configurable != 0) {
    	MobEffectInstance effect;
      if (type.isLevel()) {
        effect = new MobEffectInstance(type.getEffect(), constant * 20, configurable + levelOffset);
      } else {
        effect = new MobEffectInstance(type.getEffect(), configurable * 20, constant + levelOffset);
      }
      // teas conflict with each other, add other teas as curative items
      List<ItemStack> curativeEffects = effect.getCurativeItems();
      curativeEffects.clear();
      for (Holder<Item> tea : Registry.ITEM.getTagOrEmpty(SimplyTags.Items.EXCLUSIVE_TEAS)) {
        curativeEffects.add(new ItemStack(tea));
      }

      return effect;
    }
    return null;
  }

  @Override
  public List<Pair<MobEffectInstance,Float>> getEffects() {
    return Collections.emptyList();
  }

  /** Tea effect types */
  public enum TeaEffect {
    RESTFUL(true, () -> Registration.restful.get(), "Heals one heart after sleeping per level"),
    RELAXED(true, () -> Registration.relaxed.get(), "Heals 0.5 hearts every (60 / level) seconds"),
    CAFFEINATED(false, () -> Registration.caffeinated.get(), "Grants +6% movement and +5% attack speed"),
    INVIGORATED(false, () -> Registration.invigorated.get(), "Grants +1 attack damage and 0.5 knockback"),
    ENDERFALLING(false, () -> Registration.enderfalling.get(), "Grants immunity to ender pearl damage and reduces fall damage"),
    ABSORPTION(true, () -> MobEffects.ABSORPTION, "Grants 2 temporary absorption hearts per level for a short time");

    private final Supplier<MobEffect> effectSupplier;
    private final boolean level;
    private final String description;

    /** @param level  If true, level is configurable. If false time is configurable */
    TeaEffect(boolean level, Supplier<MobEffect> effectSupplier, String description) {
      this.effectSupplier = effectSupplier;
      this.level = level;
      this.description = description;
    }

    /**
     * If true, the level is configurable. If false time is configurable
     * @return  true if level is configurable, false if time is
     */
    public boolean isLevel() {
      return level;
    }

    /** Gets the description for the config */
    public String getDescription() {
      return description;
    }

    /** Gets the effect for this drink */
    public MobEffect getEffect() {
      return effectSupplier.get();
    }

    @Override
    public String toString() {
      return this.name().toLowerCase(Locale.US);
    }
  }
}
