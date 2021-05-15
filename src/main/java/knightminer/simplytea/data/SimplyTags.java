package knightminer.simplytea.data;

import knightminer.simplytea.SimplyTea;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags.IOptionalNamedTag;

public class SimplyTags {
	/** Initializes the tags, called on init */
	public static void init() {
		Items.init();
	}

	public static class Items {
		public static final IOptionalNamedTag<Item> TEA_CROP = forgeTag("crops/tea");

		public static final IOptionalNamedTag<Item> TEAS = tag("teas");
		public static final IOptionalNamedTag<Item> EXCLUSIVE_TEAS = tag("teas/exclusive");
		public static final IOptionalNamedTag<Item> ICE_CUBES = forgeTag("ice_cubes");


		private static IOptionalNamedTag<Item> tag(String name) {
			return ItemTags.createOptional(new ResourceLocation(SimplyTea.MOD_ID, name));
		}

		private static IOptionalNamedTag<Item> forgeTag(String name) {
			return ItemTags.createOptional(new ResourceLocation("forge", name));
		}

		private static void init() {}
	}
}
