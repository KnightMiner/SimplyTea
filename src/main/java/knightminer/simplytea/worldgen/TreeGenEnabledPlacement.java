package knightminer.simplytea.worldgen;


import knightminer.simplytea.core.Config;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.SimplePlacement;

import java.util.Random;
import java.util.stream.Stream;

public class TreeGenEnabledPlacement extends SimplePlacement<NoPlacementConfig> {
	public TreeGenEnabledPlacement() {
		super(NoPlacementConfig.CODEC);
	}

	@Override
	public Stream<BlockPos> place(Random random, NoPlacementConfig config, BlockPos pos) {
		return Config.SERVER.tree.generate() ? Stream.of(pos) : Stream.empty();
	}
}