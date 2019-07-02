package knightminer.simplytea.core;

import knightminer.simplytea.SimplyTea;
import knightminer.simplytea.core.config.CocoaDrink;
import knightminer.simplytea.core.config.TeaDrink;
import knightminer.simplytea.core.config.TeaDrink.TeaEffect;
import knightminer.simplytea.core.config.Teapot;
import knightminer.simplytea.core.config.Tree;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid=SimplyTea.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {

	public static class Server {
		public TeaDrink black_tea, green_tea, floral_tea, chai_tea, chorus_tea;
		public CocoaDrink cocoa;
		public Teapot teapot;
		public Tree tree;
		public Server(Builder builder) {

			// drinks
			builder.comment("Stats for each available drink type").push("drinks");
			floral_tea = new TeaDrink("floral", builder, TeaEffect.HERBAL, 2, 0.5, 20, 2);
			green_tea  = new TeaDrink("green", builder, TeaEffect.CAFFEINE, 3, 0.5, 150, 2);
			black_tea  = new TeaDrink("black", builder, TeaEffect.CAFFEINE, 4, 0.8, 210, 2);
			chai_tea   = new TeaDrink("chai", builder, TeaEffect.CAFFEINE, 5, 0.6, 150, 3);
			chorus_tea = new TeaDrink("chorus", builder, TeaEffect.ENDERFALLING, 3, 0.9, 150, 1);
			cocoa = new CocoaDrink(builder, 4, 0.6);
			builder.pop();

			// other categories
			teapot = new Teapot(builder);
			tree = new Tree(builder);
		}
	}


	/* Initialize */

	public static final ForgeConfigSpec serverSpec;
	public static final Server SERVER;
	static {
		final Pair<Server,ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Server::new);
		serverSpec = specPair.getRight();
		SERVER = specPair.getLeft();
	}

	@SubscribeEvent
	public static void onFileChange(final ModConfig.ConfigReloading event) {
		// clear the effect cache so we get the new version of the config
		if (event.getConfig().getType() == ModConfig.Type.SERVER) {
			SERVER.floral_tea.invalidEffects();
			SERVER.green_tea.invalidEffects();
			SERVER.black_tea.invalidEffects();
			SERVER.chai_tea.invalidEffects();
			SERVER.chorus_tea.invalidEffects();
		}
	}
}
