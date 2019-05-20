package elucent.simplytea.core;

import elucent.simplytea.SimplyTea;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;

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
}
