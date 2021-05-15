package knightminer.simplytea.data;

import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags.IOptionalNamedTag;

public class SimplyTags {
	public static class Items {
		public static final IOptionalNamedTag<Item> ICE_CUBES = forgeTag("ice_cubes");

		private static IOptionalNamedTag<Item> forgeTag(String name) {
			return ItemTags.createOptional(new ResourceLocation("forge", name));
		}
	}
}
