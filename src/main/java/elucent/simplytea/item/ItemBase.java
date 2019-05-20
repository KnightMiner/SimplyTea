package elucent.simplytea.item;

import elucent.simplytea.core.IModeledObject;
import elucent.simplytea.core.Util;
import net.minecraft.item.Item;

public class ItemBase extends Item implements IModeledObject {
	public ItemBase(String name, boolean addToTab) {
		Util.init(this, name, addToTab);
	}
}