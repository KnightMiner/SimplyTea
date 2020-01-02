package knightminer.simplytea.core;

import knightminer.simplytea.SimplyTea;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.ConstantRange;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.TableLootEntry;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SimplyTea.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Events {

  @SubscribeEvent
  public static void playerWakeUp(PlayerWakeUpEvent event) {
    // update world means the client sent it, comes from the leave bed button being clicked
    // server would set that to false, like when we sleep full night
    if (event.updateWorld()) {
      return;
    }

    // if caffeinated, remove that with no restful benefits
    PlayerEntity player = event.getPlayer();
    if (player.isPotionActive(Registration.caffeinated)) {
      player.removePotionEffect(Registration.caffeinated);
      player.removePotionEffect(Registration.restful);
      return;
    }

    // if restful, heal based on the potion level and remove it
    EffectInstance effect = player.getActivePotionEffect(Registration.restful);
    if (effect != null) {
      player.heal((effect.getAmplifier()+1)*2);
      player.removePotionEffect(Registration.restful);
    }
  }

  @SubscribeEvent
  public static void entityFall(LivingFallEvent event) {
    LivingEntity entity = event.getEntityLiving();
    if (entity.isPotionActive(Registration.enderfalling)) {
      EffectInstance effect = entity.getActivePotionEffect(Registration.enderfalling);
      // every level halves the damage of the previous, but start at 1/4
      event.setDamageMultiplier(event.getDamageMultiplier() * (float)Math.pow(2, -effect.getAmplifier()-3));
    }
  }

  @SubscribeEvent
  public static void throwEnderPearl(EnderTeleportEvent event) {
    if (event.getEntityLiving().isPotionActive(Registration.enderfalling)) {
      event.setAttackDamage(0);
    }
  }

  @SubscribeEvent
  public static void addLoot(LootTableLoadEvent event) {
    addToLootTable(event, "blocks/chorus_flower");
  }

  private static void addToLootTable(LootTableLoadEvent event, String name) {
    if (!event.getName().getNamespace().equals("minecraft") || !event.getName().getPath().equals(name)) {
      return;
    }
    ResourceLocation base = new ResourceLocation(name);
    LootTable table = event.getTable();
    if (table != LootTable.EMPTY_LOOT_TABLE) {
      ResourceLocation location = new ResourceLocation(SimplyTea.MOD_ID, base.getPath());
      table.addPool(new LootPool.Builder().name(location.toString()).rolls(ConstantRange.of(1)).addEntry(TableLootEntry.builder(location)).build());
    }
  }
}
