package knightminer.simplytea.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.common.loot.LootModifierManager;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * Loot modifier to inject an additional loot entry into an existing table
 * Based on code from Mantle
 */
public class AddEntryLootModifier extends LootModifier {
	
	private static final Codec<LootItemFunction[]> LOOT_ITEM_FUNCTION_CODEC = Codec.PASSTHROUGH.flatXmap(
			d -> {
				try {
					LootItemFunction[] functions = LootModifierManager.GSON_INSTANCE.fromJson(getJson(d), LootItemFunction[].class);
					return DataResult.success(functions);
				}catch(JsonSyntaxException e) {
					LootModifierManager.LOGGER.warn("Unable to decode item functions", e);
                    return DataResult.error(e.getMessage());
				}
			},
			functions -> {
				try
                {
                    JsonElement element = LootModifierManager.GSON_INSTANCE.toJsonTree(functions);
                    return DataResult.success(new Dynamic<>(JsonOps.INSTANCE, element));
                }
                catch (JsonSyntaxException e)
                {
                    LootModifierManager.LOGGER.warn("Unable to encode item functions", e);
                    return DataResult.error(e.getMessage());
                }
			}); 

	public static final Codec<AddEntryLootModifier> CODEC = RecordCodecBuilder.create(inst -> codecStart(inst).and(
            inst.group(
            		ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(m -> m.item),
            		LOOT_ITEM_FUNCTION_CODEC.fieldOf("functions").forGetter(m -> m.functions),
            		Codec.BOOL.fieldOf("require_empty").orElse(false).forGetter(m -> m.requireEmpty)
            )).apply(inst, AddEntryLootModifier::new)
    );

	private final Item item;
	private final LootItemFunction[] functions;
	private final BiFunction<ItemStack, LootContext, ItemStack> combinedFunctions;
	private final boolean requireEmpty;
	
	public AddEntryLootModifier(LootItemCondition[] conditionsIn, Item item, LootItemFunction[] functions, boolean requireEmpty) {
		super(conditionsIn);
		this.item = item;
		this.functions = functions;
		this.combinedFunctions = LootItemFunctions.compose(functions);
		this.requireEmpty = requireEmpty;
	}

	@Override
	public ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		if (!requireEmpty || generatedLoot.isEmpty()) {
			Consumer<ItemStack> consumer = LootItemFunction.decorate(this.combinedFunctions, generatedLoot::add, context);
			ItemStack stack = new ItemStack(this.item);
			consumer.accept(stack);
		}
		return generatedLoot;
	}
	
	@Override
	public Codec<? extends IGlobalLootModifier> codec() {
		return CODEC;
	}
	
	// From IGlobalLootModifier
	@SuppressWarnings("unchecked")
	private static <U> JsonElement getJson(Dynamic<?> dynamic) {
        Dynamic<U> typed = (Dynamic<U>) dynamic;
        return typed.getValue() instanceof JsonElement ?
                (JsonElement) typed.getValue() :
                typed.getOps().convertTo(JsonOps.INSTANCE, typed.getValue());
    }
}
