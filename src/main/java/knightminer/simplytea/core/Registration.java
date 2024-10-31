package knightminer.simplytea.core;

import knightminer.simplytea.SimplyTea;
import knightminer.simplytea.block.TeaTrunkBlock;
import knightminer.simplytea.data.AddEntryLootModifier;
import knightminer.simplytea.data.gen.BlockTagGenerator;
import knightminer.simplytea.data.gen.ItemTagGenerator;
import knightminer.simplytea.data.gen.LootTableGenerator;
import knightminer.simplytea.data.gen.RecipeGenerator;
import knightminer.simplytea.data.gen.ShapelessHoneyRecipe;
import knightminer.simplytea.data.gen.WorldgenGenerator;
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
import knightminer.simplytea.worldgen.TeaTreeGrower;
import knightminer.simplytea.worldgen.TreeGenEnabledPlacement;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegisterEvent.RegisterHelper;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = SimplyTea.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class Registration {
  /* Creative tab */
  public static CreativeModeTab group;

  /* Potions */
  public static MobEffect restful;
  public static MobEffect relaxed;
  public static MobEffect caffeinated;
  public static MobEffect invigorated;
  public static MobEffect enderfalling;

  /* Blocks */
  public static SaplingBlock tea_sapling;
  public static Block tea_trunk;
  public static Block tea_fence;
  public static Block tea_fence_gate;
  public static Block potted_tea_sapling;

  /* Items */
  /* Crafting */
  public static Item tea_leaf;
  public static Item black_tea;
  public static Item tea_stick;
  public static Item ice_cube;
  public static Item chorus_petal;

  /* Tea bags */
  public static Item teabag;
  public static Item teabag_black;
  public static Item teabag_floral;
  public static Item teabag_chorus;
  public static Item teabag_green;

  /* Tea pots */
  public static Item unfired_teapot;
  public static Item teapot;
  public static Item teapot_water;
  public static Item teapot_milk;
  public static Item teapot_hot;
  public static Item teapot_frothed;

  /* Drinks */
  public static Item unfired_cup;
  public static Item cup;
  public static Item cup_tea_black;
  public static Item cup_tea_green;
  public static Item cup_tea_floral;
  public static Item cup_tea_chai;
  public static Item cup_tea_iced;
  public static Item cup_tea_chorus;
  public static Item cup_cocoa;

  /* World Gen */
  public static PlacementModifierType<TreeGenEnabledPlacement> tree_gen_enabled;
  public static Feature<NoneFeatureConfiguration> tea_tree;

  public static RecipeSerializer<?> shapeless_honey;
  public static ResourceKey<ConfiguredFeature<?,?>> configured_tea_tree = FeatureUtils.createKey(SimplyTea.MOD_ID + ":tea_tree");
  public static ResourceKey<PlacedFeature> placed_tea_tree = PlacementUtils.createKey(SimplyTea.MOD_ID + ":tea_tree");
  public static ResourceKey<BiomeModifier> tea_tree_biome_modifier = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(SimplyTea.MOD_ID, "tea_tree"));

  @SubscribeEvent
  static void registerObjects(final RegisterEvent event) {    
    event.register(ForgeRegistries.Keys.MOB_EFFECTS, r -> {
      restful = register(r, new RestfulEffect(), "restful");
      relaxed = register(r, new RelaxedEffect(), "relaxed");
      caffeinated = register(r, new CaffeinatedEffect(), "caffeinated");
      invigorated = register(r, new InvigoratedEffect(), "invigorated");
      enderfalling = register(r, new EnderfallingEffect(), "enderfalling");
    });
    
    event.register(ForgeRegistries.Keys.BLOCKS, r -> {
	  Block.Properties props;

	  props = Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD);
	  tea_fence = register(r, new FenceBlock(props), "tea_fence");
	  tea_fence_gate = register(r, new FenceGateBlock(props, WoodType.OAK), "tea_fence_gate");

	  props = Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS);
	  tea_sapling = register(r, new SaplingBlock(new TeaTreeGrower(), props), "tea_sapling");

	  props = Block.Properties.of().mapColor(MapColor.COLOR_BROWN).ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.COLOR_BROWN).strength(2.0F).sound(SoundType.WOOD).randomTicks();
	  tea_trunk = register(r, new TeaTrunkBlock(props), "tea_trunk");

	  props = Block.Properties.of().pushReaction(PushReaction.DESTROY).strength(0f).noOcclusion();
	  potted_tea_sapling = register(r, new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, () -> tea_sapling, props), "potted_tea_sapling");
	  ((FlowerPotBlock)Blocks.FLOWER_POT).addPlant(new ResourceLocation(SimplyTea.MOD_ID, "tea_sapling"), () -> potted_tea_sapling);
	});
    
    event.register(ForgeRegistries.Keys.ITEMS, r -> {
      Item.Properties props = new Item.Properties();

      // crafting
      tea_leaf = register(r, new TooltipItem(props), "tea_leaf");
      black_tea = register(r, new TooltipItem(props), "black_tea");
      tea_stick = register(r, new TeaStickItem(props), "tea_stick");
      ice_cube = register(r, new TooltipItem(props), "ice_cube");
      chorus_petal = register(r, new TooltipItem(props), "chorus_petal");

      // tea bags
      teabag = register(r, new Item(props), "teabag");
      teabag_black = register(r, new Item(props), "teabag_black");
      teabag_floral = register(r, new Item(props), "teabag_floral");
      teabag_chorus = register(r, new Item(props), "teabag_chorus");
      teabag_green = register(r, new Item(props), "teabag_green");

      // blocks
      registerBlockItem(r, new WoodBlockItem(tea_fence, props));
      registerBlockItem(r, new WoodBlockItem(tea_fence_gate, props));
      registerBlockItem(r, new BlockItem(tea_sapling, props));

      // teapots
      props = new Item.Properties().stacksTo(16);
      unfired_teapot = register(r, new Item(props), "unfired_teapot");
      teapot = register(r, new TeapotItem(props), "teapot");
      // teacups
      unfired_cup = register(r, new Item(props), "unfired_cup");
      cup = register(r, new Item(props), "cup");

      // filled teapots
      props.craftRemainder(teapot).stacksTo(1);
      teapot_water = register(r, new TooltipItem(props), "teapot_water");
      teapot_milk = register(r, new TooltipItem(props), "teapot_milk");
      props.setNoRepair().durability(4);
      teapot_hot = register(r, new HotTeapotItem(props), "teapot_hot");
      teapot_frothed = register(r, new HotTeapotItem(props), "teapot_frothed");

      // drinks
      props = new Item.Properties().stacksTo(1).durability(2).setNoRepair().craftRemainder(cup);
      cup_tea_black = register(r, new TeaCupItem(props.food(Config.SERVER.black_tea)), "cup_tea_black");
      cup_tea_green = register(r, new TeaCupItem(props.food(Config.SERVER.green_tea)), "cup_tea_green");
      cup_tea_floral = register(r, new TeaCupItem(props.food(Config.SERVER.floral_tea)), "cup_tea_floral");
      cup_tea_chai = register(r, new TeaCupItem(props.food(Config.SERVER.chai_tea)), "cup_tea_chai");
      cup_tea_iced = register(r, new TeaCupItem(props.food(Config.SERVER.iced_tea)), "cup_tea_iced");
      cup_tea_chorus = register(r, new TeaCupItem(props.food(Config.SERVER.chorus_tea)), "cup_tea_chorus");
      cup_cocoa = register(r, new CocoaItem(props.food(Config.SERVER.cocoa)), "cup_cocoa");
    });

    event.register(Registries.CREATIVE_MODE_TAB, r -> {
      group = register(r, CreativeModeTab.builder()
              .icon(() -> new ItemStack(tea_leaf))
              .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
              .title(Component.translatable("itemGroup.simplytea"))
              .displayItems((displayParameters, output) -> {
                ForgeRegistries.ITEMS.getValues().stream()
                        .filter(item -> ForgeRegistries.ITEMS.getKey(item).getNamespace().equals(SimplyTea.MOD_ID))
                        .forEach(output::accept);
              }).build(), "simplytea");
    });
    
    event.register(ForgeRegistries.Keys.FEATURES, r -> {
	  tea_tree = register(r, new TeaTreeFeature(), "tea_tree");
	});
    
    event.register(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, r -> {
	  register(r, AddEntryLootModifier.CODEC, "add_loot_entry");
	});
    
    event.register(ForgeRegistries.Keys.RECIPE_SERIALIZERS, r -> {
	  shapeless_honey = register(r, new ShapelessHoneyRecipe.Serializer(), "shapeless_honey");
	});

    event.register(Registries.PLACEMENT_MODIFIER_TYPE, r -> {
      tree_gen_enabled = register(r, () -> TreeGenEnabledPlacement.CODEC, "tree_gen_enabled");
    });
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

      // Lowers the cauldron by a variable amound depending on configured teapot size
      // Based on GLASS_BOTTLE interaction and LayeredCauldronBlock.lowerFillLevel()
      CauldronInteraction.WATER.put(teapot, (state, level, pos, player, hand, stack) -> {
        // always consume at least one cauldron level, even for tiny teapots
        int teapotLevels = Math.max(1, Math.round(Config.SERVER.teapot.teapotCapacity() / 333.0f));
        if (Config.SERVER.teapot.fillFromCauldron() && state.getValue(LayeredCauldronBlock.LEVEL) >= teapotLevels && !level.isClientSide()) {
          player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(teapot_water)));
          player.awardStat(Stats.USE_CAULDRON);
          player.awardStat(Stats.ITEM_USED.get(teapot_water));
          int cauldronLevel = state.getValue(LayeredCauldronBlock.LEVEL) - teapotLevels;
          BlockState cauldronstate = cauldronLevel == 0 ? Blocks.CAULDRON.defaultBlockState() : state.setValue(LayeredCauldronBlock.LEVEL, Integer.valueOf(cauldronLevel));
          level.setBlockAndUpdate(pos, cauldronstate);
          level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(cauldronstate));
          level.playSound((Player)null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
          level.gameEvent((Entity)null, GameEvent.FLUID_PICKUP, pos);
        }
        return InteractionResult.PASS;
      });
    });
  }

  @SubscribeEvent
  static void gatherData(GatherDataEvent event) {
    if (event.includeServer()) {
      ExistingFileHelper existing = event.getExistingFileHelper();
      DataGenerator generator = event.getGenerator();
      PackOutput output = generator.getPackOutput();
      CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
      BlockTagGenerator blockTags = new BlockTagGenerator(output, lookupProvider, existing);
      generator.addProvider(event.includeServer(), blockTags);
      generator.addProvider(event.includeServer(), new ItemTagGenerator(output, blockTags, lookupProvider, existing));
      generator.addProvider(event.includeServer(), new RecipeGenerator(output));
      generator.addProvider(event.includeServer(), new LootTableGenerator(output));
      generator.addProvider(event.includeServer(), new WorldgenGenerator(output, lookupProvider, Set.of(SimplyTea.MOD_ID)));
    }
  }
  
  /* Helper methods */
  
  /**
   * Registers a forge registry object at the given resource location
   * @param <V>  Value class, extends registry class
   * @param <R>  Registry class
   * @param helper  The RegisterHelper
   * @param value     Value to register
   * @param location  Resource location
   */
  private static <V extends R, R> void register(RegisterHelper<R> helper, V value, ResourceLocation location) {
	helper.register(location, value);
  }

  /**
   * Registers a forge registry object using the given name and a domain of "simplytea"
   * @param <V>  Value class, extends registry class
   * @param <R>  Registry class
   * @param helper  The RegisterHelper
   * @param value     Value to register
   * @param name  Registration name
   */
  private static <V extends R, R> V register(RegisterHelper<R> helper, V value, String name) {
	helper.register(name, value);
	return value;
  }
  
  /**
   * Registers a block item
   * @param helper  Item register helper
   * @param item    Item to register, registry name will be set automatically from the internal block
   */
  private static void registerBlockItem(RegisterHelper<Item> helper, BlockItem item) {
    ResourceLocation rl = ForgeRegistries.BLOCKS.getKey(item.getBlock());
    register(helper, item, Objects.requireNonNull(rl));
  }
}
