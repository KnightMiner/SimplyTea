package knightminer.simplytea.data.gen;

import com.mojang.datafixers.util.Pair;
import knightminer.simplytea.SimplyTea;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LootTableGenerator extends LootTableProvider {
	private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, Builder>>>,LootContextParamSet>> lootTables = Collections.singletonList(Pair.of(BlockLootTableGenerator::new, LootContextParamSets.BLOCK));

	public LootTableGenerator(DataGenerator dataGeneratorIn) {
		super(dataGeneratorIn);
	}

	@Override
	public String getName() {
		return "Simply Tea Loot Tables";
	}

	@Override
	protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
		return lootTables;
	}

	@Override
	protected void validate(Map<ResourceLocation,LootTable> map, ValidationContext validationtracker) {
		map.forEach((loc, table) -> LootTables.validate(validationtracker, loc, table));
		// Remove vanilla's tables, which we also loaded so we can redirect stuff to them.
		// This ensures the remaining generator logic doesn't write those to files.
		map.keySet().removeIf((loc) -> !loc.getNamespace().equals(SimplyTea.MOD_ID));
	}
}
