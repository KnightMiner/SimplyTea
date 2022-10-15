package knightminer.simplytea.core;

import knightminer.simplytea.SimplyTea;
import knightminer.simplytea.core.config.Tree;
import knightminer.simplytea.potion.RestfulEffect;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.BiomeCategory;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryManager;

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
    Player player = event.getPlayer();
    if (RestfulEffect.removeConflicts(player)) {
      player.removeEffect(Registration.restful.get());
    } else {
      MobEffectInstance effect = player.getEffect(Registration.restful.get());
      // if restful, heal based on the potion level and remove it
      if (effect != null) {
        player.heal((effect.getAmplifier()+1)*2);
        player.removeEffect(Registration.restful.get());
      }
    }

  }

  @SubscribeEvent
  static void entityFall(LivingFallEvent event) {
    LivingEntity entity = event.getEntityLiving();
    MobEffectInstance effect = entity.getEffect(Registration.enderfalling.get());
    if (effect != null) {
      // every level halves the damage of the previous, but start at 1/4
      event.setDamageMultiplier(event.getDamageMultiplier() * (float)Math.pow(2, -effect.getAmplifier()-3));
    }
  }

  @SubscribeEvent
  static void throwEnderPearl(EntityTeleportEvent.EnderPearl event) {
    if (event.getPlayer().hasEffect(Registration.enderfalling.get())) {
      event.setAttackDamage(0);
    }
  }
  
  /**
   * Checks if the event biome is valid
   * @param event  Event to check
   * @return  True if its a forest
   */
  @SuppressWarnings("deprecation")
  private static boolean validBiome(BiomeLoadingEvent event) {
    if (event.getName() == null) {
      return event.getCategory() == BiomeCategory.FOREST;
    }
    
    ResourceKey<Biome> resourceKey = ResourceKey.create(ForgeRegistries.BIOMES.getRegistryKey(), event.getName());
    return BiomeDictionary.hasType(resourceKey, BiomeDictionary.Type.FOREST);
  }

  @SubscribeEvent
  static void onBiomeLoad(BiomeLoadingEvent event) {
    if (Config.SERVER.tree.generate() && validBiome(event)) {
      event.getGeneration().addFeature(Decoration.VEGETAL_DECORATION, Registration.placed_tea_tree.getHolder().get());
    }
  }
}
