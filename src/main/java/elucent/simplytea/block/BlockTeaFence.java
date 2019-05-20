package elucent.simplytea.block;

import elucent.simplytea.core.IModeledObject;
import elucent.simplytea.core.Util;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

public class BlockTeaFence extends BlockFence implements IModeledObject, IItemBlock {

    private Item itemBlock = null;
    public BlockTeaFence(String name) {
        super(Material.WOOD, BlockPlanks.EnumType.DARK_OAK.getMapColor());
        itemBlock = Util.init(this, name, true);
    }

    @Override
    public Item getItemBlock() {
        return itemBlock;
    }
}
