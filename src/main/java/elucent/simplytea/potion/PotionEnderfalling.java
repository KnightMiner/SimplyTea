package elucent.simplytea.potion;

import net.minecraft.entity.SharedMonsterAttributes;

public class PotionEnderfalling extends ModPotion {

	public PotionEnderfalling(String name) {
		super(name, false, 0x4E2043);

		this.registerPotionAttributeModifier(SharedMonsterAttributes.KNOCKBACK_RESISTANCE, "8ACB8640-6D4E-11E9-A923-1681BE663D3E", 0.25, 2);
	}

}
