package knightminer.simplytea.data;

import knightminer.simplytea.SimplyTea;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class SimplyTags {
	/** Initializes the tags, called on init */
	public static void init() {
		Items.init();
	}

	public static class Items {
		public static final TagKey<Item> TEA_CROP = forgeTag("crops/tea");

		public static final TagKey<Item> TEAS = tag("teas");
		public static final TagKey<Item> EXCLUSIVE_TEAS = tag("teas/exclusive");
		public static final TagKey<Item> ICE_CUBES = forgeTag("ice_cubes");


		private static TagKey<Item> tag(String name) {
			return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(SimplyTea.MOD_ID, name));
		}

		private static TagKey<Item> forgeTag(String name) {
			return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("forge", name));
		}

		private static void init() {}
	}
}
