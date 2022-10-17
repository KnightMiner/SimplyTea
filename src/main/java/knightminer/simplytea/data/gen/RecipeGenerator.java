package knightminer.simplytea.data.gen;

import knightminer.simplytea.data.SimplyTags;
import knightminer.simplytea.item.CocoaItem;
import knightminer.simplytea.item.TeaCupItem;
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
	protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
		// ingredients
		CookingRecipeBuilder.cooking(Ingredient.of(SimplyTags.Items.TEA_CROP), black_tea, 0.35f, 200, IRecipeSerializer.SMOKING_RECIPE)
												.unlockedBy("has_item", has(SimplyTags.Items.TEA_CROP))
												.save(consumer);

		// wood
		ShapedRecipeBuilder.shaped(tea_fence, 2)
											 .pattern("sss").pattern("sss")
											 .define('s', tea_stick)
											 .unlockedBy("has_stick", has(tea_stick))
											 .save(consumer);
		ShapedRecipeBuilder.shaped(tea_fence_gate)
											 .pattern("sss").pattern(" s ").pattern("sss")
											 .define('s', tea_stick)
											 .unlockedBy("has_stick", has(tea_stick))
											 .save(consumer);

		// ceramics
		ShapedRecipeBuilder.shaped(unfired_cup, 2)
											 .pattern("CBC").pattern(" C ")
											 .define('C', Items.CLAY_BALL)
											 .define('B', Items.BONE_MEAL)
											 .unlockedBy("has_item", has(Items.CLAY_BALL))
											 .save(consumer);
		fire(consumer, unfired_cup, cup);
		ShapedRecipeBuilder.shaped(unfired_teapot)
											 .pattern("CBC").pattern("CC ")
											 .define('C', Items.CLAY_BALL)
											 .define('B', Items.BONE_MEAL)
											 .unlockedBy("has_item", has(Items.CLAY_BALL))
											 .save(consumer);
		fire(consumer, unfired_teapot, teapot);

		// teapots
		boil(consumer, teapot_water, teapot_hot);
		boil(consumer, teapot_milk, teapot_frothed);

		// teabags
		ShapedRecipeBuilder.shaped(teabag, 4)
											 .pattern("  S").pattern("PP ").pattern("PP ")
											 .define('S', Items.STRING)
											 .define('P', Items.PAPER)
											 .unlockedBy("has_floral", has(Items.DANDELION))
											 .unlockedBy("has_leaf", has(tea_leaf))
											 .save(consumer);

		// basic tea
		addTeaWithBag(consumer, Items.DANDELION, teabag_floral, cup_tea_floral);
		addTeaWithBag(consumer, tea_leaf, teabag_green, cup_tea_green);
		addTeaWithBag(consumer, black_tea, teabag_black, cup_tea_black);
		addTeaWithBag(consumer, chorus_petal, teabag_chorus, cup_tea_chorus);

		// advanced tea
		addTea(consumer, cup_cocoa, Items.COCOA_BEANS, Items.COCOA_BEANS, teapot_frothed);
		addHoney(consumer, cup_cocoa, tea_stick, CocoaItem.CINNAMON_TAG);
		addTea(consumer, cup_tea_chai, teabag_black, tea_stick, teapot_frothed);
		addHoney(consumer, cup_tea_chai);
		ShapelessRecipeBuilder.shapeless(cup_tea_iced)
													.requires(cup)
													.requires(teabag_green)
													.requires(Items.APPLE)
													.requires(SimplyTags.Items.ICE_CUBES)
													.unlockedBy("has_ice", has(SimplyTags.Items.ICE_CUBES))
													.save(consumer);
		addHoney(consumer, cup_tea_iced);
	}

	/** Suffixes the item ID location with the given text */
	private static ResourceLocation suffix(IItemProvider item, String suffix) {
		ResourceLocation name = Objects.requireNonNull(item.asItem().getRegistryName());
		return new ResourceLocation(name.getNamespace(), name.getPath() + suffix);
	}

	/** Adds a recipe firing a raw clay item into a cooked one */
	private static void fire(Consumer<IFinishedRecipe> consumer, IItemProvider unfired, IItemProvider fired) {
		CookingRecipeBuilder.smelting(Ingredient.of(unfired), fired, 0.35f, 300)
												.unlockedBy("has_unfired", has(unfired))
												.save(consumer);
	}

	/** Adds a recipe to boil a teapot */
	private static void boil(Consumer<IFinishedRecipe> consumer, IItemProvider cold, IItemProvider hot) {
		CookingRecipeBuilder.cooking(Ingredient.of(cold), hot, 0.35f, 900, IRecipeSerializer.CAMPFIRE_COOKING_RECIPE)
												.unlockedBy("has_unfired", has(cold))
												.save(consumer, suffix(hot, "_campfire"));
		CookingRecipeBuilder.smelting(Ingredient.of(cold), hot, 0.35f, 300)
												.unlockedBy("has_unfired", has(cold))
												.save(consumer, suffix(hot, "_smelting"));
	}

	/** Adds a recipe to pour tea */
	private static void addTea(Consumer<IFinishedRecipe> consumer, IItemProvider filledCup, IItemProvider... ingredients) {
		ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapeless(filledCup);
		builder.requires(cup);
		for (IItemProvider ingredient : ingredients) {
			builder.requires(ingredient);
		}
		builder.unlockedBy("has_bag", has(ingredients[0]));
		builder.save(consumer);
	}

	/** Creates a recipe to add honey to a tea */
	public static void addHoney(Consumer<IFinishedRecipe> consumer, IItemProvider tea) {
		addHoney(consumer, tea, Items.HONEY_BOTTLE, TeaCupItem.HONEY_TAG);
	}

	/** Creates a recipe to "honey" to a tea */
	public static void addHoney(Consumer<IFinishedRecipe> consumer, IItemProvider tea, IItemProvider honey, String tag) {
		ResourceLocation recipeId = suffix(tea, "_" + tag);

		// advancement builder just like vanilla
		Advancement.Builder builder = Advancement.Builder.advancement();
		builder.addCriterion("has_item", has(honey))
					 .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
					 .parent(new ResourceLocation("recipes/root"))
					 .rewards(AdvancementRewards.Builder.recipe(recipeId))
					 .requirements(IRequirementsStrategy.OR);
		ResourceLocation advancementId = new ResourceLocation(recipeId.getNamespace(), "recipes/" + Objects.requireNonNull(tea.asItem().getItemCategory()).getRecipeFolderName() + "/" + recipeId.getPath());

		// build final recipe
		consumer.accept(new ShapelessHoneyRecipe.FinishedRecipe(recipeId, "simplytea:" + tag, tea, Ingredient.of(honey), tag, advancementId, builder));
	}

	/** Adds a recipe to pour tea and make tea bags */
	private static void addTeaWithBag(Consumer<IFinishedRecipe> consumer, IItemProvider leaf, IItemProvider filledTeabag, IItemProvider filledCup) {
		ShapelessRecipeBuilder.shapeless(filledTeabag)
													.group("simplytea:teabag")
													.requires(teabag)
													.requires(leaf)
													.requires(leaf)
													.unlockedBy("has_leaf", has(leaf))
													.save(consumer);
		addTea(consumer, filledCup, filledTeabag, teapot_hot);
		addHoney(consumer, filledCup);
	}
}
