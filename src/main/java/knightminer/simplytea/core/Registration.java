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
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ComposterBlock;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.FireBlock;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootConditionType;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.Features.Placements;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
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
  public static ItemGroup group = new ItemGroup("simplytea") {
    @Override
    public ItemStack createIcon() {
      return new ItemStack(tea_leaf);
    }
  };

  /* Potions */
  public static final Effect restful = injected();
  public static final Effect relaxed = injected();
  public static final Effect caffeinated = injected();
  public static final Effect invigorated = injected();
  public static final Effect enderfalling = injected();

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
  public static final Placement<NoPlacementConfig> tree_gen_enabled = injected();
  public static final Feature<NoFeatureConfig> tea_tree = injected();

  public static ConfiguredFeature<?,?> configured_tea_tree;
  public static LootConditionType matchToolType;

  @SubscribeEvent
  static void registerEffects(final RegistryEvent.Register<Effect> event) {
    IForgeRegistry<Effect> r = event.getRegistry();

    register(r, new RestfulEffect(), "restful");
    register(r, new RelaxedEffect(), "relaxed");
    register(r, new CaffeinatedEffect(), "caffeinated");
    register(r, new InvigoratedEffect(), "invigorated");
    register(r, new EnderfallingEffect(), "enderfalling");
  }

  @SubscribeEvent
  static void registerBlocks(final RegistryEvent.Register<Block> event) {
    IForgeRegistry<Block> r = event.getRegistry();

    Block.Properties props;

    props = Block.Properties.create(Material.WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD);
    register(r, new FenceBlock(props), "tea_fence");
    register(r, new FenceGateBlock(props), "tea_fence_gate");

    props = Block.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0).sound(SoundType.PLANT);
    register(r, new TeaSaplingBlock(props), "tea_sapling");

    props = Block.Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(2.0F).sound(SoundType.WOOD).tickRandomly();
    register(r, new TeaTrunkBlock(props), "tea_trunk");

    props = Block.Properties.create(Material.MISCELLANEOUS).hardnessAndResistance(0f).notSolid();
    register(r, new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, () -> tea_sapling, props), "potted_tea_sapling");
    ((FlowerPotBlock)Blocks.FLOWER_POT).addPlant(new ResourceLocation(SimplyTea.MOD_ID, "tea_sapling"), () -> potted_tea_sapling);
  }

  @SubscribeEvent
  static void registerItems(final RegistryEvent.Register<Item> event) {
    IForgeRegistry<Item> r = event.getRegistry();

    Item.Properties props = new Item.Properties().group(group);

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
    props = new Item.Properties().group(group).maxStackSize(16);
    register(r, new Item(props), "unfired_teapot");
    Item teapot = register(r, new TeapotItem(props), "teapot");
    // teacups
    register(r, new Item(props), "unfired_cup");
    Item cup = register(r, new Item(props), "cup");

    // filled teapots
    props.containerItem(teapot).maxStackSize(1);
    register(r, new TooltipItem(props), "teapot_water");
    register(r, new TooltipItem(props), "teapot_milk");
    props.setNoRepair().maxDamage(4);
    register(r, new HotTeapotItem(props), "teapot_hot");
    register(r, new HotTeapotItem(props), "teapot_frothed");

    // drinks
    props = new Item.Properties().group(group).maxStackSize(1).maxDamage(2).setNoRepair().containerItem(cup);
    register(r, new TeaCupItem(props.food(Config.SERVER.black_tea)), "cup_tea_black");
    register(r, new TeaCupItem(props.food(Config.SERVER.green_tea)), "cup_tea_green");
    register(r, new TeaCupItem(props.food(Config.SERVER.floral_tea)), "cup_tea_floral");
    register(r, new TeaCupItem(props.food(Config.SERVER.chai_tea)), "cup_tea_chai");
    register(r, new TeaCupItem(props.food(Config.SERVER.iced_tea)), "cup_tea_iced");
    register(r, new TeaCupItem(props.food(Config.SERVER.chorus_tea)), "cup_tea_chorus");
    register(r, new CocoaItem(props.food(Config.SERVER.cocoa)), "cup_cocoa");
  }

  @SubscribeEvent
  static void registerPlacement(final RegistryEvent.Register<Placement<?>> event) {
    IForgeRegistry<Placement<?>> r = event.getRegistry();

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
  static void registerRecipeSerializers(final RegistryEvent.Register<IRecipeSerializer<?>> event) {
    matchToolType = Registry.register(Registry.LOOT_CONDITION_TYPE, MatchToolTypeLootCondition.ID, new LootConditionType(MatchToolTypeLootCondition.SERIALIZER));
  }

  @SubscribeEvent
  static void registerMisc(final FMLCommonSetupEvent event) {
    event.enqueueWork(() -> {
      // flamability
      if (Blocks.FIRE instanceof FireBlock) {
        FireBlock fire = (FireBlock)Blocks.FIRE;
        fire.setFireInfo(tea_fence, 5, 20);
        fire.setFireInfo(tea_fence_gate, 5, 20);
        fire.setFireInfo(tea_trunk, 15, 30);
      }

      ComposterBlock.registerCompostable(0.3f, tea_leaf);
      ComposterBlock.registerCompostable(0.4f, black_tea);
      ComposterBlock.registerCompostable(0.5f, chorus_petal);
      ComposterBlock.registerCompostable(0.3f, tea_sapling);
    });

    // configured features
    configured_tea_tree = tea_tree.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG)
                                  .withPlacement(Registration.tree_gen_enabled.configure(NoPlacementConfig.INSTANCE))
                                  .withPlacement(Placement.CHANCE.configure(new ChanceConfig(50)))
                                  .withPlacement(Placements.HEIGHTMAP_PLACEMENT)
                                  .withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(2, 0.1F, 1)));
    Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(SimplyTea.MOD_ID, "tea_tree"), configured_tea_tree);
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
