package elucent.simplytea.item;

import elucent.simplytea.SimplyTea;
import elucent.simplytea.core.Config;
import elucent.simplytea.core.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemTeapot extends ItemBase {
	public ItemTeapot(String name) {
		super(name, true);
		setMaxStackSize(1);
        this.setHasSubtypes(true);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if(stack.getMetadata() == 0) {
			RayTraceResult trace = this.rayTrace(world, player, true);
			if (trace != null && trace.typeOfHit == Type.BLOCK) {
				BlockPos pos = trace.getBlockPos();
				IBlockState state = world.getBlockState(pos);
				Block block = state.getBlock();

				// try filling from the cauldron
				if (Config.teapot.fill_from_cauldron && block == Blocks.CAULDRON && state.getValue(BlockCauldron.LEVEL) == 3) {
					stack.setItemDamage(1);
					player.setHeldItem(hand, stack);
					Blocks.CAULDRON.setWaterLevel(world, pos, state, 0);
					return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
				}

				// we use name for lookup to prevent default fluid conflicts
				Fluid fluid = Util.getFluid(state);
				if(fluid != null) {
					// try for water or milk using the config lists
					String name = fluid.getName();
					int meta = 0;
					if (Config.teapot.waterSet.contains(name)) {
						meta = 1;
					} else if(Config.teapot.milkSet.contains(name)) {
						meta = 2;
					}

					// if either one is found, update the stack
					if(meta > 0) {
						// water is considered infinite unless disabled in the config
						if(!(Config.teapot.infinite_water && fluid == FluidRegistry.WATER)) {
							world.setBlockToAir(pos);
						}

						// update the item
						stack.setItemDamage(meta);
						player.setHeldItem(hand, stack);
						player.playSound(fluid.getFillSound(), 1.0f, 1.0f);
						return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
					}
				}
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
		// only work if the teapot is empty and right clicking a cow
		if(Config.teapot.milk_cow && stack.getMetadata() == 0 && target instanceof EntityCow && !player.capabilities.isCreativeMode) {
			// sound
			player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
			// fill with milk
			stack.setItemDamage(2);
			return true;
		}
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void initModel() {
		String name = getRegistryName().getPath();
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(this, 1, new ModelResourceLocation(new ResourceLocation(SimplyTea.MODID, name + "_water"), "inventory"));
		ModelLoader.setCustomModelResourceLocation(this, 2, new ModelResourceLocation(new ResourceLocation(SimplyTea.MODID, name + "_milk"), "inventory"));
	}

	@Override
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
		switch(stack.getMetadata()) {
		case 0:
			tooltip.add(I18n.format(this.getTranslationKey() + ".tooltip.empty"));
			break;
		case 1:
			tooltip.add(I18n.format(this.getTranslationKey() + ".tooltip.water"));
			break;
		case 2:
			tooltip.add(I18n.format(this.getTranslationKey() + ".tooltip.milk"));
			break;
		}
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> stacks) {
		if(this.isInCreativeTab(tab)) {
			stacks.add(new ItemStack(this, 1));
			stacks.add(new ItemStack(this, 1, 1));
			stacks.add(new ItemStack(this, 1, 2));
		}
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return new TeapotFluidHandler(stack);
	}
}