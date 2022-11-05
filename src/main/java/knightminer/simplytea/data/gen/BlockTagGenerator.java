package knightminer.simplytea.data.gen;

import knightminer.simplytea.SimplyTea;
import knightminer.simplytea.core.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
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
	protected void addTags() {
		// tea saplings
		this.tag(BlockTags.FLOWER_POTS).add(Registration.potted_tea_sapling.get());
		this.tag(BlockTags.SAPLINGS).add(Registration.tea_sapling.get());
		// tea fences
		this.tag(BlockTags.WOODEN_FENCES).add(Registration.tea_fence.get());
		this.tag(Tags.Blocks.FENCES_WOODEN).add(Registration.tea_fence.get());
		this.tag(BlockTags.FENCE_GATES).add(Registration.tea_fence_gate.get());
		this.tag(Tags.Blocks.FENCE_GATES_WOODEN).add(Registration.tea_fence_gate.get());

		this.tag(BlockTags.MINEABLE_WITH_AXE).add(Registration.tea_trunk.get(), Registration.tea_fence.get(), Registration.tea_fence_gate.get());
	}
}
