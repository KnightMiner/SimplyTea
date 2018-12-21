package elucent.simplytea.item;

import elucent.simplytea.IModeledObject;
import elucent.simplytea.SimplyTea;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class ItemBase extends Item implements IModeledObject {
	public ItemBase(String name, boolean addToTab) {
		setRegistryName(new ResourceLocation(SimplyTea.MODID , name));
		setUnlocalizedName(name);
		if(addToTab) {
			setCreativeTab(SimplyTea.tab);
		}
	}

	@Override
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}