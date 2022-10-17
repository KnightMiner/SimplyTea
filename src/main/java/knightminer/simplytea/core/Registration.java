package knightminer.simplytea.core;

import knightminer.simplytea.SimplyTea;
import knightminer.simplytea.block.TeaSaplingBlock;
import knightminer.simplytea.block.TeaTrunkBlock;
import knightminer.simplytea.data.AddEntryLootModifier;
import knightminer.simplytea.data.MatchToolTypeLootCondition;
import knightminer.simplytea.data.gen.BlockTagGenerator;
import knightminer.simplytea.data.gen.ItemTagGenerator;
import knightminer.simplytea.data.gen.LootTableGenerator;
import knightminer.simplytea.data.gen.RecipeGenerator;
import knightminer.simplytea.data.gen.ShapelessHoneyRecipe;
import knightminer.simplytea.item.CocoaItem;
import knightminer.simplytea.item.HotTeapotItem;
import knightminer.simplytea.item.TeaCupItem;
import knightminer.simplytea.item.TeaStickItem;
import knightminer.simplytea.item.TeapotItem;
import knightminer.simplytea.item.TooltipItem;
import knightminer.simplytea.item.WoodBlockItem;
import knightminer.simplytea.potion.CaffeinatedEffect;
import knightminer.simplytea.potion.EnderfallingEffect;
import knightminer.simplytea.potion.InvigoratedEffect;
import knightminer.simplytea.potion.RelaxedEffect;
import knightminer.simplytea.potion.RestfulEffect;
import knightminer.simplytea.worldgen.TeaTreeFeature;
import knightminer.simplytea.worldgen.TreeGenEnabledPlacement;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.data.worldgen.Features.Decorators;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.FrequencyWithExtraChanceDecoratorConfiguration;
import net.minecraft.world.level.levelgen.placement.ChanceDecoratorConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;
import java.util.Objects;

@SuppressWarnings("unused")
@ObjectHolder(SimplyTea.MOD_ID)
@EventBusSubscriber(modid = SimplyTea.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class Registration {
  /* Creative tab */
  public static CreativeModeTab group = new CreativeModeTab("simplytea") {
    @Override
    public ItemStack makeIcon() {
      return new ItemStack(tea_leaf);
    }
  };

  /* Potions */
  public static final MobEffect restful = injected();
  public static final MobEffect relaxed = injected();
  public static final MobEffect caffeinated = injected();
  public static final MobEffect invigorated = injected();
  public static final MobEffect enderfalling = injected();

  /* Blocks */
  public static final TeaSaplingBlock tea_sapling = injected();
  public static final Block tea_trunk = injected();
  public static final Block tea_fence = injected();
  public static final Block tea_fence_gate = injected();
  public static final Block potted_tea_sapling = injected();

  /* Items */
  /* Crafting */
  public static final Item tea_leaf = injected();
  public static final Item black_tea = injected();
  public static final Item tea_stick = injected();
  public static final Item ice_cube = injected();
  public static final Item chorus_petal = injected();

  /* Tea bags */
  public static final Item teabag = injected();
  public static final Item teabag_black = injected();
  public static final Item teabag_floral = injected();
  public static final Item teabag_chorus = injected();
  public static final Item teabag_green = injected();

  /* Tea pots */
  public static final Item unfired_teapot = injected();
  public static final Item teapot = injected();
  public static final Item teapot_water = injected();
  public static final Item teapot_milk = injected();
  public static final Item teapot_hot = injected();
  public static final Item teapot_frothed = injected();

  /* Drinks */
  public static final Item unfired_cup = injected();
  public static final Item cup = injected();
  public static final Item cup_tea_black = injected();
  public static final Item cup_tea_green = injected();
  public static final Item cup_tea_floral = injected();
  public static final Item cup_tea_chai = injected();
  public static final Item cup_tea_iced = injected();
  public static final Item cup_tea_chorus = injected();
  public static final Item cup_cocoa = injected();

  /* World Gen */
  public static final FeatureDecorator<NoneDecoratorConfiguration> tree_gen_enabled = injected();
  public static final Feature<NoneFeatureConfiguration> tea_tree = injected();

  public static final RecipeSerializer<?> shapeless_honey = injected();
  public static ConfiguredFeature<?,?> configured_tea_tree;
  public static LootItemConditionType matchToolType;

  @SubscribeEvent
  static void registerEffects(final RegistryEvent.Register<MobEffect> event) {
    IForgeRegistry<MobEffect> r = event.getRegistry();

    register(r, new RestfulEffect(), "restful");
    register(r, new RelaxedEffect(), "relaxed");
    MobEffect caffeinated = register(r, new CaffeinatedEffect(), "caffeinated");
    register(r, new InvigoratedEffect(), "invigorated");
    register(r, new EnderfallingEffect(), "enderfalling");
  }

  @SubscribeEvent
  static void registerBlocks(final RegistryEvent.Register<Block> event) {
    IForgeRegistry<Block> r = event.getRegistry();

    Block.Properties props;

    props = Block.Properties.of(Material.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD);
    register(r, new FenceBlock(props), "tea_fence");
    register(r, new FenceGateBlock(props), "tea_fence_gate");

    props = Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS);
    register(r, new TeaSaplingBlock(props), "tea_sapling");

    props = Block.Properties.of(Material.WOOD, MaterialColor.COLOR_BROWN).strength(2.0F).sound(SoundType.WOOD).randomTicks();
    register(r, new TeaTrunkBlock(props), "tea_trunk");

    props = Block.Properties.of(Material.DECORATION).strength(0f).noOcclusion();
    register(r, new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, () -> tea_sapling, props), "potted_tea_sapling");
    ((FlowerPotBlock)Blocks.FLOWER_POT).addPlant(new ResourceLocation(SimplyTea.MOD_ID, "tea_sapling"), () -> potted_tea_sapling);
  }

  @SubscribeEvent
  static void registerItems(final RegistryEvent.Register<Item> event) {
    IForgeRegistry<Item> r = event.getRegistry();

    Item.Properties props = new Item.Properties().tab(group);

    // crafting
    register(r, new TooltipItem(props), "tea_leaf");
    register(r, new TooltipItem(props), "black_tea");
    register(r, new TeaStickItem(props), "tea_stick");
    register(r, new TooltipItem(props), "ice_cube");
    register(r, new TooltipItem(props), "chorus_petal");

    // tea bags
    register(r, new Item(props), "teabag");
    register(r, new Item(props), "teabag_black");
    register(r, new Item(props), "teabag_floral");
    register(r, new Item(props), "teabag_chorus");
    register(r, new Item(props), "teabag_green");

    // blocks
    registerBlockItem(r, new WoodBlockItem(tea_fence, props));
    registerBlockItem(r, new WoodBlockItem(tea_fence_gate, props));
    registerBlockItem(r, new BlockItem(tea_sapling, props));

    // teapots
    props = new Item.Properties().tab(group).stacksTo(16);
    register(r, new Item(props), "unfired_teapot");
    Item teapot = register(r, new TeapotItem(props), "teapot");
    // teacups
    register(r, new Item(props), "unfired_cup");
    Item cup = register(r, new Item(props), "cup");

    // filled teapots
    props.craftRemainder(teapot).stacksTo(1);
    register(r, new TooltipItem(props), "teapot_water");
    register(r, new TooltipItem(props), "teapot_milk");
    props.setNoRepair().durability(4);
    register(r, new HotTeapotItem(props), "teapot_hot");
    register(r, new HotTeapotItem(props), "teapot_frothed");

    // drinks
    props = new Item.Properties().tab(group).stacksTo(1).durability(2).setNoRepair().craftRemainder(cup);
    register(r, new TeaCupItem(props.food(Config.SERVER.black_tea)), "cup_tea_black");
    register(r, new TeaCupItem(props.food(Config.SERVER.green_tea)), "cup_tea_green");
    register(r, new TeaCupItem(props.food(Config.SERVER.floral_tea)), "cup_tea_floral");
    register(r, new TeaCupItem(props.food(Config.SERVER.chai_tea)), "cup_tea_chai");
    register(r, new TeaCupItem(props.food(Config.SERVER.iced_tea)), "cup_tea_iced");
    register(r, new TeaCupItem(props.food(Config.SERVER.chorus_tea)), "cup_tea_chorus");
    register(r, new CocoaItem(props.food(Config.SERVER.cocoa)), "cup_cocoa");
  }

  @SubscribeEvent
  static void registerPlacement(final RegistryEvent.Register<FeatureDecorator<?>> event) {
    IForgeRegistry<FeatureDecorator<?>> r = event.getRegistry();

    register(r, new TreeGenEnabledPlacement(), "tree_gen_enabled");
  }

  @SubscribeEvent
  static void registerFeatures(final RegistryEvent.Register<Feature<?>> event) {
    IForgeRegistry<Feature<?>> r = event.getRegistry();

    register(r, new TeaTreeFeature(), "tea_tree");
  }

  @SubscribeEvent
  static void registerGlobalLootTables(final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
    IForgeRegistry<GlobalLootModifierSerializer<?>> r = event.getRegistry();

    register(r, new AddEntryLootModifier.Serializer(), "add_loot_entry");
  }

  @SubscribeEvent
  static void registerRecipeSerializers(final RegistryEvent.Register<RecipeSerializer<?>> event) {
    IForgeRegistry<RecipeSerializer<?>> r = event.getRegistry();

    register(r, new ShapelessHoneyRecipe.Serializer(), "shapeless_honey");
    matchToolType = Registry.register(Registry.LOOT_CONDITION_TYPE, MatchToolTypeLootCondition.ID, new LootItemConditionType(MatchToolTypeLootCondition.SERIALIZER));
  }

  @SubscribeEvent
  static void registerMisc(final FMLCommonSetupEvent event) {
    event.enqueueWork(() -> {
      // flamability
      if (Blocks.FIRE instanceof FireBlock) {
        FireBlock fire = (FireBlock)Blocks.FIRE;
        fire.setFlammable(tea_fence, 5, 20);
        fire.setFlammable(tea_fence_gate, 5, 20);
        fire.setFlammable(tea_trunk, 15, 30);
      }

      ComposterBlock.add(0.3f, tea_leaf);
      ComposterBlock.add(0.4f, black_tea);
      ComposterBlock.add(0.5f, chorus_petal);
      ComposterBlock.add(0.3f, tea_sapling);

      // too much caffiene to sleep
      RestfulEffect.addConflict(caffeinated);
      RestfulEffect.addConflict(invigorated);
    });

    // configured features
    configured_tea_tree = tea_tree.configured(FeatureConfiguration.NONE)
                                  .decorated(Registration.tree_gen_enabled.configured(NoneDecoratorConfiguration.INSTANCE))
                                  .decorated(FeatureDecorator.CHANCE.configured(new ChanceDecoratorConfiguration(50)))
                                  .decorated(Decorators.HEIGHTMAP_SQUARE)
                                  .decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(2, 0.1F, 1)));
    Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(SimplyTea.MOD_ID, "tea_tree"), configured_tea_tree);
  }

  @SubscribeEvent
  static void gatherData(GatherDataEvent event) {
    if (event.includeServer()) {
      ExistingFileHelper existing = event.getExistingFileHelper();
      DataGenerator generator = event.getGenerator();
      BlockTagGenerator blockTags = new BlockTagGenerator(generator, existing);
      generator.addProvider(blockTags);
      generator.addProvider(new ItemTagGenerator(generator, blockTags, existing));
      generator.addProvider(new RecipeGenerator(generator));
      generator.addProvider(new LootTableGenerator(generator));
    }
  }


  /* Helper methods */

  /**
   * Registers a forge registry object at the given resource location
   * @param <V>  Value class, extends registry class
   * @param <R>  Registry class
   * @param registry  Forge registry
   * @param value     Value to register
   * @param location  Resource location
   */
  private static <V extends R, R extends IForgeRegistryEntry<R>> void register(IForgeRegistry<R> registry, V value, ResourceLocation location) {
    value.setRegistryName(location);
    registry.register(value);
  }

  /**
   * Registers a forge registry object using the given name and a domain of "simplytea"
   * @param registry  Forge registry
   * @param value     Value to register
   * @param name      Registration name
   * @param <V>  Value class, extends registry class
   * @param <R>  Registry class
   * @return  Registered value
   */
  private static <V extends R, R extends IForgeRegistryEntry<R>> V register(IForgeRegistry<R> registry, V value, String name) {
    value.setRegistryName(new ResourceLocation(SimplyTea.MOD_ID, name));
    registry.register(value);
    return value;
  }

  /**
   * Registers a block item
   * @param registry  Item registry
   * @param item      Item to register, registry name will be set automatically from the internal block
   */
  private static void registerBlockItem(IForgeRegistry<Item> registry, BlockItem item) {
    register(registry, item, Objects.requireNonNull(item.getBlock().getRegistryName()));
  }

  /** Helper function to fix IDEA warnings on injected fields */
  @SuppressWarnings("ConstantConditions")
  @Nonnull
  private static <T> T injected() {
    return null;
  }
}
