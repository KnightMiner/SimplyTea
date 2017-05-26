package elucent.simplytea;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

@Mod(modid = SimplyTea.MODID, version = SimplyTea.VERSION, name = SimplyTea.NAME)
public class SimplyTea
{
    public static final String MODID = "simplytea";
    public static final String NAME = "Simply Tea!";
    public static final String VERSION = "1.0";
    public static Random random = new Random();
    
    @SidedProxy(clientSide = "elucent.simplytea.SimplyTea$ClientProxy",serverSide = "elucent.simplytea.SimplyTea$CommonProxy")
    public static CommonProxy proxy;
    
    public static CreativeTabs tab = new CreativeTabs("simplytea"){
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(SimplyTea.leaf_tea);
		}
    };
    
    @EventHandler
    public void preinit(FMLPreInitializationEvent event){
        proxy.preInit(event);
    }
    
    public static class CommonProxy {
    	public void preInit(FMLPreInitializationEvent event){
    		SimplyTea.registerAll();
    		SimplyTea.registerRecipes();
    	}
    }
    
    public static class ClientProxy extends CommonProxy {
    	@Override
    	public void preInit(FMLPreInitializationEvent event){
    		super.preInit(event);
    		SimplyTea.registerModels();
    	}
    }
    
    public interface IModeledObject {
    	@SideOnly(Side.CLIENT)
    	public void initModel();
    }
    
    public static class ItemBase extends Item implements IModeledObject {
    	public ItemBase(String name, boolean addToTab){
    		setRegistryName(SimplyTea.MODID+":"+name);
    		setUnlocalizedName(name);
    		if (addToTab){
    			setCreativeTab(SimplyTea.tab);
    		}
    		GameRegistry.register(this);
    	}
    	
		@Override
		public void initModel() {
			ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(),"inventory"));
		}
    }
    
    public static class ItemHotTeapot extends ItemBase {
		public ItemHotTeapot(String name, boolean addToTab) {
			super(name, addToTab);
			this.setContainerItem(this);
			setMaxStackSize(1);
		}
		
		@Override
		public boolean hasContainerItem(ItemStack stack){
			return true;
		}
		
		@Override
		public ItemStack getContainerItem(ItemStack stack){
			ItemStack c = stack.copy();
			c.setItemDamage(Math.max(0,stack.getItemDamage()-1));
			if (c.getItemDamage() == 0){
				return new ItemStack(teapot,1);
			}
			return c;
		}
		
		@Override
		public boolean showDurabilityBar(ItemStack stack){
			return stack.getMetadata() < 4;
		}
		
		@Override
		public double getDurabilityForDisplay(ItemStack stack){
			return 1.0 - (double)stack.getMetadata() / 4.0;
		}
    	
		@Override
		public void initModel() {
			ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(),"inventory"));
			ModelLoader.setCustomModelResourceLocation(this, 1, new ModelResourceLocation(getRegistryName(),"inventory"));
			ModelLoader.setCustomModelResourceLocation(this, 2, new ModelResourceLocation(getRegistryName(),"inventory"));
			ModelLoader.setCustomModelResourceLocation(this, 3, new ModelResourceLocation(getRegistryName(),"inventory"));
			ModelLoader.setCustomModelResourceLocation(this, 4, new ModelResourceLocation(getRegistryName(),"inventory"));
		}
		
		@Override
		public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced){
			tooltip.add(I18n.format("simplytea.tooltip.boiling"));
		}
		
		@Override
		public void getSubItems(Item item, CreativeTabs tab, NonNullList<ItemStack> stacks){
			if (item == this){
				stacks.add(new ItemStack(this,1,4));
			}
		}
    }
    
    public static class ItemTeapot extends ItemBase {
		public ItemTeapot(String name, boolean addToTab) {
			super(name, addToTab);
			setMaxStackSize(1);
		}
		
		@Override
	    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn){
	        ItemStack stack = playerIn.getHeldItem(handIn);
	        if (stack.getMetadata() == 0){
	        	RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, true);
	        	if (raytraceresult != null && raytraceresult.typeOfHit != null && raytraceresult.typeOfHit == Type.BLOCK){
	        		IBlockState state = worldIn.getBlockState(raytraceresult.getBlockPos());
	        		if (state.getBlock() == Blocks.WATER){
	        			stack.setItemDamage(1);
	        			playerIn.setHeldItem(handIn, stack);
	        			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS,stack);
	        		}
	        	}
	        }
			return new ActionResult<ItemStack>(EnumActionResult.FAIL,stack);
		}
    	
		@Override
		public void initModel() {
			ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(),"inventory"));
			ModelLoader.setCustomModelResourceLocation(this, 1, new ModelResourceLocation(getRegistryName(),"inventory"));
		}
		
		@Override
		public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced){
			if (stack.getMetadata() == 0){
				tooltip.add(I18n.format("simplytea.tooltip.empty"));
			}
			if (stack.getMetadata() == 1){
				tooltip.add(I18n.format("simplytea.tooltip.water"));
			}
		}
		
		@Override
		public void getSubItems(Item item, CreativeTabs tab, NonNullList<ItemStack> stacks){
			if (item == this){
				stacks.add(new ItemStack(this,1));
				stacks.add(new ItemStack(this,1,1));
			}
		}
    }
    
    public static class ItemTeaCup extends ItemFood implements IModeledObject {
		public ItemTeaCup(String name, int food, float sat, boolean addToTab) {
			super(food, sat, false);
			setMaxStackSize(1);
    		setRegistryName(SimplyTea.MODID+":"+name);
    		setUnlocalizedName(name);
    		if (addToTab){
    			setCreativeTab(SimplyTea.tab);
    		}
    		GameRegistry.register(this);
    	}
		
		@Override
	    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn){
	        ItemStack stack = playerIn.getHeldItem(handIn);
	        playerIn.setActiveHand(handIn);
    		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS,stack);
	    }
		
		@Override
		public EnumAction getItemUseAction(ItemStack stack){
			return EnumAction.DRINK;
		}
		
		@Override
		public int getMaxItemUseDuration(ItemStack stack){
			return 20;
		}
    	
		@Override
		public void initModel() {
			ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(),"inventory"));
			ModelLoader.setCustomModelResourceLocation(this, 1, new ModelResourceLocation(getRegistryName(),"inventory"));
		}
		
		@Override
		public boolean showDurabilityBar(ItemStack stack){
			return stack.getItemDamage() > 0;
		}
		
		@Override
		public double getDurabilityForDisplay(ItemStack stack){
			if (stack.getItemDamage() > 0){
				return (double)stack.getItemDamage() / 2.0;
			}
			return 0.0;
		}
    	
		@Override
		public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase base){
			ItemStack s = stack.copy();
			super.onItemUseFinish(stack, world, base);
			s.setItemDamage(stack.getItemDamage()+1);
			if (s.getItemDamage() >= 2){
				return new ItemStack(SimplyTea.cup,1);
			}
			return s;
		}
    }
    
    public static class BlockTeaSapling extends BlockBush implements IGrowable, IModeledObject {
    	public Item itemBlock = null;
    	public static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 1);
		public BlockTeaSapling(String name, boolean addToTab) {
			super();
			this.setSoundType(SoundType.PLANT);
			setUnlocalizedName(name);
			setRegistryName(SimplyTea.MODID+":"+name);
			if (addToTab){
				setCreativeTab(SimplyTea.tab);
			}
	        this.setTickRandomly(true);
			GameRegistry.register(this);
	        GameRegistry.register(itemBlock = (new ItemBlock(this).setRegistryName(this.getRegistryName())));
		}
		
		@Override
		public BlockStateContainer createBlockState(){
			return new BlockStateContainer(this, STAGE);
		}
		
		@Override
		public int getMetaFromState(IBlockState state){
			return state.getValue(STAGE);
		}
		
		@Override
		public IBlockState getStateFromMeta(int meta){
			return getDefaultState().withProperty(STAGE,meta);
		}
		
		@Override
		public boolean requiresUpdates(){
			return true;
		}

		@Override
	    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	    {
	        if (!worldIn.isRemote)
	        {
	            super.updateTick(worldIn, pos, state, rand);

	            if (worldIn.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(7) == 0)
	            {
	                this.grow(worldIn, pos, state, rand);
	            }
	        }
	    }

		@Override
	    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	    {
	        if (!worldIn.isRemote)
	        {
	            super.updateTick(worldIn, pos, state, rand);

	            if (worldIn.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(7) == 0)
	            {
	                this.grow(worldIn, pos, state, rand);
	            }
	        }
	    }

	    public void grow(World worldIn, BlockPos pos, IBlockState state, Random rand)
	    {
	        if (((Integer)state.getValue(STAGE)).intValue() == 0)
	        {
	            worldIn.setBlockState(pos, state.cycleProperty(STAGE), 4);
	        }
	        else
	        {
	            this.generateTree(worldIn, pos, state, rand);
	        }
	    }
		
		@Override
		public boolean isOpaqueCube(IBlockState state){
			return false;
		}
		
		@Override
		public boolean isFullCube(IBlockState state){
			return false;
		}
		
		@Override
		public boolean isFullBlock(IBlockState state){
			return false;
		}
		
		public static void generateTree(World world, BlockPos pos, IBlockState state, Random random){
			int height = random.nextInt(3)+4;
			world.setBlockState(pos, SimplyTea.tea_trunk.getDefaultState().withProperty(BlockTeaTrunk.TYPE, 0).withProperty(BlockTeaTrunk.CLIPPED, false));
			for (int i = 1; i < height; i ++){
				if (i == 1){
					world.setBlockState(pos.up(i), SimplyTea.tea_trunk.getDefaultState().withProperty(BlockTeaTrunk.TYPE, 1).withProperty(BlockTeaTrunk.CLIPPED, false));
				}
				else if (i < height-1){
					boolean north = random.nextBoolean();
					boolean south = random.nextBoolean();
					boolean west = random.nextBoolean();
					boolean east = random.nextBoolean();
					if (north){
						world.setBlockState(pos.up(i).north(), SimplyTea.tea_trunk.getDefaultState().withProperty(BlockTeaTrunk.TYPE, 6).withProperty(BlockTeaTrunk.CLIPPED, false));
					}
					if (east){
						world.setBlockState(pos.up(i).east(), SimplyTea.tea_trunk.getDefaultState().withProperty(BlockTeaTrunk.TYPE, 7).withProperty(BlockTeaTrunk.CLIPPED, false));
					}
					if (south){
						world.setBlockState(pos.up(i).south(), SimplyTea.tea_trunk.getDefaultState().withProperty(BlockTeaTrunk.TYPE, 4).withProperty(BlockTeaTrunk.CLIPPED, false));
					}
					if (west){
						world.setBlockState(pos.up(i).west(), SimplyTea.tea_trunk.getDefaultState().withProperty(BlockTeaTrunk.TYPE, 5).withProperty(BlockTeaTrunk.CLIPPED, false));
					}
					world.setBlockState(pos.up(i), SimplyTea.tea_trunk.getDefaultState().withProperty(BlockTeaTrunk.TYPE, 2).withProperty(BlockTeaTrunk.CLIPPED, false));
				}
				else {
					world.setBlockState(pos.up(i), SimplyTea.tea_trunk.getDefaultState().withProperty(BlockTeaTrunk.TYPE, 3).withProperty(BlockTeaTrunk.CLIPPED, false));
				}
			}
		}
		
		@Override
		public void initModel(){
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName().toString(),"inventory"));
		}

		@Override
		public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean isClient) {
			return world.isAirBlock(pos.up(1)) 
					&& world.isAirBlock(pos.up(2))
					&& world.isAirBlock(pos.up(3))
					&& world.isAirBlock(pos.up(4))
					&& world.isAirBlock(pos.up(5))
					&& world.isAirBlock(pos.up(6));
		}
		
		@Override
	    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
	    {
	        this.grow(worldIn, pos, state, rand);
	    }

		@Override
		public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
			return true;
		}
    }
    
    public static class BlockTeaTrunk extends Block implements IModeledObject {
    	public static final PropertyInteger TYPE = PropertyInteger.create("type", 0, 7);
    	public static final PropertyBool CLIPPED = PropertyBool.create("clipped");
    	
    	public static final AxisAlignedBB AABB_BOTTOM = new AxisAlignedBB(0.375, 0, 0.375, 0.625, 1, 0.625);
    	public static final AxisAlignedBB AABB_BOTTOM_2 = new AxisAlignedBB(0.125, 0, 0.125, 0.875, 1, 0.875);
    	public static final AxisAlignedBB AABB_MID = new AxisAlignedBB(0, 0, 0, 1, 1, 1);
    	public static final AxisAlignedBB AABB_TOP = new AxisAlignedBB(0.125, 0, 0.125, 0.875, 0.75, 0.875);
    	public static final AxisAlignedBB AABB_NORTH = new AxisAlignedBB(0.125, 0.125, 0, 0.875, 0.875, 0.75);
    	public static final AxisAlignedBB AABB_WEST = new AxisAlignedBB(0, 0.125, 0.125, 0.75, 0.875, 0.875);
    	public static final AxisAlignedBB AABB_SOUTH = new AxisAlignedBB(0.125, 0.125, 0.25, 0.875, 0.875, 1.0);
    	public static final AxisAlignedBB AABB_EAST = new AxisAlignedBB(0.25, 0.125, 0.125, 1.0, 0.875, 0.875);

    	public Item itemBlock = null;
		public BlockTeaTrunk(String name, boolean addToTab) {
			super(Material.WOOD);
			this.setSoundType(SoundType.WOOD);
			setHarvestLevel("axe",-1);
			setHardness(1.8f);
			setUnlocalizedName(name);
			setRegistryName(SimplyTea.MODID+":"+name);
			if (addToTab){
				setCreativeTab(SimplyTea.tab);
			}
			this.setTickRandomly(true);
			GameRegistry.register(this);
	        GameRegistry.register(itemBlock = (new ItemBlock(this).setRegistryName(this.getRegistryName())));
	    }
		
		@Override
		public boolean getTickRandomly(){
			return true;
		}
		
		@Override
		public boolean requiresUpdates(){
			return true;
		}

		@Override
	    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	    {
            if (state.getBlock() == this){
            	if (state.getValue(CLIPPED)){
            		worldIn.setBlockState(pos, state.withProperty(CLIPPED, false));
            		worldIn.notifyBlockUpdate(pos, state, state.withProperty(CLIPPED, false), 8);
            	}
            }
	    }
		
		@Override
		public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player){
			return new ItemStack(SimplyTea.tea_sapling,1);
		}
		
		@Override
		public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing face, float hitX, float hitY, float hitZ){
			ItemStack heldItem = player.getHeldItem(hand);
			if (heldItem.getItem() instanceof ItemShears){
				if (state.getBlock() == this){
					if (!state.getValue(CLIPPED) && state.getValue(TYPE) != 0){
						ItemStack stack = heldItem.copy();
						((ItemShears)heldItem.getItem()).setDamage(stack, ((ItemShears)heldItem.getItem()).getDamage(stack)+1);
						if (((ItemShears)heldItem.getItem()).getDamage(stack) > ((ItemShears)heldItem.getItem()).getMaxDamage()){
							player.setHeldItem(hand, ItemStack.EMPTY);
						}
						else {
							player.setHeldItem(hand, stack);
						}
						this.dropBlockAsItem(world, pos, state, 0);
						world.setBlockState(pos, state.withProperty(CLIPPED, true), 8);
						world.notifyBlockUpdate(pos, state, state.withProperty(CLIPPED, true), 8);
						return true;
					}
				}
			}
			return false;
		}
		
		@Override
		public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos){
			if (state.getBlock() == this){
				if (state.getValue(TYPE) == 0){
					return AABB_BOTTOM;
				}
				if (state.getValue(TYPE) == 1){
					return AABB_BOTTOM_2;
				}
				if (state.getValue(TYPE) == 2){
					return AABB_MID;
				}
				if (state.getValue(TYPE) == 3){
					return AABB_TOP;
				}
				if (state.getValue(TYPE) == 4){
					return AABB_NORTH;
				}
				if (state.getValue(TYPE) == 5){
					return AABB_EAST;
				}
				if (state.getValue(TYPE) == 6){
					return AABB_SOUTH;
				}
				if (state.getValue(TYPE) == 7){
					return AABB_WEST;
				}
			}
			return AABB_MID;
		}
		
		@Override
		public BlockStateContainer createBlockState(){
			return new BlockStateContainer(this, TYPE, CLIPPED);
		}
		
		@Override
		public int getMetaFromState(IBlockState state){
			return state.getValue(TYPE) + (state.getValue(CLIPPED) ? 8 : 0);
		}
		
		@Override
		public IBlockState getStateFromMeta(int meta){
			return getDefaultState().withProperty(TYPE,meta % 8).withProperty(CLIPPED, meta >= 8);
		}
		
		@Override
		public boolean isOpaqueCube(IBlockState state){
			return false;
		}
		
		@Override
		public boolean isFullCube(IBlockState state){
			return false;
		}
		
		@Override
		public boolean isFullBlock(IBlockState state){
			return false;
		}
		
		@Override
		public BlockRenderLayer getBlockLayer(){
			return BlockRenderLayer.CUTOUT_MIPPED;
		}
		
		@Override
		public void getSubBlocks(Item i, CreativeTabs tab, NonNullList list){
			
		}
		
		@Override
		public NonNullList<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune){
			NonNullList<ItemStack> drops = NonNullList.create();
			if (state.getBlock() == this){
				if (state.getValue(TYPE) > 0 && !state.getValue(CLIPPED)){
					drops.add(new ItemStack(SimplyTea.leaf_tea,SimplyTea.random.nextInt(3)+1));
					if (SimplyTea.random.nextInt(10) == 0){
						drops.add(new ItemStack(SimplyTea.tea_sapling,1));
					}
				}
				drops.add(new ItemStack(Items.STICK,SimplyTea.random.nextInt(3)+1));
			}
			return drops;
		}
		
		@Override
		public void initModel(){
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName().toString(),"inventory"));
		}
    }
    
    public static class WorldGenTeaTrees implements IWorldGenerator {

		@Override
		public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
				IChunkProvider chunkProvider) {
			if (random.nextInt(12) == 0){
				int x = chunkX * 16 + 2 + random.nextInt(12);
				int z = chunkZ * 16 + 2 + random.nextInt(12);
				BlockPos p = new BlockPos(x,0,z);
				p = world.getHeight(p);
				Biome b = world.getBiome(p);
				if (BiomeDictionary.hasType(b, BiomeDictionary.Type.FOREST) || b == Biomes.FOREST || b == Biomes.FOREST_HILLS){
					if (world.getBlockState(p.down()).getBlock() instanceof BlockGrass || world.isAirBlock(p)){
						BlockTeaSapling.generateTree(world, p, Blocks.AIR.getDefaultState(), random);
					}
				}
			}
		}
    	
    }
    
    private static List<Item> items = new ArrayList<Item>();
    private static List<Block> blocks = new ArrayList<Block>();
    
    public static Block tea_sapling, tea_trunk;
    public static Item leaf_tea, black_tea, cup, cup_tea_black, cup_tea_green, teabag, teapot, hot_teapot, teabag_green, teabag_black;
    
    public static void registerAll(){
    	items.add(leaf_tea = new ItemBase("leaf_tea",true));
    	items.add(black_tea = new ItemBase("black_tea",true));
    	items.add(teabag = new ItemBase("teabag",true));
    	items.add(teabag_green = new ItemBase("teabag_green",true));
    	items.add(teabag_black = new ItemBase("teabag_black",true));
    	items.add(cup = new ItemBase("cup",true).setMaxStackSize(1));
    	items.add(cup_tea_black = new ItemTeaCup("cup_tea_black",4,8f,true));
    	items.add(cup_tea_green = new ItemTeaCup("cup_tea_green",3,5f,true));
    	items.add(teapot = new ItemTeapot("teapot",true));
    	items.add(hot_teapot = new ItemHotTeapot("hot_teapot",true));
    	
    	blocks.add(tea_sapling = new BlockTeaSapling("tea_sapling",true));
    	blocks.add(tea_trunk = new BlockTeaTrunk("tea_trunk",true));
    	
    	GameRegistry.registerWorldGenerator(new WorldGenTeaTrees(), 100);
    }
    
    public static void registerRecipes(){
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(teabag,4),true,new Object[]{
				"  S",
				"PP ",
				"PP ",
				'S', "string",
				'P', Items.PAPER}).setMirrored(true));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(teabag_green,1),new Object[]{
				new ItemStack(teabag, 1), leaf_tea}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(teabag_black,1),new Object[]{
				new ItemStack(teabag, 1), black_tea}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(cup,1),true,new Object[]{
				"CBC",
				" C ",
				'C', Items.CLAY_BALL,
				'B', new ItemStack(Items.DYE,1,15)}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(teapot,1),true,new Object[]{
				"CcC",
				"CC ",
				'C', Items.CLAY_BALL,
				'c', new ItemStack(Items.DYE,1,6)}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(cup_tea_green,1),new Object[]{
				teabag_green, new ItemStack(hot_teapot,1,1), cup}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(cup_tea_black,1),new Object[]{
				teabag_black, new ItemStack(hot_teapot,1,1), cup}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(cup_tea_green,1),new Object[]{
				teabag_green, new ItemStack(hot_teapot,1,2), cup}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(cup_tea_black,1),new Object[]{
				teabag_black, new ItemStack(hot_teapot,1,2), cup}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(cup_tea_green,1),new Object[]{
				teabag_green, new ItemStack(hot_teapot,1,3), cup}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(cup_tea_black,1),new Object[]{
				teabag_black, new ItemStack(hot_teapot,1,3), cup}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(cup_tea_green,1),new Object[]{
				teabag_green, new ItemStack(hot_teapot,1,4), cup}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(cup_tea_black,1),new Object[]{
				teabag_black, new ItemStack(hot_teapot,1,4), cup}));
		FurnaceRecipes.instance().addSmelting(leaf_tea, new ItemStack(black_tea,1), 0.1f);
		FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(teapot,1,1), new ItemStack(hot_teapot,1,4), 0.1f);
	}
    
    public static void registerModels(){
    	for (Item i : items){
    		if (i instanceof IModeledObject){
    			((IModeledObject)i).initModel();
    		}
    	}
    	for (Block b : blocks){
    		if (b instanceof IModeledObject){
    			((IModeledObject)b).initModel();
    		}
    	}
    }
}
