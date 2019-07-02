package knightminer.simplytea.worldgen;

import com.mojang.datafixers.Dynamic;
import knightminer.simplytea.block.TeaSaplingBlock;
import knightminer.simplytea.core.Config;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;
import java.util.function.Function;

public class TeaTreeFeature extends Feature<NoFeatureConfig> {
	public TeaTreeFeature(Function<Dynamic<?>,? extends NoFeatureConfig> configFactory) {
		super(configFactory);
	}

	@Override
	public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random random, BlockPos pos, NoFeatureConfig config) {
		if (!Config.SERVER.tree.generate() || random.nextInt(100) != 0) {
			return false;
		}
		if(world.getBlockState(pos.down()).getBlock().isIn(BlockTags.DIRT_LIKE) && world.getBlockState(pos).isAir(world, pos)) {
			TeaSaplingBlock.generateTree(world, pos, random);
			return true;
		}
		return false;
	}
}