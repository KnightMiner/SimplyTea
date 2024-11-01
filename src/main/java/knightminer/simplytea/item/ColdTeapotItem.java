package knightminer.simplytea.item;

import knightminer.simplytea.core.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class ColdTeapotItem extends TooltipItem {

    public ColdTeapotItem(Properties props) {
        super(props);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        BlockPos pos = context.getClickedPos();
        if (player.isShiftKeyDown() && context.getClickedFace() != Direction.DOWN) {
				player.playSound(SoundEvents.BUCKET_EMPTY, 1.0f, 1.0f);
				if (!world.isClientSide) {
					for (int i = 0; i < 5; i++) {
						SimpleParticleType particle = stack.is(Registration.teapot_milk) ? Registration.milk_splash : ParticleTypes.SPLASH;
						((ServerLevel)world).sendParticles(particle, (double)pos.getX() + world.random.nextDouble(), (double)pos.getY() + 1, (double)pos.getZ() + world.random.nextDouble(), 1, 0.0D, 0.0D, 0.0D, 1.0D);
					}
                    // emptying a teapot will not create new empty teapots in creative mode if player already has an empty teapot
                    ItemStack emptyTeapot = ItemUtils.createFilledResult(stack, player, new ItemStack(Registration.teapot));
                    player.setItemInHand(context.getHand(), emptyTeapot);
                    
                    return InteractionResult.SUCCESS;
                }
			}
        return InteractionResult.PASS;
    }
}
