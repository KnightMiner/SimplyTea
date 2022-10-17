package knightminer.simplytea.data.gen;

import com.google.common.collect.Sets;
import knightminer.simplytea.SimplyTea;
import knightminer.simplytea.block.TeaTrunkBlock;
import knightminer.simplytea.core.Registration;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTable.Builder;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.TableLootEntry;
import net.minecraft.loot.conditions.BlockStateProperty;
import net.minecraft.loot.conditions.SurvivesExplosion;
import net.minecraft.loot.conditions.TableBonus;
import net.minecraft.loot.functions.ApplyBonus;
import net.minecraft.loot.functions.ExplosionDecay;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class BlockLootTableGenerator extends BlockLootTables {
	private final ResourceLocation LEAVES_ID = new ResourceLocation(SimplyTea.MOD_ID, "blocks/tea_leaves");

	@Nonnull
	@Override
	protected Iterable<Block> getKnownBlocks() {
		return ForgeRegistries.BLOCKS.getValues().stream()
																 .filter(block -> SimplyTea.MOD_ID.equals(Objects.requireNonNull(block.getRegistryName()).getNamespace()))
																 .collect(Collectors.toList());
	}

	@Override
	public void accept(BiConsumer<ResourceLocation, Builder> consumer) {
		this.addTables();
		Set<ResourceLocation> set = Sets.newHashSet();
		for (Block block : getKnownBlocks()) {
			ResourceLocation name = block.getLootTable();
			if (name != LootTables.EMPTY && set.add(name)) {
				LootTable.Builder builder = this.map.remove(name);
				if (builder == null) {
					throw new IllegalStateException(String.format("Missing loottable '%s' for '%s'", name, block.getRegistryName()));
				}

				consumer.accept(name, builder);
			}
		}

		// special case leaves builder
		LootTable.Builder leavesBuilder = this.map.remove(LEAVES_ID);
		if (leavesBuilder == null) {
			throw new IllegalStateException(String.format("Missing loottable '%s' for '%s'", LEAVES_ID, Registration.tea_tree.getRegistryName()));
		}
		consumer.accept(LEAVES_ID, leavesBuilder);

		if (!this.map.isEmpty()) {
			throw new IllegalStateException("Created block loot tables for non-blocks: " + this.map.keySet());
		}
	}
	@Override
	protected void addTables() {
		// basic
		dropSelf(Registration.tea_fence);
		dropSelf(Registration.tea_fence_gate);
		dropSelf(Registration.tea_sapling);
		dropPottedContents(Registration.potted_tea_sapling);
		// first register internal leaves loot table
		this.map.put(
				LEAVES_ID,
				LootTable.lootTable()
								 .apply(ExplosionDecay.explosionDecay())
								 .withPool(LootPool.lootPool().add(ItemLootEntry.lootTableItem(Registration.tea_leaf)))
								 .withPool(LootPool.lootPool()
																			.add(ItemLootEntry.lootTableItem(Registration.tea_leaf)
																														 .apply(ApplyBonus.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.55f, 2))))
								 .withPool(LootPool.lootPool()
																			.when(SurvivesExplosion.survivesExplosion())
																			.when(TableBonus.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 0.05f, 0.0625f, 0.083333336f, 0.1f))
																			.add(ItemLootEntry.lootTableItem(Registration.tea_sapling))));
		// then register the trunk table
		add(Registration.tea_trunk, block ->
				LootTable.lootTable()
								 .apply(ExplosionDecay.explosionDecay())
								 .withPool(LootPool.lootPool().add(ItemLootEntry.lootTableItem(Registration.tea_stick)))
								 .withPool(LootPool.lootPool()
																			.add(ItemLootEntry.lootTableItem(Registration.tea_stick)
																														 .apply(ApplyBonus.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.55f, 2))))
								 .withPool(LootPool.lootPool()
																			.when(BlockStateProperty.hasBlockStateProperties(block)
																																				 .setProperties(StatePropertiesPredicate.Builder.properties()
																																																												 .hasProperty(TeaTrunkBlock.CLIPPED, false)))
																			.add(TableLootEntry.lootTableReference(LEAVES_ID))));
	}
}
