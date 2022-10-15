package knightminer.simplytea.block;

import java.util.Random;

import knightminer.simplytea.core.Registration;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class TeaTreeGrower extends AbstractTreeGrower {

	@Override
	protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(Random pRandom, boolean pLargeHive) {
		return Registration.configured_tea_tree.getHolder().get();
	}

}
