package elucent.simplytea.item;

import elucent.simplytea.SimplyTea;
import elucent.simplytea.core.IModeledObject;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class ItemTeaCup extends ItemFood implements IModeledObject {
	public ItemTeaCup(String name, int food, float sat, boolean addToTab) {
		super(food, sat, false);
		setMaxStackSize(1);
		setRegistryName(SimplyTea.MODID + ":" + name);
		setUnlocalizedName(name);
		if(addToTab) {
			setCreativeTab(SimplyTea.tab);
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);
		playerIn.setActiveHand(handIn);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.DRINK;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 20;
	}

	@Override
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(this, 1, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return stack.getItemDamage() > 0;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		if(stack.getItemDamage() > 0) {
			return stack.getItemDamage() / 2.0;
		}
		return 0.0;
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase base) {
		ItemStack s = stack.copy();
		super.onItemUseFinish(stack, world, base);
		s.setItemDamage(stack.getItemDamage() + 1);
		if(s.getItemDamage() >= 2) {
			return new ItemStack(SimplyTea.cup, 1);
		}
		return s;
	}
}