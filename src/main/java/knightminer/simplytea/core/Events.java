package knightminer.simplytea.core;

import knightminer.simplytea.SimplyTea;
import knightminer.simplytea.potion.RestfulEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = SimplyTea.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Events {
  @SubscribeEvent
  static void playerWakeUp(PlayerWakeUpEvent event) {
    // update world means the client sent it, comes from the leave bed button being clicked
    // server would set that to false, like when we sleep full night
    if (event.updateWorld()) {
      return;
    }

    // if caffeinated, remove that with no restful benefits
    PlayerEntity player = event.getPlayer();
    if (RestfulEffect.removeConflicts(player)) {
      player.removePotionEffect(Registration.restful);
    } else {
      EffectInstance effect = player.getActivePotionEffect(Registration.restful);
      // if restful, heal based on the potion level and remove it
      if (effect != null) {
        player.heal((effect.getAmplifier()+1)*2);
        player.removePotionEffect(Registration.restful);
      }
    }

  }

  @SubscribeEvent
  static void entityFall(LivingFallEvent event) {
    LivingEntity entity = event.getEntityLiving();
    if (entity.isPotionActive(Registration.enderfalling)) {
      EffectInstance effect = entity.getActivePotionEffect(Registration.enderfalling);
      // every level halves the damage of the previous, but start at 1/4
      event.setDamageMultiplier(event.getDamageMultiplier() * (float)Math.pow(2, -effect.getAmplifier()-3));
    }
  }

  @SubscribeEvent
  static void throwEnderPearl(EnderTeleportEvent event) {
    if (event.getEntityLiving().isPotionActive(Registration.enderfalling)) {
      event.setAttackDamage(0);
    }
  }

  /**
   * Checks if the event biome is valid
   * @param event  Event to check
   * @return  True if its a forest
   */
  private static boolean validBiome(BiomeLoadingEvent event) {
    if (event.getName() == null) {
      return event.getCategory() == Category.FOREST;
    }
    return BiomeDictionary.hasType(RegistryKey.getOrCreateKey(Registry.BIOME_KEY, event.getName()), Type.FOREST);
  }

  @SubscribeEvent
  static void onBiomeLoad(BiomeLoadingEvent event) {
    if (validBiome(event)) {
      event.getGeneration().withFeature(Decoration.VEGETAL_DECORATION, Registration.configured_tea_tree);
    }
  }
}
