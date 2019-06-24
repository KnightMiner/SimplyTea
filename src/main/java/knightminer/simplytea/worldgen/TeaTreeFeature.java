package knightminer.simplytea.worldgen;

import com.mojang.datafixers.Dynamic;
import knightminer.simplytea.block.TeaSaplingBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
		if (random.nextInt(50) != 0) {
			return false;
		}
		BlockState down = world.getBlockState(pos.down());
		Block block = down.getBlock();
		if((block == Blocks.GRASS_BLOCK || Block.isDirt(block)) && world.getBlockState(pos).isAir(world, pos)) {
			TeaSaplingBlock.generateTree(world, pos, random);
			return true;
		}
		return false;
	}
}