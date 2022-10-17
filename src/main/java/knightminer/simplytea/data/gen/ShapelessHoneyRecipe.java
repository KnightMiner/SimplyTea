package knightminer.simplytea.data.gen;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import knightminer.simplytea.core.Registration;
import knightminer.simplytea.item.TeaCupItem;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.Advancement.Builder;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Objects;

public class ShapelessHoneyRecipe extends ShapelessRecipe {
	private final Item tea;
	private final Ingredient honey;
	private final String tag;
	public ShapelessHoneyRecipe(ResourceLocation id, String group, IItemProvider tea, Ingredient honey, String tag) {
		super(id, group, TeaCupItem.withHoney(new ItemStack(tea), tag), NonNullList.of(Ingredient.EMPTY, Ingredient.of(tea), honey));
		this.tea = tea.asItem();
		this.honey = honey;
		this.tag = tag;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return Registration.shapeless_honey;
	}

	@Override
	public boolean matches(CraftingInventory inv, World worldIn) {
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
	public ItemStack assemble(CraftingInventory inv) {
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
	public NonNullList<ItemStack> getRemainingItems(CraftingInventory inv) {
		NonNullList<ItemStack> list = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);
		for(int i = 0; i < list.size(); ++i) {
			ItemStack item = inv.getItem(i);
			if (item.hasContainerItem() && item.getItem() != tea) {
				list.set(i, item.getContainerItem());
			}
		}

		return list;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ShapelessHoneyRecipe> {
		@Override
		public ShapelessHoneyRecipe fromJson(ResourceLocation id, JsonObject json) {
			String group = JSONUtils.getAsString(json, "group");
			String name = JSONUtils.getAsString(json, "tea");
			ResourceLocation location = ResourceLocation.tryParse(name);
			if (location == null) {
				throw new JsonSyntaxException("Invalid tea_cup location '" + name + "'");
			}
			Item item = ForgeRegistries.ITEMS.getValue(location);
			if (item == null || item == Items.AIR) {
				throw new JsonSyntaxException("Missing tea_cup item '" + name + "'");
			}
			Ingredient ingredient = Ingredient.fromJson(json.get("honey"));
			String tag = JSONUtils.getAsString(json, "tag", "with_honey");
			return new ShapelessHoneyRecipe(id, group, item, ingredient, tag);
		}

		@Nullable
		@Override
		public ShapelessHoneyRecipe fromNetwork(ResourceLocation id, PacketBuffer buffer) {
			String group = buffer.readUtf(Short.MAX_VALUE);
			Item item = buffer.readRegistryIdUnsafe(ForgeRegistries.ITEMS);
			Ingredient ingredient = Ingredient.fromNetwork(buffer);
			String tag = buffer.readUtf(Short.MAX_VALUE);
			return new ShapelessHoneyRecipe(id, group, item, ingredient, tag);
		}

		@Override
		public void toNetwork(PacketBuffer buffer, ShapelessHoneyRecipe recipe) {
			buffer.writeUtf(recipe.getGroup());
			buffer.writeRegistryIdUnsafe(ForgeRegistries.ITEMS, recipe.tea);
			recipe.honey.toNetwork(buffer);
			buffer.writeUtf(recipe.tag);
		}
	}

	/** Finished recipe for datagen */
	public static class FinishedRecipe implements IFinishedRecipe {
		private final ResourceLocation id;
		private final String group;
		private final Item tea;
		private final Ingredient honey;
		private final String tag;
		private final ResourceLocation advancementId;
		private final Advancement.Builder advancementBuilder;

		public FinishedRecipe(ResourceLocation id, String group, IItemProvider tea, Ingredient honey, String tag, @Nullable ResourceLocation advancementId, @Nullable Builder advancementBuilder) {
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
		public IRecipeSerializer<?> getType() {
			return Registration.shapeless_honey;
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
			if (!group.isEmpty()) {
				json.addProperty("group", group);
			}
			json.addProperty("tea", Objects.requireNonNull(tea.getRegistryName()).toString());
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
