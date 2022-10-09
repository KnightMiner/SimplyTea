package knightminer.simplytea.data.gen;

import com.google.common.collect.Sets;
import knightminer.simplytea.SimplyTea;
import knightminer.simplytea.block.TeaTrunkBlock;
import knightminer.simplytea.core.Registration;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class BlockLootTableGenerator extends BlockLoot {
	private final ResourceLocation LEAVES_ID = new ResourceLocation(SimplyTea.MOD_ID, "blocks/tea_leaves");

	@Nonnull
	@Override
	protected Iterable<Block> getKnownBlocks() {
		return ForgeRegistries.BLOCKS.getValues().stream()
				.filter(block -> SimplyTea.MOD_ID.equals(ForgeRegistries.BLOCKS.getKey(block).getNamespace()))
				.collect(Collectors.toList());
	}

	@Override
	public void accept(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
		this.addTables();
		Set<ResourceLocation> set = Sets.newHashSet();
		for (Block block : getKnownBlocks()) {
			ResourceLocation name = block.getLootTable();
			if (name != LootTable.EMPTY.getLootTableId() && set.add(name)) {
				LootTable.Builder builder = this.map.remove(name);
				if (builder == null) {
					ResourceLocation blockName = ForgeRegistries.BLOCKS.getKey(block);
					throw new IllegalStateException(String.format("Missing loottable '%s' for '%s'", name, blockName));
				}

				consumer.accept(name, builder);
			}
		}

		// special case leaves builder
		LootTable.Builder leavesBuilder = this.map.remove(LEAVES_ID);
		if (leavesBuilder == null) {
			throw new IllegalStateException(String.format("Missing loottable '%s' for '%s'", LEAVES_ID, Registration.tea_tree.getKey().location()));
		}
		consumer.accept(LEAVES_ID, leavesBuilder);

		if (!this.map.isEmpty()) {
			throw new IllegalStateException("Created block loot tables for non-blocks: " + this.map.keySet());
		}
	}
	@Override
	protected void addTables() {
		// basic
		dropSelf(Registration.tea_fence.get());
		dropSelf(Registration.tea_fence_gate.get());
		dropSelf(Registration.tea_sapling.get());
		dropPottedContents(Registration.potted_tea_sapling.get());
		// first register internal leaves loot table
		this.map.put(
				LEAVES_ID,
				LootTable.lootTable()
								 .apply(ApplyExplosionDecay.explosionDecay())
								 .withPool(LootPool.lootPool().add(LootItem.lootTableItem(Registration.tea_leaf.get())))
								 .withPool(LootPool.lootPool()
																			.add(LootItem.lootTableItem(Registration.tea_leaf.get())
																														 .apply(ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.55f, 2))))
								 .withPool(LootPool.lootPool()
																			.when(ExplosionCondition.survivesExplosion())
																			.when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 0.05f, 0.0625f, 0.083333336f, 0.1f))
																			.add(LootItem.lootTableItem(Registration.tea_sapling.get()))));
		// then register the trunk table
		add(Registration.tea_trunk.get(), block ->
				LootTable.lootTable()
								 .apply(ApplyExplosionDecay.explosionDecay())
								 .withPool(LootPool.lootPool().add(LootItem.lootTableItem(Registration.tea_stick.get())))
								 .withPool(LootPool.lootPool()
																			.add(LootItem.lootTableItem(Registration.tea_stick.get())
																														 .apply(ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.55f, 2))))
								 .withPool(LootPool.lootPool()
																			.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
																																				 .setProperties(StatePropertiesPredicate.Builder.properties()
																																						 .hasProperty(TeaTrunkBlock.CLIPPED, false)))
																			.add(LootTableReference.lootTableReference(LEAVES_ID))));
	}
}
