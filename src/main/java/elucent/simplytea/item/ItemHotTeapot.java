package elucent.simplytea.item;

import java.util.List;

import elucent.simplytea.SimplyTea;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class ItemHotTeapot extends ItemBase {
	public ItemHotTeapot(String name, boolean addToTab) {
		super(name, addToTab);
		this.setContainerItem(this);
		setMaxStackSize(1);
	}

	@Override
	public boolean hasContainerItem(ItemStack stack) {
		return true;
	}

	@Override
	public ItemStack getContainerItem(ItemStack stack) {
		ItemStack c = stack.copy();
		c.setItemDamage(Math.max(0, stack.getItemDamage() - 1));
		if(c.getItemDamage() == 0) {
			return new ItemStack(SimplyTea.teapot, 1);
		}
		return c;
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return stack.getMetadata() < 4;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return 1.0 - stack.getMetadata() / 4.0;
	}

	@Override
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(this, 1, new ModelResourceLocation(getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(this, 2, new ModelResourceLocation(getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(this, 3, new ModelResourceLocation(getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(this, 4, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

	@Override
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(I18n.format("simplytea.tooltip.boiling"));
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> stacks) {
		if(tab == this.getCreativeTab()) {
			stacks.add(new ItemStack(this, 1, 4));
		}
	}
}