package knightminer.simplytea.data.gen;

import knightminer.simplytea.data.SimplyTags;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.CookingRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;
import java.util.function.Consumer;

import static knightminer.simplytea.core.Registration.black_tea;
import static knightminer.simplytea.core.Registration.chorus_petal;
import static knightminer.simplytea.core.Registration.cup;
import static knightminer.simplytea.core.Registration.cup_cocoa;
import static knightminer.simplytea.core.Registration.cup_tea_black;
import static knightminer.simplytea.core.Registration.cup_tea_chai;
import static knightminer.simplytea.core.Registration.cup_tea_chorus;
import static knightminer.simplytea.core.Registration.cup_tea_floral;
import static knightminer.simplytea.core.Registration.cup_tea_green;
import static knightminer.simplytea.core.Registration.cup_tea_iced;
import static knightminer.simplytea.core.Registration.tea_fence;
import static knightminer.simplytea.core.Registration.tea_fence_gate;
import static knightminer.simplytea.core.Registration.tea_leaf;
import static knightminer.simplytea.core.Registration.tea_stick;
import static knightminer.simplytea.core.Registration.teabag;
import static knightminer.simplytea.core.Registration.teabag_black;
import static knightminer.simplytea.core.Registration.teabag_chorus;
import static knightminer.simplytea.core.Registration.teabag_floral;
import static knightminer.simplytea.core.Registration.teabag_green;
import static knightminer.simplytea.core.Registration.teapot;
import static knightminer.simplytea.core.Registration.teapot_frothed;
import static knightminer.simplytea.core.Registration.teapot_hot;
import static knightminer.simplytea.core.Registration.teapot_milk;
import static knightminer.simplytea.core.Registration.teapot_water;
import static knightminer.simplytea.core.Registration.unfired_cup;
import static knightminer.simplytea.core.Registration.unfired_teapot;

public class RecipeGenerator extends RecipeProvider {
	public RecipeGenerator(DataGenerator generatorIn) {
		super(generatorIn);
	}

	@Override
	public String getName() {
		return "Simply Tea Recipes";
	}

	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
		// ingredients
		CookingRecipeBuilder.cookingRecipe(Ingredient.fromItems(tea_leaf), black_tea, 0.35f, 200, IRecipeSerializer.SMOKING)
												.addCriterion("has_item", hasItem(tea_leaf))
												.build(consumer);

		// wood
		ShapedRecipeBuilder.shapedRecipe(tea_fence, 2)
											 .patternLine("sss").patternLine("sss")
											 .key('s', tea_stick)
											 .addCriterion("has_stick", hasItem(tea_stick))
											 .build(consumer);
		ShapedRecipeBuilder.shapedRecipe(tea_fence_gate)
											 .patternLine("sss").patternLine(" s ").patternLine("sss")
											 .key('s', tea_stick)
											 .addCriterion("has_stick", hasItem(tea_stick))
											 .build(consumer);

		// ceramics
		ShapedRecipeBuilder.shapedRecipe(unfired_cup, 2)
											 .patternLine("CBC").patternLine(" C ")
											 .key('C', Items.CLAY_BALL)
											 .key('B', Items.BONE_MEAL)
											 .addCriterion("has_item", hasItem(Items.CLAY_BALL))
											 .build(consumer);
		fire(consumer, unfired_cup, cup);
		ShapedRecipeBuilder.shapedRecipe(unfired_teapot)
											 .patternLine("CBC").patternLine("CC ")
											 .key('C', Items.CLAY_BALL)
											 .key('B', Items.BONE_MEAL)
											 .addCriterion("has_item", hasItem(Items.CLAY_BALL))
											 .build(consumer);
		fire(consumer, unfired_teapot, teapot);

		// teapots
		boil(consumer, teapot_water, teapot_hot);
		boil(consumer, teapot_milk, teapot_frothed);

		// teabags
		ShapedRecipeBuilder.shapedRecipe(teabag, 4)
											 .patternLine("  S").patternLine("PP ").patternLine("PP ")
											 .key('S', Items.STRING)
											 .key('P', Items.PAPER)
											 .addCriterion("has_floral", hasItem(Items.DANDELION))
											 .addCriterion("has_leaf", hasItem(tea_leaf))
											 .build(consumer);

		// basic tea
		addTeaWithBag(consumer, Items.DANDELION, teabag_floral, cup_tea_floral);
		addTeaWithBag(consumer, tea_leaf, teabag_green, cup_tea_green);
		addTeaWithBag(consumer, black_tea, teabag_black, cup_tea_black);
		addTeaWithBag(consumer, chorus_petal, teabag_chorus, cup_tea_chorus);

		// advanced tea
		addTea(consumer, cup_cocoa, Items.COCOA_BEANS, Items.COCOA_BEANS, teapot_frothed);
		addTea(consumer, cup_tea_chai, teabag_black, tea_stick, teapot_frothed);
		addHoney(consumer, cup_tea_chai);
		ShapelessRecipeBuilder.shapelessRecipe(cup_tea_iced)
													.addIngredient(cup)
													.addIngredient(teabag_green)
													.addIngredient(Items.APPLE)
													.addIngredient(SimplyTags.Items.ICE_CUBES)
													.addCriterion("has_ice", hasItem(SimplyTags.Items.ICE_CUBES))
													.build(consumer);
		addHoney(consumer, cup_tea_iced);
	}

	private static ResourceLocation suffix(IItemProvider item, String suffix) {
		ResourceLocation name = Objects.requireNonNull(item.asItem().getRegistryName());
		return new ResourceLocation(name.getNamespace(), name.getPath() + suffix);
	}

	/** Adds a recipe firing a raw clay item into a cooked one */
	private static void fire(Consumer<IFinishedRecipe> consumer, IItemProvider unfired, IItemProvider fired) {
		CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(unfired), fired, 0.35f, 300)
												.addCriterion("has_unfired", hasItem(unfired))
												.build(consumer);
	}

	/** Adds a recipe to boil a teapot */
	private static void boil(Consumer<IFinishedRecipe> consumer, IItemProvider cold, IItemProvider hot) {
		CookingRecipeBuilder.cookingRecipe(Ingredient.fromItems(cold), hot, 0.35f, 900, IRecipeSerializer.CAMPFIRE_COOKING)
												.addCriterion("has_unfired", hasItem(cold))
												.build(consumer, suffix(hot, "_campfire"));
		CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(cold), hot, 0.35f, 300)
												.addCriterion("has_unfired", hasItem(cold))
												.build(consumer, suffix(hot, "_smelting"));
	}

	/** Adds a recipe to pour tea */
	private static void addTea(Consumer<IFinishedRecipe> consumer, IItemProvider filledCup, IItemProvider... ingredients) {
		ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapelessRecipe(filledCup);
		builder.addIngredient(cup);
		for (IItemProvider ingredient : ingredients) {
			builder.addIngredient(ingredient);
		}
		builder.addCriterion("has_bag", hasItem(ingredients[0]));
		builder.build(consumer);
	}

	/** Creates a recipe to add honey to a tea */
	public static void addHoney(Consumer<IFinishedRecipe> consumer, IItemProvider tea) {
		ResourceLocation recipeId = suffix(tea, "_with_honey");

		// advancement builder just like vanilla
		Advancement.Builder builder = Advancement.Builder.builder();
		builder.withCriterion("has_item", hasItem(Items.HONEY_BOTTLE))
					 .withCriterion("has_the_recipe", RecipeUnlockedTrigger.create(recipeId))
					 .withParentId(new ResourceLocation("recipes/root"))
					 .withRewards(AdvancementRewards.Builder.recipe(recipeId))
					 .withRequirementsStrategy(IRequirementsStrategy.OR);
		ResourceLocation advancementId = new ResourceLocation(recipeId.getNamespace(), "recipes/" + Objects.requireNonNull(tea.asItem().getGroup()).getPath() + "/" + recipeId.getPath());

		// build final recipe
		consumer.accept(new ShapelessHoneyRecipe.FinishedRecipe(recipeId, "simplytea:with_honey", tea, Ingredient.fromItems(Items.HONEY_BOTTLE), advancementId, builder));
	}

	/** Adds a recipe to pour tea and make tea bags */
	private static void addTeaWithBag(Consumer<IFinishedRecipe> consumer, IItemProvider leaf, IItemProvider filledTeabag, IItemProvider filledCup) {
		ShapelessRecipeBuilder.shapelessRecipe(filledTeabag)
													.setGroup("simplytea:teabag")
													.addIngredient(teabag)
													.addIngredient(leaf)
													.addIngredient(leaf)
													.addCriterion("has_leaf", hasItem(leaf))
													.build(consumer);
		addTea(consumer, filledCup, filledTeabag, teapot_hot);
		addHoney(consumer, filledCup);
	}
}
