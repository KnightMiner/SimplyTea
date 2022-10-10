package knightminer.simplytea.data.gen;

import knightminer.simplytea.SimplyTea;
import knightminer.simplytea.core.Registration;
import knightminer.simplytea.data.AddEntryLootModifier;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootContext.EntityTarget;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.LimitCount;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

public class LootModifierGenerator extends GlobalLootModifierProvider {

	public LootModifierGenerator(DataGenerator gen) {
		super(gen, SimplyTea.MOD_ID);
	}

	@Override
	protected void start() {
		// ice_cubes
		LootItemCondition[] iceCubesConditions = new LootItemCondition[] {
				LootTableIdCondition.builder(new ResourceLocation("minecraft:blocks/ice"))
					.or(LootTableIdCondition.builder(new ResourceLocation("minecraft:blocks/packed_ice")))
					.or(LootTableIdCondition.builder(new ResourceLocation("minecraft:blocks/blue_ice")))
					.build(),
				MatchTool.toolMatches(ItemPredicate.Builder.item().of(Tags.Items.TOOLS_PICKAXES)).build(),
				MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1)))).invert().build()
		};
		
		LootItemFunction[] iceCubesFunctions = new LootItemFunction[] {
				SetItemCountFunction.setCount(UniformGenerator.between(2, 4)).build(),
				ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE).build(),
				LimitCount.limitCount(IntRange.range(1, 4)).build()
		};
		
		AddEntryLootModifier iceCubesModifier = new AddEntryLootModifier(iceCubesConditions, Registration.ice_cube.get(), iceCubesFunctions, false);
		this.add("ice_cubes", iceCubesModifier);
		
		// chorus petals
		LootItemCondition[] petalsConditions = new LootItemCondition[] {
				LootTableIdCondition.builder(new ResourceLocation("minecraft:blocks/chorus_flower")).build(),
				ExplosionCondition.survivesExplosion().build(),
				LootItemEntityPropertyCondition.entityPresent(EntityTarget.THIS).invert().build()
		};
		
		LootItemFunction[] petalsFunctions = new LootItemFunction[] {
				SetItemCountFunction.setCount(UniformGenerator.between(1, 3)).build(),
		};
		
		AddEntryLootModifier petalsModifier = new AddEntryLootModifier(petalsConditions, Registration.chorus_petal.get(), petalsFunctions, false);
		this.add("chorus_petal", petalsModifier);
	}

}
