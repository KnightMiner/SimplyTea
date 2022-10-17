package knightminer.simplytea.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import knightminer.simplytea.SimplyTea;
import knightminer.simplytea.core.Registration;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ToolType;

/** Condition that validates a specific tool type is used in breaking the block */
public class MatchToolTypeLootCondition implements ILootCondition {
	public static final Serializer SERIALIZER = new Serializer();
	public static final ResourceLocation ID = new ResourceLocation(SimplyTea.MOD_ID, "match_tool_type");
	private final ToolType toolType;

	public MatchToolTypeLootCondition(ToolType toolType) {
		this.toolType = toolType;
	}

	@Override
	public LootConditionType getType() {
		return Registration.matchToolType;
	}

	@Override
	public boolean test(LootContext lootContext) {
		ItemStack stack = lootContext.getParamOrNull(LootParameters.TOOL);
		if (stack != null && !stack.isEmpty()) {
			return stack.getToolTypes().contains(toolType);
		}
		return false;
	}

	private static class Serializer implements ILootSerializer<MatchToolTypeLootCondition> {
		@Override
		public void serialize(JsonObject json, MatchToolTypeLootCondition loot, JsonSerializationContext context) {
			json.addProperty("type", loot.toolType.getName());
		}

		@Override
		public MatchToolTypeLootCondition deserialize(JsonObject json, JsonDeserializationContext context) {
			ToolType toolType = ToolType.get(JSONUtils.getAsString(json, "type"));
			return new MatchToolTypeLootCondition(toolType);
		}
	}
}
