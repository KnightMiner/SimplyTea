package elucent.simplytea;

import java.util.ArrayList;
import java.util.List;
import elucent.simplytea.block.BlockTeaSapling;
import elucent.simplytea.block.BlockTeaTrunk;
import elucent.simplytea.block.IItemBlock;
import elucent.simplytea.core.Config;
import elucent.simplytea.core.IMCHelper;
import elucent.simplytea.core.IModeledObject;
import elucent.simplytea.core.WorldGenTeaTrees;
import elucent.simplytea.item.ItemBase;
import elucent.simplytea.item.ItemCocoa;
import elucent.simplytea.item.ItemHotTeapot;
import elucent.simplytea.item.ItemTeaChamomile;
import elucent.simplytea.item.ItemTeaCup;
import elucent.simplytea.item.ItemTeapot;
import elucent.simplytea.potion.PotionCaffeinated;
import elucent.simplytea.potion.PotionRestful;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

@Mod(modid = SimplyTea.MODID, version = SimplyTea.VERSION, name = SimplyTea.NAME)
public class SimplyTea {
	public static final String MODID = "simplytea";
	public static final String NAME = "Simply Tea!";
	public static final String VERSION = "${version}";

	public static CreativeTabs tab = new CreativeTabs("simplytea") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(SimplyTea.leaf_tea);
		}
	};

	private static List<Item> items = new ArrayList<Item>();
	private static List<Block> blocks = new ArrayList<Block>();

	public static Block tea_sapling, tea_trunk;
	public static Item leaf_tea, black_tea;
	public static Item teabag, teabag_green, teabag_black, teabag_floral, teabag_chamomile;
	public static Item cup, cup_tea_black, cup_tea_green, cup_tea_floral, cup_tea_chamomile, cup_cocoa;
	public static Item teapot, hot_teapot, frothed_teapot;
	public static Potion restful, caffeinated;

	@EventHandler
	public void preinit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);

		caffeinated = new PotionCaffeinated("caffeinated").setCustomIcon(1, 0);
		restful = new PotionRestful("restful").setCustomIcon(0, 0);

		items.add(leaf_tea = new ItemBase("leaf_tea", true));
		items.add(black_tea = new ItemBase("black_tea", true));

		items.add(teabag = new ItemBase("teabag", true));
		items.add(teabag_green = new ItemBase("teabag_green", true));
		items.add(teabag_black = new ItemBase("teabag_black", true));
		items.add(teabag_floral = new ItemBase("teabag_floral", true));

		items.add(cup = new ItemBase("cup", true).setMaxStackSize(16));
		items.add(cup_tea_black = new ItemTeaCup("cup_tea_black", Config.tea.black, true));
		items.add(cup_tea_green = new ItemTeaCup("cup_tea_green", Config.tea.green, true));
		items.add(cup_tea_floral = new ItemTeaCup("cup_tea_floral", Config.tea.floral, true));
		items.add(cup_cocoa = new ItemCocoa("cup_cocoa", true));

		items.add(teapot = new ItemTeapot("teapot", true));
		items.add(hot_teapot = new ItemHotTeapot("hot_teapot", true));
		items.add(frothed_teapot = new ItemHotTeapot("frothed_teapot", true));

		// rustic support: chamomile tea
		if (Loader.isModLoaded("rustic") || Config.tea.chamomile.force) {
			items.add(teabag_chamomile = new ItemBase("teabag_chamomile", true));
			items.add(cup_tea_chamomile = new ItemTeaChamomile("cup_tea_chamomile", true));
		}

		blocks.add(tea_sapling = new BlockTeaSapling("tea_sapling", true));
		blocks.add(tea_trunk = new BlockTeaTrunk("tea_trunk", true));

		if(Config.tree.enable_generation) {
			GameRegistry.registerWorldGenerator(new WorldGenTeaTrees(), 100);
		}
	}

    @SubscribeEvent
    public void onConfigChangedEvent(OnConfigChangedEvent event) {
        if (event.getModID().equals(MODID)) {
            ConfigManager.sync(MODID, Type.INSTANCE);
        }
    }

	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> event) {
		for(Block b : blocks) {
			event.getRegistry().register(b);
		}
	}

	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) {
		for(Item i : items) {
			event.getRegistry().register(i);
		}
		for(Block b : blocks) {
			if(b instanceof IItemBlock) {
				event.getRegistry().register(((IItemBlock) b).getItemBlock());
			}
		}
	}

	@SubscribeEvent
	public void registerPotions(RegistryEvent.Register<Potion> event) {
		IForgeRegistry<Potion> r = event.getRegistry();
		r.register(caffeinated);
		r.register(restful);
	}

	@SubscribeEvent
	public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
		// if Tinkers Construct is loaded, register black tea as a drying rack recipe as its more realistic
		if(Loader.isModLoaded("tconstruct")) {
			IMCHelper.addTinkersDrying(new ItemStack(leaf_tea), new ItemStack(black_tea), 5*60);
		} else {
			GameRegistry.addSmelting(leaf_tea, new ItemStack(black_tea, 1), 0.1f);
		}
		GameRegistry.addSmelting(new ItemStack(teapot, 1, 1), new ItemStack(hot_teapot, 1, 4), 0.1f);
		GameRegistry.addSmelting(new ItemStack(teapot, 1, 2), new ItemStack(frothed_teapot, 1, 4), 0.1f);

		OreDictionary.registerOre("treeSapling", tea_sapling);
	}

	@SubscribeEvent
	public void registerModels(ModelRegistryEvent event) {
		for(Item i : items) {
			if(i instanceof IModeledObject) {
				((IModeledObject) i).initModel();
			}
		}
		for(Block b : blocks) {
			if(b instanceof IModeledObject) {
				((IModeledObject) b).initModel();
			}
		}
	}

	@SubscribeEvent
	public void playerWakeUp(PlayerWakeUpEvent event) {
		if (!event.shouldSetSpawn() || event.updateWorld()) {
			return;
		}

		// if caffeinated, remove that with no restful benefits
		EntityPlayer player = event.getEntityPlayer();
		if (player.isPotionActive(caffeinated)) {
			player.removePotionEffect(caffeinated);
			player.removePotionEffect(restful);
			return;
		}

		// if restful, heal based on the potion level and remove it
		PotionEffect effect = player.getActivePotionEffect(restful);
		if (effect != null) {
			player.heal((effect.getAmplifier()+1)*2);
			player.removePotionEffect(restful);
		}
	}
}
