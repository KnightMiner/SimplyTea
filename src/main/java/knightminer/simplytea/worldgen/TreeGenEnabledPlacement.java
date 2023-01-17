package knightminer.simplytea.worldgen;


import com.mojang.serialization.Codec;
import knightminer.simplytea.core.Config;
import knightminer.simplytea.core.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public class TreeGenEnabledPlacement extends PlacementFilter {
	public static final TreeGenEnabledPlacement INSTANCE = new TreeGenEnabledPlacement();
	public static Codec<TreeGenEnabledPlacement> CODEC = Codec.unit(() -> INSTANCE);

	private TreeGenEnabledPlacement() {}

	@Override
	protected boolean shouldPlace(PlacementContext pContext, RandomSource pRandom, BlockPos pPos) {
		return Config.SERVER.tree.generate();
	}

	@Override
	public PlacementModifierType<?> type() {
		return Registration.tree_gen_enabled;
	}
}