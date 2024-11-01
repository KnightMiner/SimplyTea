package knightminer.simplytea.core;

import knightminer.simplytea.core.config.CocoaDrink;
import knightminer.simplytea.core.config.CocoaDrink.ClearType;
import knightminer.simplytea.core.config.TeaDrink;
import knightminer.simplytea.core.config.TeaDrink.TeaEffect;
import knightminer.simplytea.core.config.Teapot;
import knightminer.simplytea.core.config.Tree;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import org.apache.commons.lang3.tuple.Pair;

//@Mod.EventBusSubscriber(modid=SimplyTea.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {

	public static class Server {
		public TeaDrink black_tea, green_tea, floral_tea, chai_tea, chorus_tea, iced_tea;
		public CocoaDrink cocoa, frothed_milk;
		public Teapot teapot;
		public Tree tree;
		public Server(Builder builder) {

			// drinks
			builder.comment("Stats for each available drink type").push("drinks");
			floral_tea = new TeaDrink("floral", builder, TeaEffect.RESTFUL, 1, 0.5, 60, 2);
			green_tea  = new TeaDrink("green", builder, TeaEffect.RELAXED, 3, 0.5, 120, 2);
			black_tea  = new TeaDrink("black", builder, TeaEffect.CAFFEINATED, 4, 0.8, 210, 2);
			chai_tea   = new TeaDrink("chai", builder, TeaEffect.INVIGORATED, 5, 0.6, 150, 1);
			chorus_tea = new TeaDrink("chorus", builder, TeaEffect.ENDERFALLING, 3, 0.9, 150, 1);
			iced_tea = new TeaDrink("iced", builder, TeaEffect.ABSORPTION, 3, 0.9, 90, 1);
			cocoa = new CocoaDrink("cocoa", builder, ClearType.NEGATIVE, 4, 0.6);
			frothed_milk = new CocoaDrink("frothed_milk", builder, ClearType.ALL, 1, 0.2);
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

//	@SubscribeEvent
//	public static void onFileChange(final ModConfig.Reloading event) {
//		// clear the effect cache so we get the new version of the config
//		if (event.getConfig().getType() == ModConfig.Type.SERVER) {
//		}
//	}
}
