package elucent.simplytea.core;

import elucent.simplytea.SimplyTea;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Ignore;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.RangeDouble;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.Config.RequiresMcRestart;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@net.minecraftforge.common.config.Config(modid = SimplyTea.MODID)
public class Config {
	@Comment("Hunger restored from tea")
	@LangKey("simplytea.config.tea")
	public static TeaCategory tea = new TeaCategory();

	@Comment("Options related to filling the teapot")
	@LangKey("simplytea.config.teapot")
	public static TeapotCategory teapot = new TeapotCategory();

	@Comment("Options related to the tea tree")
	@LangKey("simplytea.config.tree")
	public static TreeCategory tree = new TreeCategory();

	public static class TeaCategory {
		@Comment("Stats for floral tea.")
		@LangKey("simplytea.config.tea.floral")
		public HerbalTea floral = new HerbalTea(2, 0.5, 1);

		@Comment("Stats for green tea.")
		@LangKey("simplytea.config.tea.green")
		public CaffeineTea green = new CaffeineTea(3, 0.5, 150);

		@Comment("Stats for black tea.")
		@LangKey("simplytea.config.tea.black")
		public CaffeineTea black = new CaffeineTea(4, 0.8, 210);

		@Comment("Stats for chai tea.")
		@LangKey("simplytea.config.tea.green")
		public CaffeineTea chai = new CaffeineTea(5, 0.6, 150);

		@Comment("Stats and effects for chamomile tea, added when Rustic is loaded.")
		@LangKey("simplytea.config.tea.chamomile")
		public HerbalTea chamomile = new HerbalTea(2, 0.5, 2);

		@Comment("Stats for chai tea.")
		@LangKey("simplytea.config.tea.green")
		public ChorusTea chorus = new ChorusTea(3, 0.8, 150);

		@Comment("Stats and effects for cocoa.")
		@LangKey("simplytea.config.tea.cocoa")
		public Cocoa cocoa = new Cocoa();
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

		@Comment("Maximum number of chorus petals dropped from a chorus flower when the plant below is broken.")
		@RangeInt(min = 1, max = 64)
		@LangKey("simplytea.config.tree.max_petals")
		public int max_petals = 3;
	}

	public static class CaffeineTea {
		private CaffeineTea(int defaultHunger, double defaultSaturation, int caffeinatedTime) {
			this.hunger = defaultHunger;
			this.saturation = defaultSaturation;
			this.caffeinated_time = caffeinatedTime;
		}

		@RequiresMcRestart
		@Comment("Hunger restored when drinking this tea.")
		@RangeInt(min = 0, max = 20)
		@LangKey("simplytea.config.tea.hunger")
		public int hunger;

		@RequiresMcRestart
		@Comment("Saturation restored when drinking this tea")
		@RangeDouble(min = 0, max = 10)
		@LangKey("simplytea.config.tea.saturation")
		public double saturation;

		@RequiresMcRestart
		@Comment("Time in seconds for the caffeinated effect from drinking this tea.")
		@RangeInt(min = 0, max = 600)
		@LangKey("simplytea.config.tea.caffeinated_time")
		public int caffeinated_time;
	}

	public static class HerbalTea {
		private HerbalTea(int defaultHunger, double defaultSaturation, int hearts) {
			this.hunger = defaultHunger;
			this.saturation = defaultSaturation;
			this.hearts = hearts;
		}

		@RequiresMcRestart
		@Comment("Hunger restored when drinking this tea.")
		@RangeInt(min = 0, max = 20)
		@LangKey("simplytea.config.tea.hunger")
		public int hunger;

		@RequiresMcRestart
		@Comment("Saturation restored when drinking this tea")
		@RangeDouble(min = 0, max = 10)
		@LangKey("simplytea.config.tea.saturation")
		public double saturation;

		@RequiresMcRestart
		@Comment("Hearts restored when sleeping after drinking this tea.")
		@RangeInt(min = 0, max = 10)
		@LangKey("simplytea.config.tea.herbal.hearts")
		public int hearts;
	}

	public static class ChorusTea {
		private ChorusTea(int defaultHunger, double defaultSaturation, int enderfallingTime) {
			this.hunger = defaultHunger;
			this.saturation = defaultSaturation;
			this.enderfalling_time = enderfallingTime;
		}

		@RequiresMcRestart
		@Comment("Hunger restored when drinking this tea.")
		@RangeInt(min = 0, max = 20)
		@LangKey("simplytea.config.tea.hunger")
		public int hunger;

		@RequiresMcRestart
		@Comment("Saturation restored when drinking this tea")
		@RangeDouble(min = 0, max = 10)
		@LangKey("simplytea.config.tea.saturation")
		public double saturation;

		@RequiresMcRestart
		@Comment("Time in seconds for the enderfalling effect from drinking this tea.")
		@RangeInt(min = 0, max = 600)
		@LangKey("simplytea.config.tea.enderfalling_time")
		public int enderfalling_time;
	}

	public static class Cocoa {
		@RequiresMcRestart
		@Comment("Hunger restored when drinking this tea.")
		@RangeInt(min = 0, max = 20)
		@LangKey("simplytea.config.tea.hunger")
		public int hunger = 4;

		@RequiresMcRestart
		@Comment("Saturation restored when drinking this tea")
		@RangeDouble(min = 0, max = 10)
		@LangKey("simplytea.config.tea.saturation")
		public double saturation = 0.6;

		@Comment("If true, drinking cocoa clears status effects like milk")
		@LangKey("simplytea.config.tea.cocoa.clear_effects")
		public boolean clear_effects = true;
	}

	public static class TeapotCategory {
		@Comment("If true, the teapot will not consume water source blocks when filling. It will still consume water from tank and cauldrons")
		@LangKey("simplytea.config.teapot.infinite_water")
		public boolean infinite_water = true;

		@Comment("If true, the teapot can be filled with water from a cauldron")
		@LangKey("simplytea.config.teapot.fill_from_cauldron")
		public boolean fill_from_cauldron = true;

		@Comment("If true, the teapot can be filled with milk using a cow")
		@LangKey("simplytea.config.teapot.milk_cow")
		public boolean milk_cow = true;

		@Comment("List of fluid IDs treated as water for filling the teapot")
		@LangKey("simplytea.config.teapot.waters")
		public String[] waters = {"water"};

		@Ignore
		public final Set<String> waterSet = new HashSet<>();

		@Comment("List of fluid IDs treated as milk for filling the teapot")
		@LangKey("simplytea.config.teapot.milks")
		public String[] milks = {"milk"};

		@Ignore
		public final Set<String> milkSet = new HashSet<>();
	}

	/**
	 * Called to convert some config members into more efficient Java objects
	 */
	public static void parse() {
		teapot.waterSet.clear();
		teapot.waterSet.addAll(Arrays.asList(teapot.waters));
		teapot.milkSet.clear();
		teapot.milkSet.addAll(Arrays.asList(teapot.milks));
	}
}
