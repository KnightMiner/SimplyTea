package knightminer.simplytea.core;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;

public final class Util {
    private Util () {}

    /**
     * Runs a given loot table resource location using block context. Compare to {@link net.minecraft.block.Block#getDrops(BlockState, ServerWorld, BlockPos, TileEntity, Entity, ItemStack)}
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
            .withParameter(LootParameters.ORIGIN, Vector3d.atCenterOf(pos))
            .withParameter(LootParameters.BLOCK_STATE, state)
            .withOptionalParameter(LootParameters.BLOCK_ENTITY, world.getBlockEntity(pos))
            .withOptionalParameter(LootParameters.THIS_ENTITY, player)
            .withParameter(LootParameters.TOOL, tool);
        LootContext context = builder.create(LootParameterSets.BLOCK);
        LootTable table = world.getServer().getLootTables().get(location);
        return table.getRandomItems(context);
    }
}
