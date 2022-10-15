package knightminer.simplytea.data;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.Deserializers;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

/**
 * Loot modifier to inject an additional loot entry into an existing table
 * Based on code from Mantle
 */
public class AddEntryLootModifier extends LootModifier {
	
	private static final Gson GSON = Deserializers.createFunctionSerializer().create();

	private final LootPoolEntryContainer entry;
	private final boolean requireEmpty;
	
	public AddEntryLootModifier(LootItemCondition[] conditionsIn, LootPoolEntryContainer entry, boolean requireEmpty) {
		super(conditionsIn);
		this.entry = entry;
		this.requireEmpty = requireEmpty;
	}

	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		if (!requireEmpty || generatedLoot.isEmpty()) {
			entry.expand(context, generator -> generator.createItemStack(generatedLoot::add, context));
		}
		return generatedLoot;
	}
	
	public static class Serializer extends GlobalLootModifierSerializer<AddEntryLootModifier> {
		@Override
		public AddEntryLootModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] ailootcondition) {
			LootPoolEntryContainer entry = GSON.fromJson(GsonHelper.getAsJsonObject(object, "entry"), LootPoolEntryContainer.class);
			boolean requireEmpty = GsonHelper.getAsBoolean(object, "require_empty", false);
			return new AddEntryLootModifier(ailootcondition, entry, requireEmpty);
		}

		@Override
		public JsonObject write(AddEntryLootModifier instance) {
			JsonObject object = makeConditions(instance.conditions);
			object.addProperty("require_empty", instance.requireEmpty);
			object.add("entry", GSON.toJsonTree(instance.entry, LootPoolEntryContainer.class));
			return object;
		}
	}
}
