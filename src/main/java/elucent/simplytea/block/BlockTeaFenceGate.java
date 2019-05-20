package elucent.simplytea.block;

import elucent.simplytea.SimplyTea;
import elucent.simplytea.core.IModeledObject;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockPlanks;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class BlockTeaFenceGate extends BlockFenceGate implements IModeledObject, IItemBlock {

    private Item itemBlock = null;
    public BlockTeaFenceGate(String name) {
        super(BlockPlanks.EnumType.DARK_OAK);
        this.setCreativeTab(SimplyTea.tab);

        this.setUnlocalizedName(name);
        this.setRegistryName(new ResourceLocation(SimplyTea.MODID, name));
        this.setCreativeTab(SimplyTea.tab);
        itemBlock = (new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    @Override
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
                new ModelResourceLocation(getRegistryName().toString(), "inventory"));
        ModelLoader.setCustomStateMapper(this, (new StateMap.Builder()).ignore(POWERED).build());

    }

    @Override
    public Item getItemBlock() {
        return itemBlock;
    }
}
