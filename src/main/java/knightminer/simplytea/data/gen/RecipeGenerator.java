package knightminer.simplytea.data.gen;

import knightminer.simplytea.data.SimplyTags;
import knightminer.simplytea.item.CocoaItem;
import knightminer.simplytea.item.TeaCupItem;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

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
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
		// ingredients
		SimpleCookingRecipeBuilder.cooking(Ingredient.of(SimplyTags.Items.TEA_CROP), black_tea.get(), 0.35f, 200, RecipeSerializer.SMOKING_RECIPE)
												.unlockedBy("has_item", has(SimplyTags.Items.TEA_CROP))
												.save(consumer);

		// wood
		ShapedRecipeBuilder.shaped(tea_fence.get(), 2)
											 .pattern("sss").pattern("sss")
											 .define('s', tea_stick.get())
											 .unlockedBy("has_stick", has(tea_stick.get()))
											 .save(consumer);
		ShapedRecipeBuilder.shaped(tea_fence_gate.get())
											 .pattern("sss").pattern(" s ").pattern("sss")
											 .define('s', tea_stick.get())
											 .unlockedBy("has_stick", has(tea_stick.get()))
											 .save(consumer);

		// ceramics
		ShapedRecipeBuilder.shaped(unfired_cup.get(), 2)
											 .pattern("CBC").pattern(" C ")
											 .define('C', Items.CLAY_BALL)
											 .define('B', Items.BONE_MEAL)
											 .unlockedBy("has_item", has(Items.CLAY_BALL))
											 .save(consumer);
		fire(consumer, unfired_cup.get(), cup.get());
		ShapedRecipeBuilder.shaped(unfired_teapot.get())
											 .pattern("CBC").pattern("CC ")
											 .define('C', Items.CLAY_BALL)
											 .define('B', Items.BONE_MEAL)
											 .unlockedBy("has_item", has(Items.CLAY_BALL))
											 .save(consumer);
		fire(consumer, unfired_teapot.get(), teapot.get());

		// teapots
		boil(consumer, teapot_water.get(), teapot_hot.get());
		boil(consumer, teapot_milk.get(), teapot_frothed.get());

		// teabags
		ShapedRecipeBuilder.shaped(teabag.get(), 4)
											 .pattern("  S").pattern("PP ").pattern("PP ")
											 .define('S', Items.STRING)
											 .define('P', Items.PAPER)
											 .unlockedBy("has_floral", has(Items.DANDELION))
											 .unlockedBy("has_leaf", has(tea_leaf.get()))
											 .save(consumer);

		// basic tea
		addTeaWithBag(consumer, Items.DANDELION, teabag_floral.get(), cup_tea_floral.get());
		addTeaWithBag(consumer, tea_leaf.get(), teabag_green.get(), cup_tea_green.get());
		addTeaWithBag(consumer, black_tea.get(), teabag_black.get(), cup_tea_black.get());
		addTeaWithBag(consumer, chorus_petal.get(), teabag_chorus.get(), cup_tea_chorus.get());

		// advanced tea
		addTea(consumer, cup_cocoa.get(), Items.COCOA_BEANS, Items.COCOA_BEANS, teapot_frothed.get());
		addHoney(consumer, cup_cocoa.get(), tea_stick.get(), CocoaItem.CINNAMON_TAG);
		addTea(consumer, cup_tea_chai.get(), teabag_black.get(), tea_stick.get(), teapot_frothed.get());
		addHoney(consumer, cup_tea_chai.get());
		ShapelessRecipeBuilder.shapeless(cup_tea_iced.get())
													.requires(cup.get())
													.requires(teabag_green.get())
													.requires(Items.APPLE)
													.requires(SimplyTags.Items.ICE_CUBES)
													.unlockedBy("has_ice", has(SimplyTags.Items.ICE_CUBES))
													.save(consumer);
		addHoney(consumer, cup_tea_iced.get());
	}

	/** Suffixes the item ID location with the given text */
	private static ResourceLocation suffix(ItemLike item, String suffix) {
		ResourceLocation name = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item.asItem()));
		return new ResourceLocation(name.getNamespace(), name.getPath() + suffix);
	}

	/** Adds a recipe firing a raw clay item into a cooked one */
	private static void fire(Consumer<FinishedRecipe> consumer, ItemLike unfired, ItemLike fired) {
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(unfired), fired, 0.35f, 300)
												.unlockedBy("has_unfired", has(unfired))
												.save(consumer);
	}

	/** Adds a recipe to boil a teapot */
	private static void boil(Consumer<FinishedRecipe> consumer, ItemLike cold, ItemLike hot) {
		SimpleCookingRecipeBuilder.cooking(Ingredient.of(cold), hot, 0.35f, 900, RecipeSerializer.CAMPFIRE_COOKING_RECIPE)
												.unlockedBy("has_unfired", has(cold))
												.save(consumer, suffix(hot, "_campfire"));
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(cold), hot, 0.35f, 300)
												.unlockedBy("has_unfired", has(cold))
												.save(consumer, suffix(hot, "_smelting"));
	}

	/** Adds a recipe to pour tea */
	private static void addTea(Consumer<FinishedRecipe> consumer, ItemLike filledCup, ItemLike... ingredients) {
		ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapeless(filledCup);
		builder.requires(cup.get());
		for (ItemLike ingredient : ingredients) {
			builder.requires(ingredient);
		}
		builder.unlockedBy("has_bag", has(ingredients[0]));
		builder.save(consumer);
	}

	/** Creates a recipe to add honey to a tea */
	public static void addHoney(Consumer<FinishedRecipe> consumer, ItemLike tea) {
		addHoney(consumer, tea, Items.HONEY_BOTTLE, TeaCupItem.HONEY_TAG);
	}

	/** Creates a recipe to "honey" to a tea */
	public static void addHoney(Consumer<FinishedRecipe> consumer, ItemLike tea, ItemLike honey, String tag) {
		ResourceLocation recipeId = suffix(tea, "_" + tag);

		// advancement builder just like vanilla
		Advancement.Builder builder = Advancement.Builder.advancement();
		builder.addCriterion("has_item", has(honey))
					 .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
					 .parent(new ResourceLocation("recipes/root"))
					 .rewards(AdvancementRewards.Builder.recipe(recipeId))
					 .requirements(RequirementsStrategy.OR);
		ResourceLocation advancementId = new ResourceLocation(recipeId.getNamespace(), "recipes/" + Objects.requireNonNull(tea.asItem().getItemCategory()).getRecipeFolderName() + "/" + recipeId.getPath());

		// build final recipe
		consumer.accept(new ShapelessHoneyRecipe.Finished(recipeId, "simplytea:" + tag, tea, Ingredient.of(honey), tag, advancementId, builder));
	}

	/** Adds a recipe to pour tea and make tea bags */
	private static void addTeaWithBag(Consumer<FinishedRecipe> consumer, ItemLike leaf, ItemLike filledTeabag, ItemLike filledCup) {
		ShapelessRecipeBuilder.shapeless(filledTeabag)
													.group("simplytea:teabag")
													.requires(teabag.get())
													.requires(leaf)
													.requires(leaf)
													.unlockedBy("has_leaf", has(leaf))
													.save(consumer);
		addTea(consumer, filledCup, filledTeabag, teapot_hot.get());
		addHoney(consumer, filledCup);
	}
}
