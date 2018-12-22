package elucent.simplytea;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.RangeDouble;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.Config.RequiresMcRestart;

@net.minecraftforge.common.config.Config(modid = SimplyTea.MODID)
public class Config {
	@Comment("If true, filling a teapot consumes the water source block. Useful for packs with finite water.")
	@LangKey("simplytea.config.teapot_consume_source")
	public static boolean teapot_consume_source = false;

	@Comment("Hunger restored from tea")
	@LangKey("simplytea.config.tea")
	public static TeaCategory tea = new TeaCategory();

	@Comment("Options related to the tea tree")
	@LangKey("simplytea.config.tree")
	public static TreeCategory tree = new TreeCategory();

	public static class TeaCategory {
		@RequiresMcRestart
		@Comment("Hunger restored when drinking green tea.")
		@RangeInt(min = 0, max = 20)
		@LangKey("simplytea.config.tea.green_hunger")
		public int green_hunger = 3;

		@RequiresMcRestart
		@Comment("Saturation restored when drinking green tea")
		@RangeDouble(min = 0, max = 10)
		@LangKey("simplytea.config.tea.green_saturation")
		public double green_saturation = 0.5;

		@RequiresMcRestart
		@Comment("Hunger restored when drinking black tea.")
		@RangeInt(min = 0, max = 20)
		@LangKey("simplytea.config.tea.black_hunger")
		public int black_hunger = 4;

		@RequiresMcRestart
		@Comment("Saturation restored when drinking black tea")
		@RangeDouble(min = 0, max = 10)
		@LangKey("simplytea.config.tea.black_saturation")
		public double black_saturation = 0.8;

		@RequiresMcRestart
		@Comment("Hunger restored when drinking floral tea.")
		@RangeInt(min = 0, max = 20)
		@LangKey("simplytea.config.tea.floral_hunger")
		public int floral_hunger = 2;

		@RequiresMcRestart
		@Comment("Saturation restored when drinking floral tea")
		@RangeDouble(min = 0, max = 10)
		@LangKey("simplytea.config.tea.floral_saturation")
		public double floral_saturation = 0.4;
	}

	public static class TreeCategory {
		@RequiresMcRestart
		@Comment("If true, generates tea trees in forest biomes.")
		@LangKey("simplytea.config.tree.enable_generation")
		public boolean enable_generation = true;

		@Comment("Percent chance of leaves to regrow every random tick. Set to 1 to regrow every random tick (old behavior)")
		@RangeDouble(min = 0, max = 1)
		@LangKey("simplytea.config.tree.leaf_growth_chance")
		public double leaf_growth_chance = 0.05;

		@Comment("Maximum number of leaves dropped when a single branch is broken or sheared.")
		@RangeInt(min = 1, max = 64)
		@LangKey("simplytea.config.tree.max_leaves")
		public int max_leaves = 3;

		@Comment("Maximum number of sticks dropped when a single branch is broken or sheared.")
		@RangeInt(min = 1, max = 64)
		@LangKey("simplytea.config.tree.max_leaves")
		public int max_sticks = 3;

		@Comment("Percent chance for a tree to drop a sapling when broken or sheared")
		@RangeDouble(min = 0, max = 1)
		@LangKey("simplytea.config.tree.sapling_chance")
		public double sapling_chance = 0.1;
	}
}
