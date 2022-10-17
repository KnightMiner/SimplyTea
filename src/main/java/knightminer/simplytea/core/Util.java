package knightminer.simplytea.core;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public final class Util {
    private Util () {}

    /**
     * Runs a given loot table resource location using block context. Compare to {@link net.minecraft.world.level.block.Block#getDrops(BlockState, ServerLevel, BlockPos, BlockEntity, Entity, ItemStack)}
     * @param state     Current block state
     * @param world     Server world for the block
     * @param pos       Position of the block
     * @param player    Player interacting wti the block
     * @param tool      Tool being used on the block
     * @param location  Location of loot table to use to get block drops
     * @return  List of block drops
     */
    public static List<ItemStack> getBlockLoot(BlockState state, ServerLevel world, BlockPos pos, @Nullable Player player, ItemStack tool, ResourceLocation location) {
        LootContext.Builder builder = new LootContext.Builder(world)
            .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
            .withParameter(LootContextParams.BLOCK_STATE, state)
            .withOptionalParameter(LootContextParams.BLOCK_ENTITY, world.getBlockEntity(pos))
            .withOptionalParameter(LootContextParams.THIS_ENTITY, player)
            .withParameter(LootContextParams.TOOL, tool);
        LootContext context = builder.create(LootContextParamSets.BLOCK);
        LootTable table = world.getServer().getLootTables().get(location);
        return table.getRandomItems(context);
    }
}
