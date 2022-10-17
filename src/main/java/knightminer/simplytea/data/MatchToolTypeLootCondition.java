package knightminer.simplytea.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import knightminer.simplytea.SimplyTea;
import knightminer.simplytea.core.Registration;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.util.GsonHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ToolType;

/** Condition that validates a specific tool type is used in breaking the block */
public class MatchToolTypeLootCondition implements LootItemCondition {
	public static final Serializer SERIALIZER = new Serializer();
	public static final ResourceLocation ID = new ResourceLocation(SimplyTea.MOD_ID, "match_tool_type");
	private final ToolType toolType;

	public MatchToolTypeLootCondition(ToolType toolType) {
		this.toolType = toolType;
	}

	@Override
	public LootItemConditionType getType() {
		return Registration.matchToolType;
	}

	@Override
	public boolean test(LootContext lootContext) {
		ItemStack stack = lootContext.getParamOrNull(LootContextParams.TOOL);
		if (stack != null && !stack.isEmpty()) {
			return stack.getToolTypes().contains(toolType);
		}
		return false;
	}

	private static class Serializer implements Serializer<MatchToolTypeLootCondition> {
		@Override
		public void serialize(JsonObject json, MatchToolTypeLootCondition loot, JsonSerializationContext context) {
			json.addProperty("type", loot.toolType.getName());
		}

		@Override
		public MatchToolTypeLootCondition deserialize(JsonObject json, JsonDeserializationContext context) {
			ToolType toolType = ToolType.get(GsonHelper.getAsString(json, "type"));
			return new MatchToolTypeLootCondition(toolType);
		}
	}
}
