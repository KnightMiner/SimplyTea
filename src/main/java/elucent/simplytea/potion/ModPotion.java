package elucent.simplytea.potion;

import elucent.simplytea.SimplyTea;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModPotion extends Potion {

	public static final ResourceLocation POTION_ICONS = new ResourceLocation("simplytea:textures/gui/potions.png");

	private int customIcon = -1;
	public ModPotion(String name, boolean badEffect, int liquidColor) {
		super(badEffect, liquidColor);
		this.setRegistryName(new ResourceLocation(SimplyTea.MODID, name));
		this.setPotionName(String.format("%s.effect.%s", SimplyTea.MODID, name));

		// why on earth do these both exist?
		if (!badEffect) {
			this.setBeneficial();
		}
	}

	public ModPotion setCustomIcon(int x, int y) {
		this.customIcon = x + 14*y;
		return this;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderInventoryEffect(int x, int y, PotionEffect effect, net.minecraft.client.Minecraft mc) {
		if (customIcon >= 0) {
			mc.getTextureManager().bindTexture(POTION_ICONS);
			mc.draw(x + 6, y + 7, customIcon % 14 * 18, customIcon / 14 * 18, 18, 18, 255, 255, 255, 255);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderHUDEffect(int x, int y, PotionEffect effect, net.minecraft.client.Minecraft mc, float alpha) {
		if (customIcon >= 0) {
			mc.getTextureManager().bindTexture(POTION_ICONS);
			mc.draw(x + 3, y + 3, customIcon % 14 * 18, customIcon / 14 * 18, 18, 18, 255, 255, 255, (int)(alpha * 255));
		}
	}

}
