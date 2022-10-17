package knightminer.simplytea.data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.Deserializers;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.util.GsonHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * Loot modifier to inject an additional loot entry into an existing table
 * Based on code from Mantle
 */
public class AddEntryLootModifier extends LootModifier {
	private static final Gson GSON = Deserializers.createFunctionSerializer().create();

	private final LootPoolEntryContainer entry;
	private final LootItemFunction[] functions;
	private final BiFunction<ItemStack, LootContext, ItemStack> combinedFunctions;
	private final boolean requireEmpty;
	protected AddEntryLootModifier(LootItemCondition[] conditionsIn, LootPoolEntryContainer entry, LootItemFunction[] functions, boolean requireEmpty) {
		super(conditionsIn);
		this.entry = entry;
		this.functions = functions;
		this.combinedFunctions = LootItemFunctions.compose(functions);
		this.requireEmpty = requireEmpty;
	}

	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		if (!requireEmpty || generatedLoot.isEmpty()) {
			Consumer<ItemStack> consumer = LootItemFunction.decorate(this.combinedFunctions, generatedLoot::add, context);
			entry.expand(context, generator -> generator.createItemStack(consumer, context));
		}
		return generatedLoot;
	}

	public static class Serializer extends GlobalLootModifierSerializer<AddEntryLootModifier> {
		@Override
		public AddEntryLootModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] conditions) {
			LootPoolEntryContainer entry = GSON.fromJson(GsonHelper.getAsJsonObject(object, "entry"), LootPoolEntryContainer.class);
			LootItemFunction[] functions;
			if (object.has("functions")) {
				functions = GSON.fromJson(GsonHelper.getAsJsonArray(object, "functions"), LootItemFunction[].class);
			} else {
				functions = new LootItemFunction[0];
			}
			boolean requireEmpty = GsonHelper.getAsBoolean(object, "require_empty", false);
			return new AddEntryLootModifier(conditions, entry, functions, requireEmpty);
		}

		@Override
		public JsonObject write(AddEntryLootModifier instance) {
			JsonObject object = makeConditions(instance.conditions);
			object.addProperty("require_empty", instance.requireEmpty);
			object.add("entry", GSON.toJsonTree(instance.entry, LootPoolEntryContainer.class));
			object.add("functions", GSON.toJsonTree(instance.functions, LootItemFunction[].class));
			return object;
		}
	}
}
