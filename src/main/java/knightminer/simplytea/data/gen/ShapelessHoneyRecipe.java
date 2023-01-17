package knightminer.simplytea.data.gen;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import knightminer.simplytea.core.Registration;
import knightminer.simplytea.item.TeaCupItem;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.Advancement.Builder;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Objects;

public class ShapelessHoneyRecipe extends ShapelessRecipe {
	private final Item tea;
	private final Ingredient honey;
	private final String tag;
	public ShapelessHoneyRecipe(ResourceLocation id, String group, ItemLike tea, Ingredient honey, String tag) {
		super(id, group, TeaCupItem.withHoney(new ItemStack(tea), tag), NonNullList.of(Ingredient.EMPTY, Ingredient.of(tea), honey));
		this.tea = tea.asItem();
		this.honey = honey;
		this.tag = tag;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return Registration.shapeless_honey;
	}

	@Override
	public boolean matches(CraftingContainer inv, Level worldIn) {
		if (!super.matches(inv, worldIn)) {
			return false;
		}
		// search the inventory for the tea, ensure it lacks honey
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty() && stack.getItem() == tea) {
				return !TeaCupItem.hasHoney(stack, tag);
			}
		}
		// somehow did not find the tea, thats a problem
		return false;
	}

	@Override
	public ItemStack assemble(CraftingContainer inv) {
		// search the inventory for the tea, copy that for the return
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty() && stack.getItem() == tea) {
				return TeaCupItem.withHoney(ItemHandlerHelper.copyStackWithSize(stack, 1), tag);
			}
		}
		return getResultItem().copy();
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
		NonNullList<ItemStack> list = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);
		for(int i = 0; i < list.size(); ++i) {
			ItemStack item = inv.getItem(i);
			if (item.hasCraftingRemainingItem() && item.getItem() != tea) {
				list.set(i, item.getCraftingRemainingItem());
			}
		}

		return list;
	}

	public static class Serializer implements RecipeSerializer<ShapelessHoneyRecipe> {
		@Override
		public ShapelessHoneyRecipe fromJson(ResourceLocation id, JsonObject json) {
			String group = GsonHelper.getAsString(json, "group");
			String name = GsonHelper.getAsString(json, "tea");
			ResourceLocation location = ResourceLocation.tryParse(name);
			if (location == null) {
				throw new JsonSyntaxException("Invalid tea_cup location '" + name + "'");
			}
			Item item = ForgeRegistries.ITEMS.getValue(location);
			if (item == null || item == Items.AIR) {
				throw new JsonSyntaxException("Missing tea_cup item '" + name + "'");
			}
			Ingredient ingredient = Ingredient.fromJson(json.get("honey"));
			String tag = GsonHelper.getAsString(json, "tag", "with_honey");
			return new ShapelessHoneyRecipe(id, group, item, ingredient, tag);
		}

		@Nullable
		@Override
		public ShapelessHoneyRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
			String group = buffer.readUtf(Short.MAX_VALUE);
			Item item = buffer.readRegistryIdUnsafe(ForgeRegistries.ITEMS);
			Ingredient ingredient = Ingredient.fromNetwork(buffer);
			String tag = buffer.readUtf(Short.MAX_VALUE);
			return new ShapelessHoneyRecipe(id, group, item, ingredient, tag);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, ShapelessHoneyRecipe recipe) {
			buffer.writeUtf(recipe.getGroup());
			buffer.writeRegistryIdUnsafe(ForgeRegistries.ITEMS, recipe.tea);
			recipe.honey.toNetwork(buffer);
			buffer.writeUtf(recipe.tag);
		}
	}

	/** Finished recipe for datagen */
	public static class Finished implements FinishedRecipe {
		private final ResourceLocation id;
		private final String group;
		private final Item tea;
		private final Ingredient honey;
		private final String tag;
		private final ResourceLocation advancementId;
		private final Advancement.Builder advancementBuilder;

		public Finished(ResourceLocation id, String group, ItemLike tea, Ingredient honey, String tag, @Nullable ResourceLocation advancementId, @Nullable Builder advancementBuilder) {
			this.id = id;
			this.group = group;
			this.tea = tea.asItem();
			this.honey = honey;
			this.tag = tag;
			this.advancementId = advancementId;
			this.advancementBuilder = advancementBuilder;
		}

		@Override
		public ResourceLocation getId() {
			return id;
		}

		@Override
		public RecipeSerializer<?> getType() {
			return Registration.shapeless_honey;
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
			if (!group.isEmpty()) {
				json.addProperty("group", group);
			}
			json.addProperty("tea", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(tea)).toString());
			json.add("honey", honey.toJson());
			json.addProperty("tag", tag);
		}

		@Nullable
		@Override
		public ResourceLocation getAdvancementId() {
			return advancementId;
		}

		@Nullable
		@Override
		public JsonObject serializeAdvancement() {
			if (advancementBuilder == null) {
				return null;
			}
			return advancementBuilder.serializeToJson();
		}
	}
}
