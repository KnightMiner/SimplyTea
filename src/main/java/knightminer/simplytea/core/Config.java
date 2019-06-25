package knightminer.simplytea.core;

// TODO: TOML
public class Config {
	public static TeaCategory tea = new TeaCategory();
	public static TeapotCategory teapot = new TeapotCategory();
	public static TreeCategory tree = new TreeCategory();

	public static class TeaCategory {
		public HerbalTea floral = new HerbalTea(2, 0.5, 1);
		public CaffeineTea green = new CaffeineTea(3, 0.5, 150);
		public CaffeineTea black = new CaffeineTea(4, 0.8, 210);
		public CaffeineTea chai = new CaffeineTea(5, 0.6, 150);
		public HerbalTea chamomile = new HerbalTea(2, 0.5, 2);
		public ChorusTea chorus = new ChorusTea(3, 0.8, 150);
		public Cocoa cocoa = new Cocoa();
	}

	public static class TreeCategory {
		public boolean enable_generation = true;
		public double leaf_growth_chance = 0.05;
	}

	public static class CaffeineTea {
		private CaffeineTea(int defaultHunger, double defaultSaturation, int caffeinatedTime) {
			this.hunger = defaultHunger;
			this.saturation = defaultSaturation;
			this.caffeinated_time = caffeinatedTime;
		}

		public int hunger;
		public double saturation;
		public int caffeinated_time;
	}

	public static class HerbalTea {
		private HerbalTea(int defaultHunger, double defaultSaturation, int hearts) {
			this.hunger = defaultHunger;
			this.saturation = defaultSaturation;
			this.hearts = hearts;
		}

		public int hunger;
		public double saturation;
		public int hearts;
	}

	public static class ChorusTea {
		private ChorusTea(int defaultHunger, double defaultSaturation, int enderfallingTime) {
			this.hunger = defaultHunger;
			this.saturation = defaultSaturation;
			this.enderfalling_time = enderfallingTime;
		}

		public int hunger;
		public double saturation;
		public int enderfalling_time;
	}

	public static class Cocoa {
		public int hunger = 4;
		public double saturation = 0.6;
		public boolean clear_effects = true;
	}

	public static class TeapotCategory {
		public boolean infinite_water = true;
		public boolean fill_from_cauldron = true;
		public boolean milk_cow = true;
	}
}
