package elucent.simplytea.item;

import elucent.simplytea.SimplyTea;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemHotTeapot extends ItemBase {
	public ItemHotTeapot(String name) {
		super(name, true);
		this.setContainerItem(this);
		this.setMaxStackSize(1);
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
	@SideOnly(Side.CLIENT)
	public void initModel() {
		for (int i = 0; i <= 4; i++) {
			ModelLoader.setCustomModelResourceLocation(this, i, new ModelResourceLocation(getRegistryName(), "inventory"));
		}
	}

	@Override
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(I18n.format(this.getTranslationKey() + ".tooltip"));
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> stacks) {
		if(this.isInCreativeTab(tab)) {
			stacks.add(new ItemStack(this, 1, 4));
		}
	}
}