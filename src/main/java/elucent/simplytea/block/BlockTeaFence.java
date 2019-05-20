package elucent.simplytea.block;

import elucent.simplytea.SimplyTea;
import elucent.simplytea.core.IModeledObject;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class BlockTeaFence extends BlockFence implements IModeledObject, IItemBlock {

    private Item itemBlock = null;
    public BlockTeaFence(String name) {
        super(Material.WOOD, BlockPlanks.EnumType.DARK_OAK.getMapColor());

        setUnlocalizedName(name);
        setRegistryName(new ResourceLocation(SimplyTea.MODID, name));
        this.setCreativeTab(SimplyTea.tab);
        itemBlock = (new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    @Override
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
                new ModelResourceLocation(getRegistryName().toString(), "inventory"));
    }

    @Override
    public Item getItemBlock() {
        return itemBlock;
    }
}
