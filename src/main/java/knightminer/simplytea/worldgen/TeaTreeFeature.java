package knightminer.simplytea.worldgen;

import com.mojang.datafixers.Dynamic;
import knightminer.simplytea.block.TeaSaplingBlock;
import knightminer.simplytea.core.Config;
import knightminer.simplytea.core.Registration;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
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
		BlockPos down = pos.down();
		BlockState soil = world.getBlockState(down);
		if(soil.canSustainPlant(world, down, Direction.UP, Registration.tea_sapling) && world.getBlockState(pos).isAir(world, pos)) {
			TeaSaplingBlock.generateTree(world, pos, random);
			return true;
		}
		return false;
	}
}