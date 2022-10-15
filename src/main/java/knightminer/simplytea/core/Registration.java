package knightminer.simplytea.core;

import knightminer.simplytea.SimplyTea;
import knightminer.simplytea.block.TeaTreeGrower;
import knightminer.simplytea.block.TeaTrunkBlock;
import knightminer.simplytea.data.AddEntryLootModifier;
import knightminer.simplytea.data.gen.BlockTagGenerator;
import knightminer.simplytea.data.gen.ItemTagGenerator;
import knightminer.simplytea.data.gen.LootModifierGenerator;
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
import net.minecraft.core.Registry;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
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
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

@EventBusSubscriber(modid = SimplyTea.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class Registration {
  /* Creative tab */
  public static CreativeModeTab group = new CreativeModeTab("simplytea") {
    @Override
    public ItemStack makeIcon() {
      return new ItemStack(tea_leaf.get());
    }
  };
  
  public static final DeferredRegister<GlobalLootModifierSerializer<?>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.LOOT_MODIFIER_SERIALIZERS, SimplyTea.MOD_ID);

  public static final RegistryObject<AddEntryLootModifier.Serializer> ADD_ENTRY_MODIFIER = LOOT_MODIFIERS.register("add_loot_entry", AddEntryLootModifier.Serializer::new);
  
  /* Potions */
  public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.Keys.MOB_EFFECTS, SimplyTea.MOD_ID);

  public static final RegistryObject<RestfulEffect> restful = EFFECTS.register("restful", RestfulEffect::new);
  public static final RegistryObject<RelaxedEffect> relaxed = EFFECTS.register("relaxed", RelaxedEffect::new);
  public static final RegistryObject<CaffeinatedEffect> caffeinated = EFFECTS.register("caffeinated", CaffeinatedEffect::new);
  public static final RegistryObject<InvigoratedEffect> invigorated = EFFECTS.register("invigorated", InvigoratedEffect::new);
  public static final RegistryObject<EnderfallingEffect> enderfalling = EFFECTS.register("enderfalling", EnderfallingEffect::new);

  /* Blocks */
  public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.Keys.BLOCKS, SimplyTea.MOD_ID);

  public static final RegistryObject<SaplingBlock> tea_sapling = BLOCKS.register("tea_sapling", () -> new SaplingBlock(new TeaTreeGrower(), Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
  public static final RegistryObject<TeaTrunkBlock> tea_trunk = BLOCKS.register("tea_trunk", () -> new TeaTrunkBlock(Block.Properties.of(Material.WOOD, MaterialColor.COLOR_BROWN).strength(2.0F).sound(SoundType.WOOD).randomTicks()));
  public static final RegistryObject<FenceBlock> tea_fence = BLOCKS.register("tea_fence", () -> new FenceBlock(Block.Properties.of(Material.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
  public static final RegistryObject<FenceGateBlock> tea_fence_gate = BLOCKS.register("tea_fence_gate", () -> new FenceGateBlock(Block.Properties.of(Material.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
  public static final RegistryObject<FlowerPotBlock> potted_tea_sapling = BLOCKS.register("potted_tea_sapling", () -> {
	  return new FlowerPotBlock(
			  () -> (FlowerPotBlock) Blocks.FLOWER_POT.delegate.get(),
			  tea_sapling::get,
			  Block.Properties.of(Material.DECORATION).strength(0f).noOcclusion()
		);
  });

  /* Items */
  public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.Keys.ITEMS, SimplyTea.MOD_ID);

  /* Crafting */
  public static final RegistryObject<TooltipItem> tea_leaf = ITEMS.register("tea_leaf", () -> new TooltipItem(new Item.Properties().tab(group)));
  public static final RegistryObject<TooltipItem> black_tea = ITEMS.register("black_tea", () -> new TooltipItem(new Item.Properties().tab(group)));
  public static final RegistryObject<TeaStickItem> tea_stick = ITEMS.register("tea_stick", () -> new TeaStickItem(new Item.Properties().tab(group)));
  public static final RegistryObject<TooltipItem> ice_cube = ITEMS.register("ice_cube", () -> new TooltipItem(new Item.Properties().tab(group)));
  public static final RegistryObject<TooltipItem> chorus_petal = ITEMS.register("chorus_petal", () -> new TooltipItem(new Item.Properties().tab(group)));

  /* Tea bags */
  public static final RegistryObject<Item> teabag = ITEMS.register("teabag", () -> new Item(new Item.Properties().tab(group)));
  public static final RegistryObject<Item> teabag_black = ITEMS.register("teabag_black", () -> new Item(new Item.Properties().tab(group)));
  public static final RegistryObject<Item> teabag_floral = ITEMS.register("teabag_floral", () -> new Item(new Item.Properties().tab(group)));
  public static final RegistryObject<Item> teabag_chorus = ITEMS.register("teabag_chorus", () -> new Item(new Item.Properties().tab(group)));
  public static final RegistryObject<Item> teabag_green = ITEMS.register("teabag_green", () -> new Item(new Item.Properties().tab(group)));

  /* Tea pots */
  public static final RegistryObject<Item> unfired_teapot = ITEMS.register("unfired_teapot", () -> new Item(new Item.Properties().tab(group).stacksTo(16)));
  public static final RegistryObject<TeapotItem> teapot = ITEMS.register("teapot", () -> new TeapotItem(new Item.Properties().tab(group).stacksTo(16)));
  public static final RegistryObject<TooltipItem> teapot_water = ITEMS.register("teapot_water", () -> new TooltipItem(new Item.Properties().tab(group).stacksTo(16)));
  public static final RegistryObject<TooltipItem> teapot_milk = ITEMS.register("teapot_milk", () -> new TooltipItem(new Item.Properties().tab(group).stacksTo(16)));
  public static final RegistryObject<HotTeapotItem> teapot_hot = ITEMS.register("teapot_hot", () -> new HotTeapotItem(new Item.Properties().tab(group).stacksTo(16)));
  public static final RegistryObject<HotTeapotItem> teapot_frothed = ITEMS.register("teapot_frothed", () -> new HotTeapotItem(new Item.Properties().tab(group).stacksTo(16)));

  /* Drinks */
  public static final RegistryObject<Item> unfired_cup = ITEMS.register("unfired_cup", () -> new Item(new Item.Properties().tab(group).stacksTo(16)));
  public static final RegistryObject<Item> cup = ITEMS.register("cup", () -> new Item(new Item.Properties().tab(group).stacksTo(16)));
  public static final RegistryObject<TeaCupItem> cup_tea_black = ITEMS.register("cup_tea_black", () -> new TeaCupItem(new Item.Properties().tab(group).stacksTo(16).food(Config.SERVER.black_tea)));
  public static final RegistryObject<TeaCupItem> cup_tea_green = ITEMS.register("cup_tea_green", () -> new TeaCupItem(new Item.Properties().tab(group).stacksTo(16).food(Config.SERVER.green_tea)));
  public static final RegistryObject<TeaCupItem> cup_tea_floral = ITEMS.register("cup_tea_floral", () -> new TeaCupItem(new Item.Properties().tab(group).stacksTo(16).food(Config.SERVER.floral_tea)));
  public static final RegistryObject<TeaCupItem> cup_tea_chai = ITEMS.register("cup_tea_chai", () -> new TeaCupItem(new Item.Properties().tab(group).stacksTo(16).food(Config.SERVER.chai_tea)));
  public static final RegistryObject<TeaCupItem> cup_tea_iced = ITEMS.register("cup_tea_iced", () -> new TeaCupItem(new Item.Properties().tab(group).stacksTo(16).food(Config.SERVER.iced_tea)));
  public static final RegistryObject<TeaCupItem> cup_tea_chorus = ITEMS.register("cup_tea_chorus", () -> new TeaCupItem(new Item.Properties().tab(group).stacksTo(16).food(Config.SERVER.chorus_tea)));
  public static final RegistryObject<CocoaItem> cup_cocoa = ITEMS.register("cup_cocoa", () -> new CocoaItem(new Item.Properties().tab(group).stacksTo(16).food(Config.SERVER.cocoa)));
  
  /* Fence Items */
  public static final RegistryObject<WoodBlockItem> tea_fence_item = ITEMS.register("tea_fence", () -> new WoodBlockItem(tea_fence.get(), new Item.Properties().tab(group)));
  public static final RegistryObject<WoodBlockItem> tea_fence_gate_item = ITEMS.register("tea_fence_gate", () -> new WoodBlockItem(tea_fence_gate.get(), new Item.Properties().tab(group)));
  public static final RegistryObject<BlockItem> tea_sapling_item = ITEMS.register("tea_sapling", () -> new BlockItem(tea_sapling.get(), new Item.Properties().tab(group)));

  /* Recipes */
  public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.RECIPE_SERIALIZERS, SimplyTea.MOD_ID);
  
  public static final RegistryObject<ShapelessHoneyRecipe.Serializer> shapeless_honey = RECIPE_SERIALIZERS.register("shapeless_honey", () -> new ShapelessHoneyRecipe.Serializer());
  
  /* World Gen */
  public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registry.FEATURE_REGISTRY, SimplyTea.MOD_ID);

  public static final RegistryObject<Feature<NoneFeatureConfiguration>> tea_tree = FEATURES.register("tea_tree", TeaTreeFeature::new);
  
  public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, SimplyTea.MOD_ID);
  
  public static final RegistryObject<ConfiguredFeature<?, ?>> configured_tea_tree = CONFIGURED_FEATURES.register("configured_tea_tree", () -> {
	return new ConfiguredFeature<NoneFeatureConfiguration, Feature<NoneFeatureConfiguration>>(tea_tree.get(), NoneFeatureConfiguration.NONE);
  });
  
  public static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, SimplyTea.MOD_ID);
  
  public static final RegistryObject<PlacedFeature> placed_tea_tree = PLACED_FEATURES.register("placed_tea_tree", () -> {
	return new PlacedFeature(configured_tea_tree.getHolder().get(), List.of(
				RarityFilter.onAverageOnceEvery(384),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
				BiomeFilter.biome()
			));
  });

  @SubscribeEvent
  static void registerMisc(final FMLCommonSetupEvent event) {
    event.enqueueWork(() -> {
      // flamability
      if (Blocks.FIRE instanceof FireBlock fire) {
        fire.setFlammable(tea_fence.get(), 5, 20);
        fire.setFlammable(tea_fence_gate.get(), 5, 20);
        fire.setFlammable(tea_trunk.get(), 15, 30);
      }

      ComposterBlock.add(0.3f, tea_leaf.get());
      ComposterBlock.add(0.4f, black_tea.get());
      ComposterBlock.add(0.5f, chorus_petal.get());
      ComposterBlock.add(0.3f, tea_sapling.get());

      // too much caffiene to sleep
      RestfulEffect.addConflict(caffeinated.get());
      RestfulEffect.addConflict(invigorated.get());
      
      ((FlowerPotBlock)Blocks.FLOWER_POT).addPlant(tea_sapling.getKey().location(), potted_tea_sapling);
      
      // Register cauldron interaction
      CauldronInteraction.WATER.put(teapot.get(), (pBlockState, pLevel, pBlockPos, pPlayer, pHand, pStack) -> {
    	  if(!Config.SERVER.teapot.fillFromCauldron() || pBlockState.getValue(LayeredCauldronBlock.LEVEL) != 3) {
    		  return InteractionResult.PASS;
    	  }
    	  if(!pLevel.isClientSide) {
    		  Item item = pStack.getItem();
              pPlayer.setItemInHand(pHand, ItemUtils.createFilledResult(pStack, pPlayer, new ItemStack(Registration.teapot_water.get())));
              pPlayer.awardStat(Stats.USE_CAULDRON);
              pPlayer.awardStat(Stats.ITEM_USED.get(item));
              pLevel.setBlockAndUpdate(pBlockPos, Blocks.CAULDRON.defaultBlockState());
              pLevel.playSound((Player)null, pBlockPos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
              pLevel.gameEvent((Entity)null, GameEvent.FLUID_PICKUP, pBlockPos);
    	  }
    	  return InteractionResult.sidedSuccess(pLevel.isClientSide);
       });
    });
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
      generator.addProvider(new LootModifierGenerator(generator));
    }
  }
}
