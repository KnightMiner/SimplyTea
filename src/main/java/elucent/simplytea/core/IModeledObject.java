package elucent.simplytea.core;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IModeledObject {
	ResourceLocation getRegistryName();

	@SideOnly(Side.CLIENT)
	default void initModel() {
		Item item;
		if(this instanceof Block) {
			item = Item.getItemFromBlock((Block)this);
		} else {
			item = (Item) this;
		}
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(getRegistryName(), "inventory"));

	}
}