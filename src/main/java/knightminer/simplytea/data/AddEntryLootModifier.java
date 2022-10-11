package knightminer.simplytea.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.common.loot.LootModifierManager;

/**
 * Loot modifier to inject an additional loot entry into an existing table
 * Based on code from Mantle
 */
public class AddEntryLootModifier extends LootModifier {
	
	private static final Codec<LootPoolEntryContainer> LOOT_POOL_ENTRY_CODEC = Codec.PASSTHROUGH.flatXmap(
			d -> {
				try {
					LootPoolEntryContainer entry = LootModifierManager.GSON_INSTANCE.fromJson(getJson(d), LootPoolEntryContainer.class);
					return DataResult.success(entry);
				}catch(JsonSyntaxException e) {
					LootModifierManager.LOGGER.warn("Unable to decode entry", e);
                    return DataResult.error(e.getMessage());
				}
			},
			entry -> {
				try {
                    JsonElement element = LootModifierManager.GSON_INSTANCE.toJsonTree(entry);
                    return DataResult.success(new Dynamic<>(JsonOps.INSTANCE, element));
                }catch (JsonSyntaxException e) {
                    LootModifierManager.LOGGER.warn("Unable to encode entry", e);
                    return DataResult.error(e.getMessage());
                }
			});

	public static final Codec<AddEntryLootModifier> CODEC = RecordCodecBuilder.create(inst -> codecStart(inst).and(
            inst.group(
            		LOOT_POOL_ENTRY_CODEC.fieldOf("entry").forGetter(m -> m.entry),
            		Codec.BOOL.fieldOf("require_empty").orElse(false).forGetter(m -> m.requireEmpty)
            )).apply(inst, AddEntryLootModifier::new)
    );

	private final LootPoolEntryContainer entry;
	private final boolean requireEmpty;
	
	public AddEntryLootModifier(LootItemCondition[] conditionsIn, LootPoolEntryContainer entry, boolean requireEmpty) {
		super(conditionsIn);
		this.entry = entry;
		this.requireEmpty = requireEmpty;
	}

	@Override
	public ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		if (!requireEmpty || generatedLoot.isEmpty()) {
			entry.expand(context, generator -> generator.createItemStack(generatedLoot::add, context));
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
