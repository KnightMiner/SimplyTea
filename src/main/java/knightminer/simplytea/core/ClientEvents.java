package knightminer.simplytea.core;

import knightminer.simplytea.SimplyTea;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = SimplyTea.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {
  @SubscribeEvent
  public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
    event.register((state, world, pos, index) -> {
      if (world == null || pos == null) {
        return FoliageColor.getDefaultColor();
      }
      return BiomeColors.getAverageFoliageColor(world, pos);
    }, Registration.tea_trunk);
  }

  @SubscribeEvent
  public static void registerMisc(FMLClientSetupEvent event) {
    // set render types
    RenderType cutout_mipped = RenderType.cutoutMipped();
    ItemBlockRenderTypes.setRenderLayer(Registration.tea_sapling, cutout_mipped);
    ItemBlockRenderTypes.setRenderLayer(Registration.potted_tea_sapling, cutout_mipped);
    ItemBlockRenderTypes.setRenderLayer(Registration.tea_trunk, cutout_mipped);
  }
}
