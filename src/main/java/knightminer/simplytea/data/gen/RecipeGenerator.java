package knightminer.simplytea.data.gen;

import knightminer.simplytea.data.SimplyTags;
import knightminer.simplytea.item.CocoaItem;
import knightminer.simplytea.item.TeaCupItem;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;
import java.util.function.Consumer;

import static knightminer.simplytea.core.Registration.black_tea;
import static knightminer.simplytea.core.Registration.chorus_petal;
import static knightminer.simplytea.core.Registration.cup;
import static knightminer.simplytea.core.Registration.cup_cocoa;
import static knightminer.simplytea.core.Registration.cup_frothed;
import static knightminer.simplytea.core.Registration.cup_tea_black;
import static knightminer.simplytea.core.Registration.cup_tea_chai;
import static knightminer.simplytea.core.Registration.cup_tea_chorus;
import static knightminer.simplytea.core.Registration.cup_tea_floral;
import static knightminer.simplytea.core.Registration.cup_tea_green;
import static knightminer.simplytea.core.Registration.cup_tea_iced;
import static knightminer.simplytea.core.Registration.cup_water_hot;
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
	public RecipeGenerator(PackOutput output) {
		super(output);
	}

	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
		// ingredients
		SimpleCookingRecipeBuilder.smoking(Ingredient.of(SimplyTags.Items.TEA_CROP), RecipeCategory.FOOD, black_tea, 0.35f, 200)
												.unlockedBy("has_item", has(SimplyTags.Items.TEA_CROP))
												.save(consumer);

		// wood
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, tea_fence, 2)
											 .pattern("sss").pattern("sss")
											 .define('s', tea_stick)
											 .unlockedBy("has_stick", has(tea_stick))
											 .save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, tea_fence_gate)
											 .pattern("sss").pattern(" s ").pattern("sss")
											 .define('s', tea_stick)
											 .unlockedBy("has_stick", has(tea_stick))
											 .save(consumer);

		// ceramics
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, unfired_cup, 2)
											 .pattern("CBC").pattern(" C ")
											 .define('C', Items.CLAY_BALL)
											 .define('B', Items.BONE_MEAL)
											 .unlockedBy("has_item", has(Items.CLAY_BALL))
											 .save(consumer);
		fire(consumer, unfired_cup, cup);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, unfired_teapot)
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
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, teabag, 4)
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
		addTea(consumer, cup_water_hot, "teapot", teapot_hot);

		// advanced tea
		addTea(consumer, cup_frothed, "teapot", teapot_frothed);
		addHoney(consumer, RecipeCategory.FOOD, cup_frothed, tea_stick, CocoaItem.CINNAMON_TAG);
		addTea(consumer, cup_cocoa, "teapot", Items.COCOA_BEANS, Items.COCOA_BEANS, teapot_frothed);
		addTea(consumer, cup_cocoa, "cup", Items.COCOA_BEANS, Items.COCOA_BEANS, cup_frothed);
		addHoney(consumer, RecipeCategory.FOOD, cup_cocoa, tea_stick, CocoaItem.CINNAMON_TAG);
		addTea(consumer, cup_tea_chai, "teapot", teabag_black, tea_stick, teapot_frothed);
		addTea(consumer, cup_tea_chai, "cup", teabag_black, tea_stick, cup_frothed);
		addHoney(consumer, cup_tea_chai);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, cup_tea_iced)
													.requires(cup)
													.requires(teabag_green)
													.requires(Items.APPLE)
													.requires(SimplyTags.Items.ICE_CUBES)
													.unlockedBy("has_ice", has(SimplyTags.Items.ICE_CUBES))
													.save(consumer);
		addHoney(consumer, cup_tea_iced);
	}

	/** Suffixes the item ID location with the given text */
	private static ResourceLocation suffix(ItemLike item, String suffix) {
		ResourceLocation name = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item.asItem()));
		return new ResourceLocation(name.getNamespace(), name.getPath() + suffix);
	}

	/** Adds a recipe firing a raw clay item into a cooked one */
	private static void fire(Consumer<FinishedRecipe> consumer, ItemLike unfired, ItemLike fired) {
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(unfired), RecipeCategory.MISC, fired, 0.35f, 300)
												.unlockedBy("has_unfired", has(unfired))
												.save(consumer);
	}

	/** Adds a recipe to boil a teapot */
	private static void boil(Consumer<FinishedRecipe> consumer, ItemLike cold, ItemLike hot) {
		SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(cold), RecipeCategory.MISC, hot, 0.35f, 900)
												.unlockedBy("has_unfired", has(cold))
												.save(consumer, suffix(hot, "_campfire"));
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(cold), RecipeCategory.MISC, hot, 0.35f, 300)
												.unlockedBy("has_unfired", has(cold))
												.save(consumer, suffix(hot, "_smelting"));
	}

	/** Adds a recipe to pour tea */
	private static void addTea(Consumer<FinishedRecipe> consumer, ItemLike filledCup, String suffix, ItemLike... ingredients) {
		ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, filledCup);
		builder.requires(cup);
		for (ItemLike ingredient : ingredients) {
			builder.requires(ingredient);
		}
		builder.unlockedBy("has_bag", has(ingredients[0]));
		builder.save(consumer, suffix(filledCup, "_from_" + suffix).toString());
	}

	/** Creates a recipe to add honey to a tea */
	public static void addHoney(Consumer<FinishedRecipe> consumer, ItemLike tea) {
		addHoney(consumer, RecipeCategory.FOOD, tea, Items.HONEY_BOTTLE, TeaCupItem.HONEY_TAG);
	}

	/** Creates a recipe to "honey" to a tea */
	public static void addHoney(Consumer<FinishedRecipe> consumer, RecipeCategory category, ItemLike tea, ItemLike honey, String tag) {
		ResourceLocation recipeId = suffix(tea, "_" + tag);

		// advancement builder just like vanilla
		Advancement.Builder builder = Advancement.Builder.advancement();
		builder.addCriterion("has_item", has(honey))
					 .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
					 .parent(new ResourceLocation("recipes/root"))
					 .rewards(AdvancementRewards.Builder.recipe(recipeId))
					 .requirements(RequirementsStrategy.OR);
		ResourceLocation advancementId = new ResourceLocation(recipeId.getNamespace(), "recipes/" + category.getFolderName() + "/" + recipeId.getPath());

		// build final recipe
		consumer.accept(new ShapelessHoneyRecipe.Finished(recipeId, "simplytea:" + tag, tea, Ingredient.of(honey), tag, advancementId, builder));
	}

	/** Adds a recipe to pour tea and make tea bags */
	private static void addTeaWithBag(Consumer<FinishedRecipe> consumer, ItemLike leaf, ItemLike filledTeabag, ItemLike filledCup) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, filledTeabag)
													.group("simplytea:teabag")
													.requires(teabag)
													.requires(leaf)
													.requires(leaf)
													.unlockedBy("has_leaf", has(leaf))
													.save(consumer);
		addTea(consumer, filledCup, "teapot", filledTeabag, teapot_hot);
		addTea(consumer, filledCup, "cup", filledTeabag, cup_water_hot);
		addHoney(consumer, filledCup);
	}
}
