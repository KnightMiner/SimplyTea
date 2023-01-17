package knightminer.simplytea.core;

import knightminer.simplytea.SimplyTea;
import knightminer.simplytea.potion.RestfulEffect;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = SimplyTea.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Events {
  @SubscribeEvent
  static void playerWakeUp(PlayerWakeUpEvent event) {
    // update world means the client sent it, comes from the leave bed button being clicked
    // server would set that to false, like when we sleep full night
    if (event.updateLevel()) {
      return;
    }

    // if caffeinated, remove that with no restful benefits
    Player player = event.getEntity();
    if (RestfulEffect.removeConflicts(player)) {
      player.removeEffect(Registration.restful);
    } else {
      MobEffectInstance effect = player.getEffect(Registration.restful);
      // if restful, heal based on the potion level and remove it
      if (effect != null) {
        player.heal((effect.getAmplifier()+1)*2);
        player.removeEffect(Registration.restful);
      }
    }

  }

  @SubscribeEvent
  static void entityFall(LivingFallEvent event) {
    LivingEntity entity = event.getEntity();
    MobEffectInstance effect = entity.getEffect(Registration.enderfalling);
    if (effect != null) {
      // every level halves the damage of the previous, but start at 1/4
      event.setDamageMultiplier(event.getDamageMultiplier() * (float)Math.pow(2, -effect.getAmplifier()-3));
    }
  }

  @SubscribeEvent
  static void throwEnderPearl(EntityTeleportEvent.EnderPearl event) {
    if (event.getPlayer().hasEffect(Registration.enderfalling)) {
      event.setAttackDamage(0);
    }
  }
}
