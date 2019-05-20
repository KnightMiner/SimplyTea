package elucent.simplytea.core;

import elucent.simplytea.SimplyTea;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidBlock;

import java.util.Random;

public final class Util {
    private Util () {}

    /**
     * Initializes an item, setting its registry name, unlocalized name, and creative tab
     * @param block     Block to initialize
     * @param name      Name for registry and localization
     * @param addToTab  If true, adds to the creative tab
     * @return ItemBlock for this block
     */
    public static Item init(Block block, String name, boolean addToTab) {
        ResourceLocation loc = new ResourceLocation(SimplyTea.MODID, name);
        block.setRegistryName(loc);
        block.setTranslationKey(name);
        if(addToTab) {
            block.setCreativeTab(SimplyTea.tab);
        }

        return new ItemBlock(block).setRegistryName(loc);
    }

    /**
     * Initializes an item, setting its registry name, unlocalized name, and creative tab
     * @param item      Item to initialize
     * @param name      Name for registry and localization
     * @param addToTab  If true, adds to the creative tab
     */
    public static void init(Item item, String name, boolean addToTab) {
        item.setRegistryName(new ResourceLocation(SimplyTea.MODID, name));
        item.setTranslationKey(name);
        if(addToTab) {
            item.setCreativeTab(SimplyTea.tab);
        }
    }

    /**
     * Gets a fluid from a block state liquid
     * @param state  State to check for fluid
     * @return  Fluid from this block state, or null if none
     */
    public static Fluid getFluid(IBlockState state) {
        Block block = state.getBlock();
        if(block instanceof IFluidBlock) {
            return ((IFluidBlock)block).getFluid();
        } else if(block instanceof BlockLiquid && state.getValue(BlockLiquid.LEVEL) == 0) {
            Material material = state.getMaterial();
            if(material == Material.WATER) {
                return FluidRegistry.WATER;
            }
            if(material == Material.LAVA) {
                return FluidRegistry.LAVA;
            }
        }
        return null;
    }

    /**
     * Gets a random number between 1 and max for random drops
     * @param rand  Random instance
     * @param max   Maximum value
     * @return  Random number between 1 and max
     */
    public static int randomCount(Random rand, int max) {
        if(max == 1) {
            return 1;
        }
        return 1 + rand.nextInt(max);
    }
}
