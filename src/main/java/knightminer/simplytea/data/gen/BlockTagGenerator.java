package knightminer.simplytea.data.gen;

import knightminer.simplytea.SimplyTea;
import knightminer.simplytea.core.Registration;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockTagGenerator extends BlockTagsProvider {
	public BlockTagGenerator(DataGenerator generatorIn, ExistingFileHelper existing) {
		super(generatorIn, SimplyTea.MOD_ID, existing);
	}

	@Override
	public String getName() {
		return "Simply Tea Block Tags";
	}

	@Override
	protected void registerTags() {
		// tea saplings
		this.getOrCreateBuilder(BlockTags.FLOWER_POTS).add(Registration.potted_tea_sapling);
		this.getOrCreateBuilder(BlockTags.SAPLINGS).add(Registration.tea_sapling);
		// tea fences
		this.getOrCreateBuilder(BlockTags.WOODEN_FENCES).add(Registration.tea_fence);
		this.getOrCreateBuilder(Tags.Blocks.FENCES_WOODEN).add(Registration.tea_fence);
		this.getOrCreateBuilder(BlockTags.FENCE_GATES).add(Registration.tea_fence_gate);
		this.getOrCreateBuilder(Tags.Blocks.FENCE_GATES_WOODEN).add(Registration.tea_fence_gate);
	}
}
