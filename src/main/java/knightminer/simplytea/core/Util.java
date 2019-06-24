package knightminer.simplytea.core;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.LootTable;

import javax.annotation.Nullable;
import java.util.List;

public final class Util {
    private Util () {}

    /**
     * Runs a given loot table resource location using block context
     * @param state     Current block state
     * @param world     Server world for the block
     * @param pos       Position of the block
     * @param player    Player interacting wti the block
     * @param tool      Tool being used on the block
     * @param location  Location of loot table to use to get block drops
     * @return  List of block drops
     */
    public static List<ItemStack> getBlockLoot(BlockState state, ServerWorld world, BlockPos pos, @Nullable PlayerEntity player, ItemStack tool, ResourceLocation location) {
        LootContext.Builder builder = new LootContext.Builder(world)
            .withParameter(LootParameters.POSITION, pos)
            .withParameter(LootParameters.BLOCK_STATE, state)
            .withNullableParameter(LootParameters.BLOCK_ENTITY, world.getTileEntity(pos))
            .withNullableParameter(LootParameters.THIS_ENTITY, player)
            .withParameter(LootParameters.TOOL, tool);
        LootContext lootcontext = builder.build(LootParameterSets.BLOCK);
        ServerWorld serverworld = lootcontext.getWorld();
        LootTable table = serverworld.getServer().getLootTableManager().getLootTableFromLocation(location);
        return table.generate(lootcontext);
    }
}
