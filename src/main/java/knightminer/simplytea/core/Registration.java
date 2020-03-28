package knightminer.simplytea.core;

import knightminer.simplytea.SimplyTea;
import knightminer.simplytea.block.TeaSaplingBlock;
import knightminer.simplytea.block.TeaTrunkBlock;
import knightminer.simplytea.item.CocoaItem;
import knightminer.simplytea.item.HotTeapotItem;
import knightminer.simplytea.item.TeaCupItem;
import knightminer.simplytea.item.TeaStickItem;
import knightminer.simplytea.item.TeapotItem;
import knightminer.simplytea.item.TooltipItem;
import knightminer.simplytea.item.WoodBlockItem;
import knightminer.simplytea.potion.CaffeinatedEffect;
import knightminer.simplytea.potion.EnderfallingEffect;
import knightminer.simplytea.potion.RestfulEffect;
import knightminer.simplytea.worldgen.TeaTreeFeature;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ComposterBlock;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.FireBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;

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
  public static final Effect restful = null;
  public static final Effect caffeinated = null;
  public static final Effect enderfalling = null;

  /* Blocks */
  public static final TeaSaplingBlock tea_sapling = null;
  public static final Block tea_trunk = null;
  public static final Block tea_fence = null;
  public static final Block tea_fence_gate = null;
  public static final Block potted_tea_sapling = null;

  /* Items */
  /* Crafting */
  public static final Item tea_leaf = null;
  public static final Item black_tea = null;
  public static final Item tea_stick = null;
  public static final Item chorus_petal = null;

  /* Tea bags */
  public static final Item teabag = null;
  public static final Item teabag_black = null;
  public static final Item teabag_floral = null;
  public static final Item teabag_chorus = null;
  public static final Item teabag_green = null;

  /* Tea bags */
  public static final Item teapot = null;
  public static final Item teapot_water = null;
  public static final Item teapot_milk = null;

  /* Drinks */
  public static final Item cup = null;
  public static final Item cup_tea_black = null;
  public static final Item cup_tea_green = null;
  public static final Item cup_tea_floral = null;
  public static final Item cup_tea_chai = null;
  public static final Item cup_tea_chorus = null;
  public static final Item cup_cocoa = null;

  /* World Gen */
  public static final Feature<NoFeatureConfig> tea_tree = null;

  @SubscribeEvent
  public static void registerEffects(final RegistryEvent.Register<Effect> event) {
    IForgeRegistry<Effect> r = event.getRegistry();

    register(r, new CaffeinatedEffect(), "caffeinated");
    register(r, new RestfulEffect(), "restful");
    register(r, new EnderfallingEffect(), "enderfalling");
  }


  @SubscribeEvent
  public static void registerBlocks(final RegistryEvent.Register<Block> event) {
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
  public static void registerItems(final RegistryEvent.Register<Item> event) {
    IForgeRegistry<Item> r = event.getRegistry();

    Item.Properties props = new Item.Properties().group(group);

    // crafting
    register(r, new TooltipItem(props), "tea_leaf");
    register(r, new TooltipItem(props), "black_tea");
    register(r, new TeaStickItem(props), "tea_stick");
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
    Item teapot = register(r, new TeapotItem(props), "teapot");
    props.containerItem(teapot).maxStackSize(1);
    register(r, new TooltipItem(props), "teapot_water");
    register(r, new TooltipItem(props), "teapot_milk");
    props.setNoRepair().maxDamage(4);
    register(r, new HotTeapotItem(props), "teapot_hot");
    register(r, new HotTeapotItem(props), "teapot_frothed");

    // drinks
    props = new Item.Properties().group(group).maxStackSize(16);
    Item cup = register(r, new Item(props), "cup");
    props = new Item.Properties().group(group).maxStackSize(1).maxDamage(2).setNoRepair().containerItem(cup);
    register(r, new TeaCupItem(props.food(Config.SERVER.black_tea)), "cup_tea_black");
    register(r, new TeaCupItem(props.food(Config.SERVER.green_tea)), "cup_tea_green");
    register(r, new TeaCupItem(props.food(Config.SERVER.floral_tea)), "cup_tea_floral");
    register(r, new TeaCupItem(props.food(Config.SERVER.chai_tea)), "cup_tea_chai");
    register(r, new TeaCupItem(props.food(Config.SERVER.chorus_tea)), "cup_tea_chorus");
    register(r, new CocoaItem(props.food(Config.SERVER.cocoa)), "cup_cocoa");
  }

  @SubscribeEvent
  public static void registerFeatures(final RegistryEvent.Register<Feature<?>> event) {
    IForgeRegistry<Feature<?>> r = event.getRegistry();

    register(r, new TeaTreeFeature(NoFeatureConfig::deserialize), "tea_tree");
  }

  @SubscribeEvent
  public static void registerMisc(final FMLCommonSetupEvent event) {
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
  }

  @SubscribeEvent
  public static void registerFeatures(final InterModProcessEvent event) {
    BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST).forEach((biome) -> {
      biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION,
                       tea_tree.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG)
                               .withPlacement(Placement.DARK_OAK_TREE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
    });
  }

  /* Helper methods */

  /**
   * Registers a forge registry object at the given resource location
   * @param registry  Forge registry
   * @param value     Value to register
   * @param location  Resource location
   * @param <V>  Value class, extends registry class
   * @param <R>  Registry class
   * @return  Registered value
   */
  private static <V extends R, R extends IForgeRegistryEntry<R>> V register(IForgeRegistry<R> registry, V value, ResourceLocation location) {
    value.setRegistryName(location);
    registry.register(value);
    return value;
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
   * @return  Registered block item
   */
  private static BlockItem registerBlockItem(IForgeRegistry<Item> registry, BlockItem item) {
    return register(registry, item, item.getBlock().getRegistryName());
  }
}
