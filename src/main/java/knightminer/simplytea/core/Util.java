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
            .withParameter(LootParameters.field_237457_g_, Vector3d.copyCentered(pos))
            .withParameter(LootParameters.BLOCK_STATE, state)
            .withNullableParameter(LootParameters.BLOCK_ENTITY, world.getTileEntity(pos))
            .withNullableParameter(LootParameters.THIS_ENTITY, player)
            .withParameter(LootParameters.TOOL, tool);
        LootContext context = builder.build(LootParameterSets.BLOCK);
        LootTable table = world.getServer().getLootTableManager().getLootTableFromLocation(location);
        return table.generate(context);
    }

    /**
     * Fills a stack of containers, shrinking it by 1
     * @param player     Player to give the item to and for creative checks
     * @param container  Container stack, may contain more than 1
     * @param filled     Filled container stack
     * @return  Filled stack if 1 container, leftover container if more than 1, dropping the filled
     */
    public static ItemStack fillContainer(PlayerEntity player, ItemStack container, ItemStack filled) {
        container = container.copy();
        if (!player.isCreative()) {
            container.shrink(1);
            if (container.isEmpty()) {
                return filled;
            }
        }
        if (!player.inventory.addItemStackToInventory(filled)) {
            player.dropItem(filled, false);
        }
        return container;
    }
}
