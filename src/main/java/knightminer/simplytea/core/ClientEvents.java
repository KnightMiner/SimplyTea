package knightminer.simplytea.core;

import knightminer.simplytea.SimplyTea;
import net.minecraft.world.FoliageColors;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = SimplyTea.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {
  @SubscribeEvent
  public static void registerBlockColors(ColorHandlerEvent.Block event) {
    event.getBlockColors().register((state, world, pos, index) -> {
      if (world == null || pos == null) {
        return FoliageColors.getDefault();
      }
      return BiomeColors.getFoliageColor(world, pos);
    }, Registration.tea_trunk);
  }
}
