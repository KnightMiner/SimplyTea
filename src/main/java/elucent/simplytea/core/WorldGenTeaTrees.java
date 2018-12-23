package elucent.simplytea.core;

import java.util.Random;

import elucent.simplytea.block.BlockTeaSapling;
import net.minecraft.block.BlockGrass;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenTeaTrees implements IWorldGenerator {
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if(random.nextInt(20) == 0) {
			int x = chunkX * 16 + 2 + random.nextInt(12);
			int z = chunkZ * 16 + 2 + random.nextInt(12);
			BlockPos p = new BlockPos(x, 0, z);
			p = world.getHeight(p);
			Biome b = world.getBiome(p);
			if(BiomeDictionary.hasType(b, BiomeDictionary.Type.FOREST) || b == Biomes.FOREST
					|| b == Biomes.FOREST_HILLS) {
				if(world.getBlockState(p.down()).getBlock() instanceof BlockGrass && world.isAirBlock(p)) {
					BlockTeaSapling.generateTree(world, p, Blocks.AIR.getDefaultState(), random);
				}
			}
		}
	}
}