package elucent.simplytea;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.RangeDouble;
import net.minecraftforge.common.config.Config.RangeInt;

@net.minecraftforge.common.config.Config(modid = SimplyTea.MODID, category = "")
public class Config {

	@Comment("Options related to the tea tree")
	@LangKey("simplytea.config.tree")
	public static TreeCategory tree = new TreeCategory();

	public static class TreeCategory {
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
