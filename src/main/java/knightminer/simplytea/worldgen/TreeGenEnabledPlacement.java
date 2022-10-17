package knightminer.simplytea.worldgen;


import knightminer.simplytea.core.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;
import net.minecraft.world.level.levelgen.placement.SimpleFeatureDecorator;

import java.util.Random;
import java.util.stream.Stream;

public class TreeGenEnabledPlacement extends SimpleFeatureDecorator<NoneDecoratorConfiguration> {
	public TreeGenEnabledPlacement() {
		super(NoneDecoratorConfiguration.CODEC);
	}

	@Override
	public Stream<BlockPos> place(Random random, NoneDecoratorConfiguration config, BlockPos pos) {
		return Config.SERVER.tree.generate() ? Stream.of(pos) : Stream.empty();
	}
}