package knightminer.simplytea.data.gen;

import knightminer.simplytea.SimplyTea;
import knightminer.simplytea.core.Registration;
import knightminer.simplytea.data.SimplyTags;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemTagGenerator extends ItemTagsProvider {
	public ItemTagGenerator(DataGenerator dataGenerator, BlockTagsProvider blockTags, ExistingFileHelper existing) {
		super(dataGenerator, blockTags, SimplyTea.MOD_ID, existing);
	}

	@Override
	public String getName() {
		return "Simply Tea Item Tags";
	}

	@Override
	protected void registerTags() {
		this.getOrCreateBuilder(Tags.Items.RODS_WOODEN).add(Registration.tea_stick);
		this.getOrCreateBuilder(SimplyTags.Items.ICE_CUBES).add(Registration.ice_cube);
		this.getOrCreateBuilder(SimplyTags.Items.EXCLUSIVE_TEAS).add(
				Registration.cup_tea_green, Registration.cup_tea_black,
				Registration.cup_tea_iced, Registration.cup_tea_chai, Registration.cup_tea_chorus);
		this.getOrCreateBuilder(SimplyTags.Items.TEAS).add(Registration.cup_tea_floral).addTag(SimplyTags.Items.EXCLUSIVE_TEAS);

		// saplings
		copy(BlockTags.SAPLINGS, ItemTags.SAPLINGS);
		// fences
		copy(BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES);
		copy(Tags.Blocks.FENCES_WOODEN, Tags.Items.FENCES_WOODEN);
		copy(Tags.Blocks.FENCE_GATES_WOODEN, Tags.Items.FENCE_GATES_WOODEN);
	}
}
