package elucent.simplytea.block;

import elucent.simplytea.core.IModeledObject;
import elucent.simplytea.core.Util;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockPlanks;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class BlockTeaFenceGate extends BlockFenceGate implements IModeledObject, IItemBlock {

    private Item itemBlock = null;
    public BlockTeaFenceGate(String name) {
        super(BlockPlanks.EnumType.DARK_OAK);
        itemBlock = Util.init(this, name, true);
    }

    @Override
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
        ModelLoader.setCustomStateMapper(this, (new StateMap.Builder()).ignore(POWERED).build());

    }

    @Override
    public Item getItemBlock() {
        return itemBlock;
    }
}
