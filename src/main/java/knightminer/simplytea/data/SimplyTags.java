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
		public static final IOptionalNamedTag<Item> TEAS = ItemTags.createOptional(new ResourceLocation(SimplyTea.MOD_ID, "teas"));
		public static final IOptionalNamedTag<Item> EXCLUSIVE_TEAS = ItemTags.createOptional(new ResourceLocation(SimplyTea.MOD_ID, "teas/exclusive"));
		public static final IOptionalNamedTag<Item> ICE_CUBES = ItemTags.createOptional(new ResourceLocation("forge", "ice_cubes"));

		private static void init() {}
	}
}
