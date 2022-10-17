package knightminer.simplytea.data.gen;

import com.mojang.datafixers.util.Pair;
import knightminer.simplytea.SimplyTea;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTable.Builder;
import net.minecraft.loot.LootTableManager;
import net.minecraft.loot.ValidationTracker;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LootTableGenerator extends LootTableProvider {
	private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, Builder>>>,LootParameterSet>> lootTables = Collections.singletonList(Pair.of(BlockLootTableGenerator::new, LootParameterSets.BLOCK));

	public LootTableGenerator(DataGenerator dataGeneratorIn) {
		super(dataGeneratorIn);
	}

	@Override
	public String getName() {
		return "Simply Tea Loot Tables";
	}

	@Override
	protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
		return lootTables;
	}

	@Override
	protected void validate(Map<ResourceLocation,LootTable> map, ValidationTracker validationtracker) {
		map.forEach((loc, table) -> LootTableManager.validate(validationtracker, loc, table));
		// Remove vanilla's tables, which we also loaded so we can redirect stuff to them.
		// This ensures the remaining generator logic doesn't write those to files.
		map.keySet().removeIf((loc) -> !loc.getNamespace().equals(SimplyTea.MOD_ID));
	}
}
