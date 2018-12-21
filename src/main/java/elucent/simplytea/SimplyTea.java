package elucent.simplytea;

import java.util.ArrayList;
import java.util.List;
import elucent.simplytea.block.BlockTeaSapling;
import elucent.simplytea.block.BlockTeaTrunk;
import elucent.simplytea.block.IItemBlock;
import elucent.simplytea.item.ItemBase;
import elucent.simplytea.item.ItemHotTeapot;
import elucent.simplytea.item.ItemTeaCup;
import elucent.simplytea.item.ItemTeapot;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = SimplyTea.MODID, version = SimplyTea.VERSION, name = SimplyTea.NAME)
public class SimplyTea {
	public static final String MODID = "simplytea";
	public static final String NAME = "Simply Tea!";
	public static final String VERSION = "1.4";

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
	public static Item teabag, teabag_green, teabag_black;
	public static Item cup, cup_tea_black, cup_tea_green;
	public static Item teapot, hot_teapot;

	@EventHandler
	public void preinit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);

		items.add(leaf_tea = new ItemBase("leaf_tea", true));
		items.add(black_tea = new ItemBase("black_tea", true));
		items.add(teabag = new ItemBase("teabag", true));
		items.add(teabag_green = new ItemBase("teabag_green", true));
		items.add(teabag_black = new ItemBase("teabag_black", true));
		items.add(cup = new ItemBase("cup", true).setMaxStackSize(1));
		items.add(cup_tea_black = new ItemTeaCup("cup_tea_black", 4, 8f, true));
		items.add(cup_tea_green = new ItemTeaCup("cup_tea_green", 3, 5f, true));
		items.add(teapot = new ItemTeapot("teapot", true));
		items.add(hot_teapot = new ItemHotTeapot("hot_teapot", true));

		blocks.add(tea_sapling = new BlockTeaSapling("tea_sapling", true));
		blocks.add(tea_trunk = new BlockTeaTrunk("tea_trunk", true));

		GameRegistry.registerWorldGenerator(new WorldGenTeaTrees(), 100);
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
	public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
		FurnaceRecipes.instance().addSmelting(leaf_tea, new ItemStack(black_tea, 1), 0.1f);
		FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(teapot, 1, 1), new ItemStack(hot_teapot, 1, 4), 0.1f);
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
}
